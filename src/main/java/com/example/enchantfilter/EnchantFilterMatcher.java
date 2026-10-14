package com.example.enchantfilter;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import java.util.Map;

public class EnchantFilterMatcher {
    public static boolean matches(ItemStack stack) {
        if (EnchantFilterState.SELECTED.isEmpty() || stack.isEmpty()) return false;

        ItemEnchantments component = stack.getOrDefault(
                DataComponents.ENCHANTMENTS,
                ItemEnchantments.EMPTY
        );
        if (component.isEmpty()) return false;

        Map<ResourceKey<Enchantment>, Integer> found = new java.util.HashMap<>();
        component.entrySet().forEach(entry -> entry.getKey().unwrapKey()
                .ifPresent(key -> found.put(key, entry.getIntValue())));

        if (EnchantFilterState.exactMatch && found.size() != EnchantFilterState.SELECTED_LEVELS.size()) {
            return false;
        }

        if (EnchantFilterState.exactMatch) {
            return EnchantFilterState.SELECTED_LEVELS.entrySet().stream()
                    .allMatch(selected -> selected.getValue().equals(found.get(selected.getKey())));
        }

        boolean matchAll = EnchantFilterState.matchAll;
        for (Map.Entry<ResourceKey<Enchantment>, Integer> selected : EnchantFilterState.SELECTED_LEVELS.entrySet()) {
            boolean has = selected.getValue().equals(found.get(selected.getKey()));
            if (matchAll && !has) return false;
            if (!matchAll && has) return true;
        }
        return matchAll;
    }
}
