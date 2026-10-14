package com.example.enchantfilter;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EnchantFilterState {
    public static final Set<ResourceKey<Enchantment>> SELECTED = new LinkedHashSet<>();
    public static final Map<ResourceKey<Enchantment>, Integer> SELECTED_LEVELS = new LinkedHashMap<>();
    public static boolean matchAll = true;
    public static boolean exactMatch = false;
    public static boolean panelOpen = false;

    public static void toggle(ResourceKey<Enchantment> key, int level) {
        Integer selectedLevel = SELECTED_LEVELS.get(key);
        if (selectedLevel != null && selectedLevel == level) {
            SELECTED_LEVELS.remove(key);
            SELECTED.remove(key);
        } else {
            SELECTED_LEVELS.put(key, level);
            SELECTED.add(key);
        }
    }

    public static void setLevel(ResourceKey<Enchantment> key, int level) {
        SELECTED_LEVELS.put(key, level);
        SELECTED.add(key);
    }

    public static void clear(ResourceKey<Enchantment> key) {
        SELECTED_LEVELS.remove(key);
        SELECTED.remove(key);
    }

    public static int selectedLevel(ResourceKey<Enchantment> key) {
        return SELECTED_LEVELS.getOrDefault(key, 0);
    }

    public static boolean isSelected(ResourceKey<Enchantment> key) {
        return SELECTED_LEVELS.containsKey(key);
    }
}
