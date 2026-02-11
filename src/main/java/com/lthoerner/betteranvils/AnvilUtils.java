package com.lthoerner.betteranvils;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static com.lthoerner.betteranvils.EnchantUtils.*;

enum AnvilOperation {
    RENAME,
    COMBINE_ENCHANT,
    BOOK_ENCHANT,
    COMBINE_REPAIR,
    MATERIAL_REPAIR,
}

enum DamageableMaterial {
    LEATHER,
    WOOD,
    STONE,
    IRON,
    GOLD,
    DIAMOND,
    NETHERITE,
    SCUTE,
    PHANTOM_MEMBRANE,
}

class AnvilAction {
    final Plugin plugin;

    final ItemStack leftItem;
    final ItemStack rightItem;
    final String renameText;
    final @NotNull ArrayList<AnvilOperation> operations;

    final boolean SHOW_WARNINGS;
    final boolean SHOW_ERRORS;
    final int MAX_COST;
    final double ENCHANT_COST_MULTIPLIER;
    final int RENAME_COST;
    final int DURABILITY_PER_LEVEL;
    final int MIN_REPAIR_COST;

    AnvilAction(ItemStack leftItem, ItemStack rightItem, String renameText) {
        this.plugin = BetterAnvils.getInstance();

        this.leftItem = leftItem;
        this.rightItem = rightItem;
        this.renameText = renameText;
        // 一个 AnvilAction 由一个或多个 AnvilOperation 构成 — An AnvilAction consists of one or more AnvilOperations
        this.operations = AnvilUtils.getAnvilOperations(leftItem, rightItem, renameText);

        Configuration config = plugin.getConfig();

        this.SHOW_WARNINGS = config.getBoolean("show-warnings");
        this.SHOW_ERRORS = config.getBoolean("show-errors");
        this.MAX_COST = config.getInt("max-cost");
        this.ENCHANT_COST_MULTIPLIER = config.getDouble("enchant-cost-multiplier");
        this.RENAME_COST = config.getInt("rename-cost");
        this.DURABILITY_PER_LEVEL = config.getInt("durability-per-level");
        this.MIN_REPAIR_COST = config.getInt("min-repair-cost");
    }

    AnvilResult getResult() {
        if (operations.isEmpty()) {
            return null;
        }

        boolean combineEnchant = operations.contains(AnvilOperation.COMBINE_ENCHANT);
        boolean bookEnchant = operations.contains(AnvilOperation.BOOK_ENCHANT);
        boolean combineRepair = operations.contains(AnvilOperation.COMBINE_REPAIR);
        boolean materialRepair = operations.contains(AnvilOperation.MATERIAL_REPAIR);
        boolean rename = operations.contains(AnvilOperation.RENAME);

        ItemStack resultItem = null;
        int cost = 0;

        // 物品不能同时进行合并附魔和书籍附魔 — An item cannot be combine enchanted and book enchanted simultaneously
        if (combineEnchant ^ bookEnchant) {
            resultItem = cloneLeftItemIfResultNull(leftItem, null);

            Map<Enchantment, Integer> combinedEnchants = combineEnchants(leftItem, rightItem);
            try {
                resultItem = applyEnchants(resultItem, combinedEnchants);
            } catch (IllegalArgumentException e) {
                // 如果附魔失败，必须取消整个操作，防止物品在重命名或修复过程中丢失附魔 — If enchantment has failed, the entire operation must be canceled to prevent the result item
                    // 在重命名或修复过程中丢失附魔 — from being losing its enchantments in the process of being renamed or repaired
                if (SHOW_WARNINGS) plugin.getLogger().warning(e.getMessage());
                return null;
            }

            cost += ENCHANT_COST_MULTIPLIER * totalLevels(resultItem);
        }

        // 物品不能同时进行合并修复和材料修复 — An item cannot be combine repaired and material repaired simultaneously
        if (combineRepair ^ materialRepair) {
            resultItem = cloneLeftItemIfResultNull(leftItem, resultItem);

            Damageable metaBeforeRepair = (Damageable) resultItem.getItemMeta();
            assert metaBeforeRepair != null;
            int damageBeforeRepair = metaBeforeRepair.getDamage();

            int damageAfterRepair;
            if (combineRepair) {
                damageAfterRepair = AnvilUtils.combineDamage(leftItem, rightItem);
            } else {
                try {
                    damageAfterRepair = AnvilUtils.materialRepairDamage(leftItem, rightItem.getAmount());
                } catch (IllegalArgumentException e) {
                    if(SHOW_ERRORS) {
                        plugin.getLogger().severe(e.getMessage());
                        plugin.getLogger().severe("This is a bug in BetterAnvils. Please report it at " +
                                "https://github.com/lthoerner/betteranvils.");
                    }

                    return null;
                }
            }

            Damageable resultMeta = (Damageable) resultItem.getItemMeta();
            assert resultMeta != null;
            resultMeta.setDamage(damageAfterRepair);
            resultItem.setItemMeta(resultMeta);

            // 如果物品正在被合并修复，可能不会被合并附魔，但仍需合并它们的附魔 — If the items are being combine repaired, they may not necessarily be combine enchanted, but
            // 仍需合并附魔 — their enchantments must be combined nonetheless
            if (combineRepair) {
                Map<Enchantment, Integer> combinedEnchants = combineEnchants(leftItem, rightItem);
                // 这通常不会失败：若两件物品在合并修复时未经过 combineEnchant/bookEnchant 验证，则通常其中一件未被附魔，无需额外验证 — This should never fail, because if two items are being combine repaired without having already
                // 经过 combineEnchant/bookEnchant 部分的验证，则其中一件通常未被附魔，验证不必要 — gone through the validation process in the combineEnchant/bookEnchant section, one of the items is
                // （未被附魔，因此无需验证） — not enchanted and validation is not necessary
                try {
                    resultItem = applyEnchants(resultItem, combinedEnchants);
                } catch (IllegalArgumentException e) {
                    if (SHOW_ERRORS) {
                        plugin.getLogger().severe(e.getMessage());
                        plugin.getLogger().severe("This is a bug in BetterAnvils. Please report it at " +
                                "https://github.com/lthoerner/betteranvils.");
                    }

                    return null;
                }
            }

            int healedDamage = damageBeforeRepair - damageAfterRepair;

            cost += Math.max(MIN_REPAIR_COST, healedDamage / DURABILITY_PER_LEVEL);
        }

        if (rename) {
            // 如果在重命名之前已有其他操作且结果物品与重命名前的左侧物品没有差别，则不应执行重命名操作 — If there has been some other operation performed on the item, and the result item is not any different
            // 这通常发生在附魔已达上限，合并附魔无法提升等级时（此时重命名无意义） — from the left item prior to the rename operation, then no action should be performed
            // 这通常发生在附魔已达上限且合并附魔不会提升附魔等级的情况 — This generally happens if an enchantment has reached its max level and combine enchanting
            // （将不会提升附魔等级） — will not increase the level of the enchantment
            if (resultItem != null && resultItem.equals(leftItem)) {
                return null;
            }

            resultItem = cloneLeftItemIfResultNull(leftItem, resultItem);

            ItemMeta resultMeta = resultItem.getItemMeta();
            assert resultMeta != null;
            resultMeta.setDisplayName(renameText);
            resultItem.setItemMeta(resultMeta);

            cost += RENAME_COST;
        }

        assert resultItem != null;
        // 如果结果物品与左侧物品相同，铁砧不应执行任何动作，以避免玩家意外消耗经验或材料在无效操作上 — If the result item is the same as the left item, the anvil does not take any action,
        // （避免浪费） — in order to prevent the player from accidentally spending experience or materials on a useless action
        if (resultItem.equals(leftItem)) {
            return null;
        }

        return new AnvilResult(resultItem, Math.min(MAX_COST, cost));
    }

    // 该函数用于确保 AnvilOperations 可以独立或组合应用：除非结果已存在，否则将结果设为左侧物品的副本 — This function is used to ensure that the AnvilOperations can be applied both independently and
    // 并在需要时将结果强制为左侧物品（除非结果已存在） — in conjunction by forcing the result to be the left item unless it is already instantiated
    static @NotNull ItemStack cloneLeftItemIfResultNull(@NotNull ItemStack leftItem, ItemStack resultItem) {
        if (resultItem == null) {
            return leftItem.clone();
        }

        return resultItem;
    }
}

class AnvilUtils {
    static final double MATERIAL_REPAIR_COST_MULTIPLIER =
            BetterAnvils.getInstance().getConfig().getDouble("material-repair-cost-multiplier");

    // 根据输入物品判断铁砧正在执行的操作类型 — Determines the type of operations that the anvil is performing based on the input items
    static @NotNull ArrayList<AnvilOperation> getAnvilOperations(ItemStack leftItem, ItemStack rightItem, String renameText) {
        // 如果左侧槽为空，铁砧不执行任何操作 — If the left slot is empty, the anvil does nothing
        if (leftItem == null) {
            return new ArrayList<>();
        }

        ArrayList<AnvilOperation> operations = new ArrayList<>();

        ItemMeta leftMeta = leftItem.getItemMeta();
        assert leftMeta != null;
        String leftName = leftMeta.getDisplayName();

        // 如果指定了新名称，铁砧正在重命名物品 — If there is a new name specified for the item, the anvil is renaming the item
        if (renameText != null && !renameText.equals(leftName)) {
            operations.add(AnvilOperation.RENAME);
        }

        // 如果右侧槽没有物品，铁砧仅执行重命名操作并不进行其他操作 — If there is no item in the right slot, the anvil only renames the item and does nothing else
        if (rightItem == null) {
            return operations;
        }

        // 如果两件物品相同，表示在执行合并操作 — If both items are the same, they are being combined
        if (leftItem.getType() == rightItem.getType()) {
            // 如果至少一件物品带有附魔，则进行合并附魔（combine enchanted） — If at least one item is enchanted, the items are being "combine enchanted"
            Map<Enchantment, Integer> leftEnchantments = getAllEnchants(leftItem);
            Map<Enchantment, Integer> rightEnchantments = getAllEnchants(rightItem);
            boolean bothItemsEnchanted = !leftEnchantments.isEmpty() && !rightEnchantments.isEmpty();
            boolean wastefulEnchant = isWastefulCombine(leftEnchantments, rightEnchantments);
            boolean leftItemEnchantable = isEnchantable(leftItem);
            if (!wastefulEnchant && bothItemsEnchanted && leftItemEnchantable) {
                operations.add(AnvilOperation.COMBINE_ENCHANT);
            }

            // 如果物品可损坏且耐久未满，则进行合并修复（combine repaired） — If the items are damageable and are not at full durability, they are being "combine repaired"
            if (isDamageable(leftItem) && isDamaged(leftItem)) {
                operations.add(AnvilOperation.COMBINE_REPAIR);
            }
        }

        // 如果右侧为魔法书且左侧不是魔法书，则为书籍附魔（book enchanted） — If the right item is an enchanted book, it is being "book enchanted," unless the left item is also an enchanted book
        // 该例外存在因为合并附魔与书籍附魔互斥，主要是为了避免重复计费 — This exception needs to exist because combine enchanting and book enchanting are mutually exclusive,
        // （会导致重复的等级费用） — mostly to avoid duplicate level costs
        if (rightItem.getType() == Material.ENCHANTED_BOOK && leftItem.getType() != Material.ENCHANTED_BOOK) {
            operations.add(AnvilOperation.BOOK_ENCHANT);
        }

        // 如果左侧物品可修复，且右侧为用于修复它的材料，则为材料修复（material repaired） — If the left item is repairable, and the right item is a material used to repair it, it is being "material repaired"
        if (materialRepairsItem(leftItem, rightItem)) {
            operations.add(AnvilOperation.MATERIAL_REPAIR);
        }

        // 如果除重命名外没有其他操作且右侧有物品，则重命名会不必要地消耗右侧物品，因此移除重命名操作 — If no operations other than RENAME are specified, and there is an item in the right-hand slot, the rename
        // 操作会不必要地消耗右侧物品，故做此判断 — operation would consume the right item unnecessarily, hence this condition
        // 如果此时 operations 只有一个元素，上述情况会自动成立 — All these conditions would automatically be true if operations only has one element at this point
        if (operations.size() == 1) {
            operations.remove(AnvilOperation.RENAME);
        }

        return operations;
    }

    // 获取合并修复后物品的伤害值（以损伤值表示，而非耐久度），因为 Damageable 的工作方式如此 — Gets the result of combine repairing two items in an anvil, represented by the amount of damage rather than
    // 耐久度 — the amount of durability due to the way that Damageable works
    // 注意：如果一件或两件物品被附魔，应与 combineEnchants 配合使用 — Note: If one or both items are enchanted, this should be used in conjunction with combineEnchants
    static int combineDamage(@NotNull ItemStack leftItem, @NotNull ItemStack rightItem) {
        Damageable leftMeta = (Damageable) leftItem.getItemMeta();
        Damageable rightMeta = (Damageable) rightItem.getItemMeta();
        // 两件物品必须是可损坏的才会到达此处，否则 AnvilAction 不应被分类为 COMBINE_REPAIR — Both items must be damageable to get to this point, because otherwise the AnvilAction
        // 不应被归类为 COMBINE_REPAIR — should not have been classified as a COMBINE_REPAIR
        assert leftMeta != null;
        assert rightMeta != null;

        int maxDurability = leftItem.getType().getMaxDurability();

        int leftRemainingDurability = maxDurability - leftMeta.getDamage();
        int rightRemainingDurability = maxDurability - rightMeta.getDamage();

        int combinedDurability = leftRemainingDurability + rightRemainingDurability;
        // 如果合并后的耐久度超过物品最大耐久度，则结果伤害为 0 — If the combined durability is higher than the max durability for the item, the resulting damage is 0
        int combinedDamage = maxDurability - combinedDurability;
        if (combinedDamage < 0) {
            combinedDamage = 0;
        }

        return combinedDamage;
    }

    // 根据提供的材料数量，计算材料修复后物品的伤害值 — Gets the damage value of an item being material repaired, given the amount of materials provided
    static int materialRepairDamage(@NotNull ItemStack item, int materialCount) throws IllegalArgumentException {
        Damageable itemMeta = (Damageable) item.getItemMeta();
        assert itemMeta != null;

        int maxDurability = item.getType().getMaxDurability();
        int remainingDurability = maxDurability - itemMeta.getDamage();

        int fullRepairMaterialCost = repairMaterialCost(item);

        int durabilityPerMaterial = maxDurability / fullRepairMaterialCost;
        int materialAddedDurability = materialCount * durabilityPerMaterial;
        int combinedDurability = remainingDurability + materialAddedDurability;

        int combinedDamage = maxDurability - combinedDurability;
        if (combinedDamage < 0) {
            combinedDamage = 0;
        }

        return combinedDamage;
    }

    // 判断给定物品是否为工具/武器/护甲（通过其是否可损坏判断） — Determines if the given item is a tool, weapon, or armor, indicated by the fact that it is damageable
    static boolean isDamageable(@NotNull ItemStack item) {
        return item.getItemMeta() instanceof Damageable;
    }

    // 判断给定物品是否既可损坏且未满耐久（即已损坏） — Determines if the given item both is damageable and is not at full durability (is damaged)
    static boolean isDamaged(@NotNull ItemStack item) {
        if (!isDamageable(item)) {
            return false;
        }

        Damageable meta = (Damageable) item.getItemMeta();
        // 如果物品可损坏，则其元数据应为 Damageable，因此这里不应为 null — The item has to have a Damageable meta if it is damageable, so this should never be null
        assert meta != null;
        return meta.hasDamage();
    }

    // 判断铁砧右侧材料是否匹配左侧的工具/武器/护甲（用于判断是否可用作材料修复） — Determines if the material on the right side of the anvil matches the tool, weapon, or armor on the left side
    static boolean materialRepairsItem(@NotNull ItemStack leftItem, @NotNull ItemStack rightItem) {
        if (!isDamageable(leftItem) || repairMaterialType(rightItem) == null) {
            return false;
        }

        DamageableMaterial leftMaterial = getRepairMaterial(leftItem);
        DamageableMaterial rightMaterial = repairMaterialType(rightItem);
        return leftMaterial == rightMaterial;
    }

    // 判断用于修复给定物品的材料类型，若无法用材料修复则返回 null — Determines the material used to repair the given item, or null if the item cannot be repaired with a material
    static DamageableMaterial getRepairMaterial(@NotNull ItemStack item) {
        // 只有可损坏的物品可以被修复 — Only damageable items can be repaired
        if (!isDamageable(item)) {
            return null;
        }

        Material damageableItemType = item.getType();
        // 针对非标准材料或无材质前缀的工具的特殊处理 — Special cases for tools that are not made of the standard materials, or do not have a material prefix
        if (damageableItemType == Material.SHIELD) {
            return DamageableMaterial.WOOD;
        } else if (damageableItemType == Material.TURTLE_HELMET) {
            return DamageableMaterial.SCUTE;
        } else if (damageableItemType == Material.ELYTRA) {
            return DamageableMaterial.PHANTOM_MEMBRANE;
        }

        // 大多数工具/武器/护甲的一般处理逻辑 — General cases for most tools, weapons, and armor
        String damageableItemName = damageableItemType.name();
        if (damageableItemName.startsWith("LEATHER_")) {
            return DamageableMaterial.LEATHER;
        } else if (damageableItemName.startsWith("WOODEN_")) {
            return DamageableMaterial.WOOD;
        } else if (damageableItemName.startsWith("STONE_")) {
            return DamageableMaterial.STONE;
        } else if (damageableItemName.startsWith("IRON_") || damageableItemName.startsWith("CHAINMAIL_")) {
            return DamageableMaterial.IRON;
        } else if (damageableItemName.startsWith("GOLDEN_")) {
            return DamageableMaterial.GOLD;
        } else if (damageableItemName.startsWith("DIAMOND_")) {
            return DamageableMaterial.DIAMOND;
        } else if (damageableItemName.startsWith("NETHERITE_")) {
            return DamageableMaterial.NETHERITE;
        } else {
            return null;
        }
    }

    // 如果给定物品是用于修复工具/武器/护甲的材料，将其从 Material 转换为 DamageableMaterial — If the given item is a material used to repair tools, weapons, or armor, converts it from a Material to a DamageableMaterial
    static DamageableMaterial repairMaterialType(@NotNull ItemStack item) {
        Material itemType = item.getType();

        if (itemType.name().endsWith("_PLANKS")) {
            return DamageableMaterial.WOOD;
        }

        if (itemType == Material.LEATHER) {
            return DamageableMaterial.LEATHER;
        } else if (itemType == Material.COBBLESTONE || itemType == Material.COBBLED_DEEPSLATE || itemType == Material.BLACKSTONE) {
            return DamageableMaterial.STONE;
        } else if (itemType == Material.IRON_INGOT) {
            return DamageableMaterial.IRON;
        } else if (itemType == Material.GOLD_INGOT) {
            return DamageableMaterial.GOLD;
        } else if (itemType == Material.DIAMOND) {
            return DamageableMaterial.DIAMOND;
        } else if (itemType == Material.NETHERITE_INGOT) {
            return DamageableMaterial.NETHERITE;
        } else if (itemType.name().equals("SCUTE")) {
            return DamageableMaterial.SCUTE;
        } else if (itemType.name().equals("PHANTOM_MEMBRANE")) {
            return DamageableMaterial.PHANTOM_MEMBRANE;
        } else {
            return null;
        }
    }

    // 计算将物品从 0 修复到满耐久所需的材料数量 — Gets the amount of repair material required to repair a given item from 0 to full durability
    static int repairMaterialCost(@NotNull ItemStack item) throws IllegalArgumentException {
        Material damageableItemType = item.getType();
        String damageableItemName = damageableItemType.name();

        double materialCount;

        if (damageableItemType == Material.SHIELD) {
            materialCount = 6;
        } else if (damageableItemType == Material.ELYTRA) {
            materialCount = 7;
        } else if (damageableItemName.endsWith("_HELMET") || damageableItemName.endsWith("_CAP")) {
            materialCount = 5;
        } else if (damageableItemName.endsWith("_CHESTPLATE") || damageableItemName.endsWith("_TUNIC")) {
            materialCount = 8;
        } else if (damageableItemName.endsWith("_LEGGINGS") || damageableItemName.endsWith("_PANTS")) {
            materialCount = 7;
        } else if (damageableItemName.endsWith("_BOOTS")) {
            materialCount = 4;
        } else if (damageableItemName.endsWith("_SWORD")) {
            materialCount = 2;
        } else if (damageableItemName.endsWith("_PICKAXE")) {
            materialCount = 3;
        } else if (damageableItemName.endsWith("_AXE")) {
            materialCount = 3;
        } else if (damageableItemName.endsWith("_SHOVEL")) {
            materialCount = 1;
        } else if (damageableItemName.endsWith("_HOE")) {
            materialCount = 2;
        } else {
            throw new IllegalArgumentException(
                    "Item " + damageableItemType.name() + " has no corresponding repair material");
        }

        return (int) (MATERIAL_REPAIR_COST_MULTIPLIER * materialCount);
    }
}

class AnvilResult {
    final @NotNull ItemStack resultItem;
    final int cost;

    AnvilResult(@NotNull ItemStack resultItem, int cost) {
        this.resultItem = resultItem;
        this.cost = cost;
    }
}
