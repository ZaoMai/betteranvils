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

    // Backwards-compatible enchantment references for newer Bukkit versions
    static final Enchantment DAMAGE_ALL = Enchantment.getByKey(NamespacedKey.minecraft("sharpness"));
    static final Enchantment DAMAGE_UNDEAD = Enchantment.getByKey(NamespacedKey.minecraft("smite"));
    static final Enchantment DAMAGE_ARTHROPODS = Enchantment.getByKey(NamespacedKey.minecraft("bane_of_arthropods"));
    static final Enchantment FIRE_ASPECT = Enchantment.getByKey(NamespacedKey.minecraft("fire_aspect"));
    static final Enchantment KNOCKBACK = Enchantment.getByKey(NamespacedKey.minecraft("knockback"));
    static final Enchantment LOOT_BONUS_MOBS = Enchantment.getByKey(NamespacedKey.minecraft("looting"));
    static final Enchantment SWEEPING_EDGE = Enchantment.getByKey(NamespacedKey.minecraft("sweeping"));

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

    // Combines all the enchantments of an item, both standard and stored, into a single map
    static Map<Enchantment, Integer> getAllEnchants(ItemStack item) {
        if (item == null) {
            return null;
        }

        // Get the standard enchantments of the item
        Map<Enchantment, Integer> allEnchantments = new HashMap<>(item.getEnchantments());

        // Get the stored enchantments of the item if it is an enchanted book
        // (Only enchanted books have an EnchantmentStorageMeta)
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) meta;
            // Combine the stored enchantments of the book with its standard enchantments
            // Generally books should never have both standard and stored enchantments, but this is here just in case
            allEnchantments.putAll(enchantmentMeta.getStoredEnchants());
        }

        return allEnchantments;
    }

    // Safely applies enchantments to an item, switching to stored enchantments if necessary
    // Throws an exception if there are conflicting or otherwise illegal enchantments
    // TODO: Reconcile some of the logic in this method with isWastefulCombine()
    static @NotNull ItemStack applyEnchants(@NotNull ItemStack item, @NotNull Map<Enchantment, Integer> enchantments) throws IllegalArgumentException {
        // The item is cloned in order to check whether each enchantment is legal without modifying the original item
        // TODO: Is there any way to do this without cloning the item?
        ItemStack enchantedItem = item.clone();

        // Reset the enchantments on the item before adding the new ones
        stripEnchantments(enchantedItem);

        // If there are incompatible enchantments in the list, the operation is cancelled
        // TODO: Maybe replace this with Enchantment.conflictsWith()?
        if (hasIncompatibleEnchants(enchantments)) {
            throw new IllegalArgumentException("Incompatible enchantments detected for item "
                    + enchantedItem.getType().name());
        }

        Map<Enchantment, Integer> normalizedEnchantments = new HashMap<>();

        boolean atLeastOneEnchantmentLevelNotDecreased = false;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int originalLevel = entry.getValue();
            // If the level is higher than the maximum allowed level for the enchantment, set it to the maximum
            int normalizedLevel = normalizeEnchantLevel(enchantment, entry.getValue());
            normalizedEnchantments.put(enchantment, normalizedLevel);

            // If the level was not decreased via normalization, set the flag
            if (normalizedLevel == originalLevel) {
                atLeastOneEnchantmentLevelNotDecreased = true;
            }

            // If the enchantment is allowed for the given item and does not conflict, check the next enchantment
            // Otherwise, cancel the operation altogether by throwing an exception
            // For some reason Enchantment.canEnchantItem() does not work for enchanted books,
            // so they must have a special case
            if (!enchantment.canEnchantItem(enchantedItem) && !isBook(enchantedItem)) {
                // TODO: Figure out non-deprecated alternative for Enchantment.getName()
                throw new IllegalArgumentException("Illegal enchantment " + enchantment + " for item "
                        + enchantedItem.getType().name());
            }
        }

        // If all the enchantments were decreased via normalization, cancel the operation
        if (!atLeastOneEnchantmentLevelNotDecreased) {
            throw new IllegalArgumentException("All enchantment levels above level cap for item "
                    + enchantedItem.getType().name());
        }

        // If all the enchantments are legal, apply them to the item
        enchantedItem.addUnsafeEnchantments(normalizedEnchantments);
        // If the item is an enchanted book, this converts the standard enchantments to stored enchantments
        storeEnchantsInBook(enchantedItem);

        return enchantedItem;
    }

    // Removes all the enchantments of an item, both standard and stored
    static void stripEnchantments(@NotNull ItemStack item) {
        // Strip the standard enchantments of the item
        item.getEnchantments().keySet().forEach(item::removeEnchantment);

        // Strip the stored enchantments of the item if it is an enchanted book
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) meta;
            enchantmentMeta.getStoredEnchants().keySet().forEach(enchantmentMeta::removeStoredEnchant);
            item.setItemMeta(enchantmentMeta);
        }
    }

    // If the given item is an enchanted book, converts its standard enchantments into stored enchantments
    static void storeEnchantsInBook(@NotNull ItemStack item) {
        // Stored enchantments are only relevant for enchanted books
        if (item.getType() != Material.ENCHANTED_BOOK) {
            return;
        }

        // Get all the enchantments of the item and convert them to stored enchantments
        EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta) item.getItemMeta();
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            assert enchantmentMeta != null;

            // If the book already has an enchantment, take the higher level
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

            // If the enchantment is on both items, add the higher level or increment the level
            // If the enchantment is only on the left item, add it
            if (rightEnchantments.containsKey(enchantment)) {
                // If the levels are different, add the higher level
                // If the levels are the same, increment the level
                int rightLevel = rightEnchantments.get(enchantment);
                if (leftLevel != rightLevel) {
                    resultEnchantments.put(enchantment, Math.max(leftLevel, rightLevel));
                } else {
                    resultEnchantments.put(enchantment, leftLevel + 1);
                }
            } else {
                // If the enchantment is only on the left item, add it
                resultEnchantments.put(enchantment, leftLevel);
            }
        }

        // Add the right enchantments
        for (Map.Entry<Enchantment, Integer> entry : rightEnchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();

            // If the enchantment is only on the right item, add it
            if (!leftEnchantments.containsKey(enchantment)) {
                resultEnchantments.put(enchantment, level);
            }
        }

        return resultEnchantments;
    }

    // Determines whether combining the left enchantments with the right enchantments would be wasteful
    // This is to prevent combining two items with the same enchantments, but different levels, to create an item with
    // the same enchantments as one of the original items
    static boolean isWastefulCombine(@NotNull Map<Enchantment, Integer> leftEnchants, @NotNull Map<Enchantment, Integer> rightEnchants) {
        // If the left item has different enchantments than the right item, it is not wasteful
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

            // If the levels are the same and the enchantment is not at its
            // max level already, the combine is not wasteful
            if (leftLevel == rightLevel && leftLevel != MAX_ENCHANT_LEVELS.get(enchantment)) {
                return false;
            }

            // If both items have the higher level on at least one enchantment, the combine is not wasteful
            if (leftHasAtLeastOneHigherLevel && rightHasAtLeastOneHigherLevel) {
                return false;
            }
        }

        // If both items have the same enchantments, but one has the higher level on every enchantment,
        // the combine is wasteful
        return true;
    }

    // Calculates the total number of enchantment levels on an item
    // Used for anvil cost calculations
    static int totalLevels(@NotNull ItemStack item) {
        Map<Enchantment, Integer> enchantments = getAllEnchants(item);
        int totalLevels = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            totalLevels += entry.getValue();
        }

        return totalLevels;
    }

    // Determines if an item can be enchanted, indicated by the fact that it is either a book, tool, weapon, or armor
    static boolean isEnchantable(@NotNull ItemStack item) {
        // TODO: Exclude non-enchantable tools (are there any?)
        return isDamageable(item) || isBook(item);
    }

    // Determines if the given item is a book or enchanted book
    static boolean isBook(ItemStack item) {
        if (item == null) {
            return false;
        }

        Material type = item.getType();
        return type == Material.ENCHANTED_BOOK || type == Material.BOOK;
    }

    // Either returns the given enchantment level or the maximum level for the enchantment,
    // depending on whether the given level exceeds the maximum
    static int normalizeEnchantLevel(Enchantment enchantment, int level) {
        return Math.min(level, MAX_ENCHANT_LEVELS.get(enchantment));
    }

    // Determines whether a set of enchantments contains incompatible enchantments
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
