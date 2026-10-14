package com.example.enchantfilter.mixin;

import com.example.enchantfilter.EnchantFilterMatcher;
import com.example.enchantfilter.EnchantFilterState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin {
    @Shadow protected int leftPos;
    @Shadow protected int topPos;

    @Inject(method = "extractSlot", at = @At("HEAD"))
    private void enchantFilter$highlightSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (EnchantFilterState.SELECTED.isEmpty()) return;

        ItemStack stack = slot.getItem();
        if (EnchantFilterMatcher.matches(stack)) {
            int sx = slot.x;
            int sy = slot.y;
            graphics.fill(sx, sy, sx + 16, sy + 16, 0x8055FF55);
        }
    }
}
