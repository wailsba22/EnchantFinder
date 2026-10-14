package com.example.enchantfilter;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class EnchantFilterClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("enchantfilter", "general")
    );

    public static final KeyMapping TOGGLE_PANEL_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.enchantfilter.toggle_panel",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_PANEL_KEY.consumeClick()) {
                EnchantFilterState.panelOpen = !EnchantFilterState.panelOpen;
                if (EnchantFilterState.panelOpen) {
                    EnchantFilterPanel.refresh();
                }
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof AbstractContainerScreen<?>)) return;

            ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, tickProgress) -> {
                if (EnchantFilterState.panelOpen) {
                    EnchantFilterPanel.render(graphics, screen, mouseX, mouseY);
                }
            });

            ScreenMouseEvents.allowMouseClick(screen).register((s, event) ->
                    !(EnchantFilterState.panelOpen && EnchantFilterPanel.handleClick(s, event.x(), event.y(), event.button())));

                ScreenMouseEvents.allowMouseDrag(screen).register((s, event, deltaX, deltaY) ->
                    !(EnchantFilterState.panelOpen && EnchantFilterPanel.handleDrag(s, event, deltaX, deltaY)));

                ScreenMouseEvents.allowMouseRelease(screen).register((s, event) ->
                    !(EnchantFilterState.panelOpen && EnchantFilterPanel.handleRelease(event)));

            ScreenMouseEvents.allowMouseScroll(screen).register((s, mouseX, mouseY, horizontalAmount, verticalAmount) ->
                    !(EnchantFilterState.panelOpen && EnchantFilterPanel.handleScroll(s, mouseX, mouseY, verticalAmount)));
        });
    }
}
