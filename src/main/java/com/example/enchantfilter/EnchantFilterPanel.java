package com.example.enchantfilter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EnchantFilterPanel {
    private static final int ROW_HEIGHT = 14;
    private static final int PANEL_WIDTH = 150;
    private static final int PANEL_PADDING = 4;
    private static final int TOP_MARGIN = 20;
        private static final Map<String, Integer> POPULARITY = Map.ofEntries(
            Map.entry("sharpness", 0), Map.entry("fortune", 1), Map.entry("efficiency", 2),
            Map.entry("unbreaking", 3), Map.entry("mending", 4), Map.entry("protection", 5),
            Map.entry("looting", 6), Map.entry("feather_falling", 7), Map.entry("power", 8),
            Map.entry("silk_touch", 9), Map.entry("fire_aspect", 10), Map.entry("thorns", 11),
            Map.entry("respiration", 12), Map.entry("aqua_affinity", 13), Map.entry("depth_strider", 14),
            Map.entry("sweeping_edge", 15), Map.entry("projectile_protection", 16),
            Map.entry("fire_protection", 17), Map.entry("blast_protection", 18), Map.entry("infinity", 19)
        );

    private static final List<Holder<Enchantment>> allEnchants = new ArrayList<>();
    private static int scroll = 0;
    private static int panelX = 6;
        private static int panelY = TOP_MARGIN;
        private static boolean dragging = false;
    private static int rowLimit = 12;

    public static void refresh() {
        allEnchants.clear();
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.level == null || client.screen == null) return;

        client.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .listElements()
            .sorted(Comparator.comparingInt((Holder<Enchantment> holder) ->
                    POPULARITY.getOrDefault(holder.unwrapKey().orElseThrow().identifier().getPath(), 1000))
                .thenComparing(holder -> holder.unwrapKey().orElseThrow().identifier().getPath()))
                .forEach(allEnchants::add);

        int rows = visibleRows(client.screen);
        scroll = Math.min(scroll, Math.max(0, allEnchants.size() - rows));
    }

    private static int panelX(Screen screen) {
        return panelX;
    }

    private static int visibleRows(Screen screen) {
        if (screen == null) return 1;
        int available = screen.height - panelY - 40;
        return Math.max(1, Math.min(rowLimit, available / ROW_HEIGHT));
    }

    private static int listTop() {
        return panelY + 12 + ROW_HEIGHT * 2 + 2;
    }

    public static void render(GuiGraphicsExtractor ctx, Screen screen, int mouseX, int mouseY) {
        if (screen == null || allEnchants.isEmpty()) refresh();
        if (screen == null) return;

        Minecraft client = Minecraft.getInstance();
        int x = panelX(screen);
        int y = panelY;
        int rows = visibleRows(screen);
        int panelHeight = rows * ROW_HEIGHT + 46;

        ctx.fill(x - PANEL_PADDING, y - PANEL_PADDING,
                x + PANEL_WIDTH + PANEL_PADDING, y + panelHeight, 0xCC101010);
        ctx.text(client.font, Component.literal("Enchant Filter"), x, y, 0xFFFFFFFF);
        y += 12;

        String modeLabel = EnchantFilterState.matchAll ? "Mode: MATCH ALL" : "Mode: MATCH ANY";
        boolean modeHover = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + ROW_HEIGHT;
        ctx.fill(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, modeHover ? 0x66FFFFFF : 0x33FFFFFF);
        ctx.text(client.font, Component.literal(modeLabel), x + 3, y + 3, 0xFFFFFF55);
        y += ROW_HEIGHT + 2;

        boolean exactHover = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY <= y + ROW_HEIGHT;
        ctx.fill(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, exactHover ? 0x66FFFFFF : 0x33FFFFFF);
        ctx.text(client.font, Component.literal("Exact: " + (EnchantFilterState.exactMatch ? "ON" : "OFF")),
                x + 3, y + 3, 0xFFFFFF55);

        int listTop = listTop();

        for (int i = 0; i < rows; i++) {
            int index = scroll + i;
            if (index >= allEnchants.size()) break;

            Holder<Enchantment> holder = allEnchants.get(index);
            ResourceKey<Enchantment> key = holder.unwrapKey().orElseThrow();
            int maxLevel = holder.value().getMaxLevel();
            int level = Math.min(maxLevel, Math.max(1, EnchantFilterState.selectedLevel(key)));
            int rowY = listTop + i * ROW_HEIGHT;
            boolean selected = EnchantFilterState.selectedLevel(key) == level;
            boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= rowY && mouseY <= rowY + ROW_HEIGHT;

            int bg = selected ? 0xFF2E7D32 : (hovered ? 0x33FFFFFF : 0x00000000);
            if (bg != 0) ctx.fill(x, rowY, x + PANEL_WIDTH, rowY + ROW_HEIGHT, bg);

            String label = (selected ? "[x] " : "[ ] ") + holder.value().description().getString() + " " + level;
            ctx.text(client.font, Component.literal(label), x + 3, rowY + 3, 0xFFFFFFFF);
        }
    }

    public static boolean handleClick(Screen screen, double mouseX, double mouseY, int button) {
        if (allEnchants.isEmpty()) refresh();
        int x = panelX(screen);
        int y = panelY;
        int rows = visibleRows(screen);

        if (mouseX < x - PANEL_PADDING || mouseX > x + PANEL_WIDTH + PANEL_PADDING) return false;

        if (button == 0 && mouseY >= y && mouseY <= y + 12) {
            dragging = true;
            return true;
        }

        int modeTop = y + 12;
        int modeBottom = modeTop + ROW_HEIGHT;
        if (mouseY >= modeTop && mouseY <= modeBottom) {
            EnchantFilterState.matchAll = !EnchantFilterState.matchAll;
            return true;
        }

        int exactTop = modeBottom + 2;
        int exactBottom = exactTop + ROW_HEIGHT;
        if (mouseY >= exactTop && mouseY <= exactBottom) {
            EnchantFilterState.exactMatch = !EnchantFilterState.exactMatch;
            return true;
        }

        int listTop = exactBottom + 2;
        if (mouseY < listTop) return true;

        int row = (int) ((mouseY - listTop) / ROW_HEIGHT);
        int index = scroll + row;
        if (row >= 0 && row < rows && index < allEnchants.size()) {
            ResourceKey<Enchantment> key = allEnchants.get(index).unwrapKey().orElseThrow();
            int maxLevel = allEnchants.get(index).value().getMaxLevel();
            if (button == 1) {
                EnchantFilterState.clear(key);
            } else {
                int selectedLevel = EnchantFilterState.selectedLevel(key);
                int nextLevel = selectedLevel == 0 || selectedLevel >= maxLevel ? 1 : selectedLevel + 1;
                EnchantFilterState.setLevel(key, nextLevel);
            }
        }
        return true;
    }

    public static boolean handleDrag(Screen screen, MouseButtonEvent event, double deltaX, double deltaY) {
        if (!dragging || event.button() != 0) return false;
        panelX = Math.max(4, Math.min(screen.width - PANEL_WIDTH - 4, panelX + (int) deltaX));
        panelY = Math.max(4, Math.min(screen.height - 50, panelY + (int) deltaY));
        return true;
    }

    public static boolean handleRelease(MouseButtonEvent event) {
        if (dragging && event.button() == 0) {
            dragging = false;
            return true;
        }
        return false;
    }

    public static boolean handleScroll(Screen screen, double mouseX, double mouseY, double verticalAmount) {
        int x = panelX(screen);
        if (mouseX < x - PANEL_PADDING || mouseX > x + PANEL_WIDTH + PANEL_PADDING) return false;

        if (isControlDown()) {
            rowLimit += verticalAmount > 0 ? 1 : -1;
            rowLimit = Math.max(4, Math.min(30, rowLimit));
            int rows = visibleRows(screen);
            int max = Math.max(0, allEnchants.size() - rows);
            scroll = Math.min(scroll, max);
            panelY = Math.min(panelY, Math.max(4, screen.height - rows * ROW_HEIGHT - 50));
            return true;
        }

        scroll -= (int) Math.signum(verticalAmount);
        int rows = visibleRows(screen);
        int max = Math.max(0, allEnchants.size() - rows);
        if (scroll < 0) scroll = 0;
        if (scroll > max) scroll = max;
        return true;
    }

    private static boolean isControlDown() {
        long window = Minecraft.getInstance().getWindow().handle();
        return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }

}
