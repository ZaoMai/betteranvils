package com.lthoerner.betteranvils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lthoerner.betteranvils.AnvilUtils.isDamageable;

class EnchantUtils {
    static HashMap<Enchantment, Integer> MAX_ENCHANT_LEVELS = new HashMap<>(38);
    static ArrayList<Enchantment[]> INCOMPATIBLE_ENCHANTMENTS = new ArrayList<>(10);

    // 向后兼容的附魔引用（兼容较新的 Bukkit 版本） — Backwards-compatible enchantment references for newer Bukkit versions
    static final Enchantment DAMAGE_ALL = Enchantment.getByKey(NamespacedKey.minecraft("sharpness"));
    static final Enchantment DAMAGE_UNDEAD = Enchantment.getByKey(NamespacedKey.minecraft("smite"));
    static final Enchantment DAMAGE_ARTHROPODS = Enchantment.getByKey(NamespacedKey.minecraft("bane_of_arthropods"));
    static final Enchantment FIRE_ASPECT = Enchantment.getByKey(NamespacedKey.minecraft("fire_aspect"));
    static final Enchantment KNOCKBACK = Enchantment.getByKey(NamespacedKey.minecraft("knockback"));
    static final Enchantment LOOT_BONUS_MOBS = Enchantment.getByKey(NamespacedKey.minecraft("looting"));
    static final Enchantment SWEEPING_EDGE = (Enchantment.getByKey(NamespacedKey.minecraft("sweeping_edge")) != null
        ? Enchantment.getByKey(NamespacedKey.minecraft("sweeping_edge"))
        : Enchantment.getByKey(NamespacedKey.minecraft("sweeping")));

    static final Enchantment IMPALING = Enchantment.getByKey(NamespacedKey.minecraft("impaling"));
    static final Enchantment RIPTIDE = Enchantment.getByKey(NamespacedKey.minecraft("riptide"));
    static final Enchantment LOYALTY = Enchantment.getByKey(NamespacedKey.minecraft("loyalty"));

    static final Enchantment ARROW_DAMAGE = Enchantment.getByKey(NamespacedKey.minecraft("power"));
    static final Enchantment ARROW_FIRE = Enchantment.getByKey(NamespacedKey.minecraft("flame"));
    static final Enchantment ARROW_KNOCKBACK = Enchantment.getByKey(NamespacedKey.minecraft("punch"));
    static final Enchantment ARROW_INFINITE = Enchantment.getByKey(NamespacedKey.minecraft("infinity"));

    static final Enchantment PIERCING = Enchantment.getByKey(NamespacedKey.minecraft("piercing"));
    static final Enchantment QUICK_CHARGE = Enchantment.getByKey(NamespacedKey.minecraft("quick_charge"));
    static final Enchantment MULTISHOT = Enchantment.getByKey(NamespacedKey.minecraft("multishot"));

    static final Enchantment DIG_SPEED = Enchantment.getByKey(NamespacedKey.minecraft("efficiency"));
    static final Enchantment LOOT_BONUS_BLOCKS = Enchantment.getByKey(NamespacedKey.minecraft("fortune"));
    static final Enchantment SILK_TOUCH = Enchantment.getByKey(NamespacedKey.minecraft("silk_touch"));

    static final Enchantment LURE = Enchantment.getByKey(NamespacedKey.minecraft("lure"));
    static final Enchantment LUCK = Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea"));

    static final Enchantment PROTECTION_ENVIRONMENTAL = Enchantment.getByKey(NamespacedKey.minecraft("protection"));
    static final Enchantment PROTECTION_FIRE = Enchantment.getByKey(NamespacedKey.minecraft("fire_protection"));
    static final Enchantment PROTECTION_PROJECTILE = Enchantment.getByKey(NamespacedKey.minecraft("projectile_protection"));
    static final Enchantment PROTECTION_EXPLOSIONS = Enchantment.getByKey(NamespacedKey.minecraft("blast_protection"));
    static final Enchantment PROTECTION_FALL = Enchantment.getByKey(NamespacedKey.minecraft("feather_falling"));
    static final Enchantment THORNS = Enchantment.getByKey(NamespacedKey.minecraft("thorns"));
    static final Enchantment SOUL_SPEED = Enchantment.getByKey(NamespacedKey.minecraft("soul_speed"));
    static final Enchantment SWIFT_SNEAK = Enchantment.getByKey(NamespacedKey.minecraft("swift_sneak"));
    static final Enchantment DEPTH_STRIDER = Enchantment.getByKey(NamespacedKey.minecraft("depth_strider"));
    static final Enchantment OXYGEN = Enchantment.getByKey(NamespacedKey.minecraft("respiration"));
    static final Enchantment WATER_WORKER = Enchantment.getByKey(NamespacedKey.minecraft("aqua_affinity"));
    static final Enchantment FROST_WALKER = Enchantment.getByKey(NamespacedKey.minecraft("frost_walker"));

    static final Enchantment DURABILITY = Enchantment.getByKey(NamespacedKey.minecraft("unbreaking"));
    static final Enchantment MENDING = Enchantment.getByKey(NamespacedKey.minecraft("mending"));

    static final Enchantment BINDING_CURSE = Enchantment.getByKey(NamespacedKey.minecraft("binding_curse"));
    static final Enchantment VANISHING_CURSE = Enchantment.getByKey(NamespacedKey.minecraft("vanishing_curse"));

    static final Enchantment CHANNELING = Enchantment.getByKey(NamespacedKey.minecraft("channeling"));
    static {
        Configuration config = BetterAnvils.getInstance().getConfig();
        boolean USE_VANILLA_MAX_LEVELS = config.getBoolean("use-vanilla-max-levels");

        int SHARPNESS_LEVEL = USE_VANILLA_MAX_LEVELS ? 5 : config.getInt("max-level.sharpness");
        int SMITE_LEVEL = USE_VANILLA_MAX_LEVELS ? 5 : config.getInt("max-level.smite");
        int BANE_OF_ARTHROPODS_LEVEL = USE_VANILLA_MAX_LEVELS ? 5 : config.getInt("max-level.bane-of-arthropods");
        int FIRE_ASPECT_LEVEL = USE_VANILLA_MAX_LEVELS ? 2 : config.getInt("max-level.fire-aspect");
        int KNOCKBACK_LEVEL = USE_VANILLA_MAX_LEVELS ? 2 : config.getInt("max-level.knockback");
        int LOOTING_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.looting");
        int SWEEPING_EDGE_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.sweeping-edge");

        int IMPALING_LEVEL = USE_VANILLA_MAX_LEVELS ? 5 : config.getInt("max-level.impaling");
        int RIPTIDE_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.riptide");
        int LOYALTY_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.loyalty");

        int POWER_LEVEL = USE_VANILLA_MAX_LEVELS ? 5 : config.getInt("max-level.power");
        int FLAME_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.flame");
        int PUNCH_LEVEL = USE_VANILLA_MAX_LEVELS ? 2 : config.getInt("max-level.punch");
        int INFINITY_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.infinity");

        int PIERCING_LEVEL = USE_VANILLA_MAX_LEVELS ? 4 : config.getInt("max-level.piercing");
        int QUICK_CHARGE_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.quick-charge");
        int MULTISHOT_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.multishot");

        int EFFICIENCY_LEVEL = USE_VANILLA_MAX_LEVELS ? 5 : config.getInt("max-level.efficiency");
        int SILK_TOUCH_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.silk-touch");
        int FORTUNE_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.fortune");

        int LURE_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.lure");
        int LUCK_OF_THE_SEA_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.luck-of-the-sea");

        int PROTECTION_LEVEL = USE_VANILLA_MAX_LEVELS ? 4 : config.getInt("max-level.protection");
        int FIRE_PROTECTION_LEVEL = USE_VANILLA_MAX_LEVELS ? 4 : config.getInt("max-level.fire-protection");
        int PROJECTILE_PROTECTION_LEVEL = USE_VANILLA_MAX_LEVELS ? 4 : config.getInt("max-level.projectile-protection");
        int BLAST_PROTECTION_LEVEL = USE_VANILLA_MAX_LEVELS ? 4 : config.getInt("max-level.blast-protection");
        int FEATHER_FALLING_LEVEL = USE_VANILLA_MAX_LEVELS ? 4 : config.getInt("max-level.feather-falling");
        int THORNS_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.thorns");
        int SOUL_SPEED_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.soul-speed");
        int SWIFT_SNEAK_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.swift-sneak");
        int DEPTH_STRIDER_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.depth-strider");
        int RESPIRATION_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.respiration");
        int AQUA_AFFINITY_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.aqua-affinity");
        int FROST_WALKER_LEVEL = USE_VANILLA_MAX_LEVELS ? 2 : config.getInt("max-level.frost-walker");

        int UNBREAKING_LEVEL = USE_VANILLA_MAX_LEVELS ? 3 : config.getInt("max-level.unbreaking");
        int MENDING_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.mending");

        int CURSE_OF_BINDING_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.curse-of-binding");
        int CURSE_OF_VANISHING_LEVEL = USE_VANILLA_MAX_LEVELS ? 1 : config.getInt("max-level.curse-of-vanishing");

        MAX_ENCHANT_LEVELS.put(DAMAGE_ALL, SHARPNESS_LEVEL);
        MAX_ENCHANT_LEVELS.put(DAMAGE_UNDEAD, SMITE_LEVEL);
        MAX_ENCHANT_LEVELS.put(DAMAGE_ARTHROPODS, BANE_OF_ARTHROPODS_LEVEL);
        MAX_ENCHANT_LEVELS.put(FIRE_ASPECT, FIRE_ASPECT_LEVEL);
        MAX_ENCHANT_LEVELS.put(KNOCKBACK, KNOCKBACK_LEVEL);
        MAX_ENCHANT_LEVELS.put(LOOT_BONUS_MOBS, LOOTING_LEVEL);
        MAX_ENCHANT_LEVELS.put(SWEEPING_EDGE, SWEEPING_EDGE_LEVEL);

        MAX_ENCHANT_LEVELS.put(IMPALING, IMPALING_LEVEL);
        MAX_ENCHANT_LEVELS.put(RIPTIDE, RIPTIDE_LEVEL);
        MAX_ENCHANT_LEVELS.put(LOYALTY, LOYALTY_LEVEL);

        MAX_ENCHANT_LEVELS.put(ARROW_DAMAGE, POWER_LEVEL);
        MAX_ENCHANT_LEVELS.put(ARROW_FIRE, FLAME_LEVEL);
        MAX_ENCHANT_LEVELS.put(ARROW_KNOCKBACK, PUNCH_LEVEL);
        MAX_ENCHANT_LEVELS.put(ARROW_INFINITE, INFINITY_LEVEL);

        MAX_ENCHANT_LEVELS.put(PIERCING, PIERCING_LEVEL);
        MAX_ENCHANT_LEVELS.put(QUICK_CHARGE, QUICK_CHARGE_LEVEL);
        MAX_ENCHANT_LEVELS.put(MULTISHOT, MULTISHOT_LEVEL);

        MAX_ENCHANT_LEVELS.put(DIG_SPEED, EFFICIENCY_LEVEL);
        MAX_ENCHANT_LEVELS.put(LOOT_BONUS_BLOCKS, FORTUNE_LEVEL);
        MAX_ENCHANT_LEVELS.put(SILK_TOUCH, SILK_TOUCH_LEVEL);

        MAX_ENCHANT_LEVELS.put(LURE, LURE_LEVEL);
        MAX_ENCHANT_LEVELS.put(LUCK, LUCK_OF_THE_SEA_LEVEL);

        MAX_ENCHANT_LEVELS.put(PROTECTION_ENVIRONMENTAL, PROTECTION_LEVEL);
        MAX_ENCHANT_LEVELS.put(PROTECTION_FIRE, FIRE_PROTECTION_LEVEL);
        MAX_ENCHANT_LEVELS.put(PROTECTION_PROJECTILE, PROJECTILE_PROTECTION_LEVEL);
        MAX_ENCHANT_LEVELS.put(PROTECTION_EXPLOSIONS, BLAST_PROTECTION_LEVEL);
        MAX_ENCHANT_LEVELS.put(PROTECTION_FALL, FEATHER_FALLING_LEVEL);
        MAX_ENCHANT_LEVELS.put(THORNS, THORNS_LEVEL);
        MAX_ENCHANT_LEVELS.put(SOUL_SPEED, SOUL_SPEED_LEVEL);
        MAX_ENCHANT_LEVELS.put(SWIFT_SNEAK, SWIFT_SNEAK_LEVEL);
        MAX_ENCHANT_LEVELS.put(DEPTH_STRIDER, DEPTH_STRIDER_LEVEL);
        MAX_ENCHANT_LEVELS.put(OXYGEN, RESPIRATION_LEVEL);
        MAX_ENCHANT_LEVELS.put(WATER_WORKER, AQUA_AFFINITY_LEVEL);
        MAX_ENCHANT_LEVELS.put(FROST_WALKER, FROST_WALKER_LEVEL);

        MAX_ENCHANT_LEVELS.put(DURABILITY, UNBREAKING_LEVEL);
        MAX_ENCHANT_LEVELS.put(MENDING, MENDING_LEVEL);

        MAX_ENCHANT_LEVELS.put(BINDING_CURSE, CURSE_OF_BINDING_LEVEL);
        MAX_ENCHANT_LEVELS.put(VANISHING_CURSE, CURSE_OF_VANISHING_LEVEL);

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            DAMAGE_ALL,
            DAMAGE_UNDEAD,
            DAMAGE_ARTHROPODS,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            MENDING,
            ARROW_INFINITE,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            MULTISHOT,
            PIERCING,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            LOOT_BONUS_BLOCKS,
            SILK_TOUCH,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            PROTECTION_ENVIRONMENTAL,
            PROTECTION_FIRE,
            PROTECTION_PROJECTILE,
            PROTECTION_EXPLOSIONS,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            DEPTH_STRIDER,
            FROST_WALKER,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            CHANNELING,
            RIPTIDE,
        });

        INCOMPATIBLE_ENCHANTMENTS.add(new Enchantment[] {
            LOYALTY,
            RIPTIDE,
        });
    }

    // 将物品的所有附魔合并为单个映射（包含普通附魔和存储附魔） — Combines all the enchantments of an item, both standard and stored, into a single map
    static Map<Enchantment, Integer> getAllEnchants(ItemStack item) {
        if (item == null) {
            return null;
        }

        // 获取物品的普通附魔 — Get the standard enchantments of the item
        Map<Enchantment, Integer> allEnchantments = new HashMap<>(item.getEnchantments());

        // 如果物品是魔法书，获取其存储的附魔 — Get the stored enchantments of the item if it is an enchanted book
        // （只有魔法书拥有 EnchantmentStorageMeta） — (Only enchanted books have an EnchantmentStorageMeta)
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) meta;
            // 将魔法书的存储附魔与普通附魔合并 — Combine the stored enchantments of the book with its standard enchantments
            // 通常书不会同时有普通和存储附魔，此处为防万一 — Generally books should never have both standard and stored enchantments, but this is here just in case
            allEnchantments.putAll(enchantmentMeta.getStoredEnchants());
        }

        return allEnchantments;
    }

    // 安全地为物品添加附魔，必要时转换为存储附魔 — Safely applies enchantments to an item, switching to stored enchantments if necessary
    // 如果存在冲突或非法附魔会抛出异常 — Throws an exception if there are conflicting or otherwise illegal enchantments
    // TODO: 将此方法中的部分逻辑与 isWastefulCombine() 协调 — TODO: Reconcile some of the logic in this method with isWastefulCombine()
    static @NotNull ItemStack applyEnchants(@NotNull ItemStack item, @NotNull Map<Enchantment, Integer> enchantments) throws IllegalArgumentException {
        // 克隆物品以便在不修改原始物品的情况下检查每个附魔是否合法 — The item is cloned in order to check whether each enchantment is legal without modifying the original item
        // TODO: 是否有不克隆物品也能实现的方法？ — TODO: Is there any way to do this without cloning the item?
        ItemStack enchantedItem = item.clone();

        // 在添加新附魔前重置物品的附魔 — Reset the enchantments on the item before adding the new ones
        stripEnchantments(enchantedItem);

        // 如果列表中存在不兼容的附魔，操作会被取消 — If there are incompatible enchantments in the list, the operation is cancelled
        // TODO: 或许可以用 Enchantment.conflictsWith() 来替代？ — TODO: Maybe replace this with Enchantment.conflictsWith()?
        if (hasIncompatibleEnchants(enchantments)) {
            throw new IllegalArgumentException("Incompatible enchantments detected for item "
                    + enchantedItem.getType().name());
        }

        Map<Enchantment, Integer> normalizedEnchantments = new HashMap<>();

        boolean atLeastOneEnchantmentLevelNotDecreased = false;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int originalLevel = entry.getValue();
            // 如果等级高于该附魔允许的最大等级，则设置为最大等级 — If the level is higher than the maximum allowed level for the enchantment, set it to the maximum
            int normalizedLevel = normalizeEnchantLevel(enchantment, entry.getValue());
            normalizedEnchantments.put(enchantment, normalizedLevel);

            // 如果归一化后等级没有被降低，则设置标志 — If the level was not decreased via normalization, set the flag
            if (normalizedLevel == originalLevel) {
                atLeastOneEnchantmentLevelNotDecreased = true;
            }

            // 如果该附魔对物品可用且不冲突，则继续检查下一个附魔 — If the enchantment is allowed for the given item and does not conflict, check the next enchantment
            // 否则通过抛出异常取消整个操作 — Otherwise, cancel the operation altogether by throwing an exception
            // 出于某些原因 Enchantment.canEnchantItem() 对魔法书不起作用，因此需要特殊处理 — For some reason Enchantment.canEnchantItem() does not work for enchanted books,
            // （因此需要特殊情况处理） — so they must have a special case
            if (!enchantment.canEnchantItem(enchantedItem) && !isBook(enchantedItem)) {
                // TODO: 找到非弃用的替代方法以替换 Enchantment.getName() — TODO: Figure out non-deprecated alternative for Enchantment.getName()
                throw new IllegalArgumentException("Illegal enchantment " + enchantment + " for item "
                        + enchantedItem.getType().name());
            }
        }

        // 如果所有附魔等级都被归一化降低，则取消操作 — If all the enchantments were decreased via normalization, cancel the operation
        if (!atLeastOneEnchantmentLevelNotDecreased) {
            throw new IllegalArgumentException("All enchantment levels above level cap for item "
                    + enchantedItem.getType().name());
        }

        // 如果所有附魔合法，则将它们应用到物品上 — If all the enchantments are legal, apply them to the item
        enchantedItem.addUnsafeEnchantments(normalizedEnchantments);
        // 如果物品是魔法书，则将普通附魔转换为存储附魔 — If the item is an enchanted book, this converts the standard enchantments to stored enchantments
        storeEnchantsInBook(enchantedItem);

        return enchantedItem;
    }

    // 移除物品的所有附魔（包含普通附魔与存储附魔） — Removes all the enchantments of an item, both standard and stored
    static void stripEnchantments(@NotNull ItemStack item) {
        // 移除物品的普通附魔 — Strip the standard enchantments of the item
        item.getEnchantments().keySet().forEach(item::removeEnchantment);

        // 如果是魔法书，移除其存储附魔 — Strip the stored enchantments of the item if it is an enchanted book
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) meta;
            enchantmentMeta.getStoredEnchants().keySet().forEach(enchantmentMeta::removeStoredEnchant);
            item.setItemMeta(enchantmentMeta);
        }
    }

    // 如果给定物品是魔法书，则将其普通附魔转换为存储附魔 — If the given item is an enchanted book, converts its standard enchantments into stored enchantments
    static void storeEnchantsInBook(@NotNull ItemStack item) {
        // 存储附魔只在魔法书上有意义 — Stored enchantments are only relevant for enchanted books
        if (item.getType() != Material.ENCHANTED_BOOK) {
            return;
        }

        // 获取物品的所有附魔并转换为存储附魔 — Get all the enchantments of the item and convert them to stored enchantments
        EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) item.getItemMeta();
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            assert enchantmentMeta != null;

            // 如果魔法书已经有该附魔，则取更高等级 — If the book already has an enchantment, take the higher level
            int standardLevel = entry.getValue();
            int resultLevel;
            if (enchantmentMeta.hasStoredEnchant(entry.getKey())) {
                int storedLevel = enchantmentMeta.getStoredEnchantLevel(entry.getKey());
                resultLevel = Math.max(standardLevel, storedLevel);
            } else {
                resultLevel = standardLevel;
            }

            enchantmentMeta.addStoredEnchant(entry.getKey(), resultLevel, true);
            enchantmentMeta.removeEnchant(entry.getKey());
        }

        item.setItemMeta(enchantmentMeta);
    }

    // Combines the enchantments of two items into a single map
    // Note: If both items are the same, and the left item is not at full durability, this should be used
    // in conjunction with getCombineRepairResultDurability
    static @NotNull Map<Enchantment, Integer> combineEnchants(@NotNull ItemStack leftItem, @NotNull ItemStack rightItem) {
        Map<Enchantment, Integer> leftEnchantments = getAllEnchants(leftItem);
        Map<Enchantment, Integer> rightEnchantments = getAllEnchants(rightItem);
        Map<Enchantment, Integer> resultEnchantments = new HashMap<>();

        // Add the left enchantments
        for (Map.Entry<Enchantment, Integer> entry : leftEnchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int leftLevel = entry.getValue();

            // 如果两个物品都带有该附魔，则选择更高等级或在相同等级时递增等级 — If the enchantment is on both items, add the higher level or increment the level
            // 如果该附魔仅在左侧物品上，则直接添加 — If the enchantment is only on the left item, add it
            if (rightEnchantments.containsKey(enchantment)) {
                // 如果等级不同，取更高等级 — If the levels are different, add the higher level
                // 如果等级相同，则将等级 +1 — If the levels are the same, increment the level
                int rightLevel = rightEnchantments.get(enchantment);
                if (leftLevel != rightLevel) {
                    resultEnchantments.put(enchantment, Math.max(leftLevel, rightLevel));
                } else {
                    resultEnchantments.put(enchantment, leftLevel + 1);
                }
            } else {
                // 如果该附魔仅在左侧物品存在，则添加 — If the enchantment is only on the left item, add it
                resultEnchantments.put(enchantment, leftLevel);
            }
        }

        // Add the right enchantments
        for (Map.Entry<Enchantment, Integer> entry : rightEnchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();

            // 如果该附魔仅在右侧物品存在，则添加 — If the enchantment is only on the right item, add it
            if (!leftEnchantments.containsKey(enchantment)) {
                resultEnchantments.put(enchantment, level);
            }
        }

        return resultEnchantments;
    }

    // 判断将左侧附魔与右侧附魔合并是否属于浪费操作 — Determines whether combining the left enchantments with the right enchantments would be wasteful
    // 目的是防止将两件具有相同附魔但不同等级的物品合并，最终得到与其中一件相同的物品 — This is to prevent combining two items with the same enchantments, but different levels, to create an item with
    // the same enchantments as one of the original items
    static boolean isWastefulCombine(@NotNull Map<Enchantment, Integer> leftEnchants, @NotNull Map<Enchantment, Integer> rightEnchants) {
        // 如果左侧物品的附魔与右侧不同，则不算浪费 — If the left item has different enchantments than the right item, it is not wasteful
        if (!leftEnchants.keySet().equals(rightEnchants.keySet())) {
            return false;
        }

        boolean leftHasAtLeastOneHigherLevel = false;
        boolean rightHasAtLeastOneHigherLevel = false;
        for (Map.Entry<Enchantment, Integer> entry : leftEnchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int leftLevel = entry.getValue();
            int rightLevel = rightEnchants.get(enchantment);

            if (leftLevel > rightLevel) {
                leftHasAtLeastOneHigherLevel = true;
            } else if (rightLevel > leftLevel) {
                rightHasAtLeastOneHigherLevel = true;
            }

            // 如果等级相同且尚未到达该附魔的最大等级，则合并不算浪费 — If the levels are the same and the enchantment is not at its
            // 已有最大等级，则合并不算浪费 — max level already, the combine is not wasteful
            if (leftLevel == rightLevel && leftLevel != MAX_ENCHANT_LEVELS.get(enchantment)) {
                return false;
            }

            // 如果双方在至少一个附魔上都有更高等级，则合并不算浪费 — If both items have the higher level on at least one enchantment, the combine is not wasteful
            if (leftHasAtLeastOneHigherLevel && rightHasAtLeastOneHigherLevel) {
                return false;
            }
        }

        // 如果两件物品的附魔相同，但其中一件在每个附魔上都更高，则合并属于浪费 — If both items have the same enchantments, but one has the higher level on every enchantment,
        // 则合并属于浪费 — the combine is wasteful
        return true;
    }

    // 计算一个物品上所有附魔等级的总和 — Calculates the total number of enchantment levels on an item
    // 用于铁砧的费用计算 — Used for anvil cost calculations
    static int totalLevels(@NotNull ItemStack item) {
        Map<Enchantment, Integer> enchantments = getAllEnchants(item);
        int totalLevels = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            totalLevels += entry.getValue();
        }

        return totalLevels;
    }

    // 判断物品是否可附魔（例如书、工具、武器或护甲） — Determines if an item can be enchanted, indicated by the fact that it is either a book, tool, weapon, or armor
    static boolean isEnchantable(@NotNull ItemStack item) {
        // TODO: 排除不可附魔的工具（是否存在？） — TODO: Exclude non-enchantable tools (are there any?)
        return isDamageable(item) || isBook(item);
    }

    // 判断给定物品是否为普通书或魔法书 — Determines if the given item is a book or enchanted book
    static boolean isBook(ItemStack item) {
        if (item == null) {
            return false;
        }

        Material type = item.getType();
        return type == Material.ENCHANTED_BOOK || type == Material.BOOK;
    }

    // 根据给定等级是否超过最大等级，返回给定等级或该附魔的最大等级 — Either returns the given enchantment level or the maximum level for the enchantment,
    // 取决于给定等级是否超过最大值 — depending on whether the given level exceeds the maximum
    static int normalizeEnchantLevel(Enchantment enchantment, int level) {
        return Math.min(level, MAX_ENCHANT_LEVELS.get(enchantment));
    }

    // 判断一组附魔中是否包含互相不兼容的附魔 — Determines whether a set of enchantments contains incompatible enchantments
    static boolean hasIncompatibleEnchants(Map<Enchantment, Integer> enchantments) {
        for (Enchantment[] incompatibleEnchantments : INCOMPATIBLE_ENCHANTMENTS) {
            int count = 0;
            for (Enchantment enchantment : incompatibleEnchantments) {
                if (enchantments.containsKey(enchantment)) {
                    count++;
                    if (count > 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
