package net.glasslauncher.mods.alwaysmoreitems.gui;

import net.glasslauncher.mods.alwaysmoreitems.api.Rarity;
import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * This is a special class that renders AFTER the whole GUI has been rendered. This garuantees no weird item overlapping.
 * Note that the last mod to register an instance of this with the controller takes precedent if there's multiple tooltips with the same priority.
 * Extra note, you can render multiple tooltips, if absolutely required, by using a negative priority.
 */
public class TooltipInstance {
    public static final Color DEFAULT_FONT_COLOR = new Color(255, 255, 255, 255);
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 192);

    @Nullable
    protected String simpleTip;
    @Nullable
    protected ItemStack itemStack;
    protected int cursorX;
    protected int cursorY;
    protected Rarity rarity;
    protected int screenWidth;
    protected int screenHeight;
    @Nullable
    protected Screen screen;
    @Nullable
    protected HandledScreen containerScreen;

    private int priority = 0;

    protected List<String> tooltip;

    protected int cachedTooltipWidth = 0;

    public TooltipInstance(@NotNull String simpleTip, @NotNull ItemStack itemStack, int cursorX, int cursorY) {
        this.simpleTip = simpleTip;
        this.itemStack = itemStack;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        rarity = itemStack.getItem() instanceof RarityProvider rarityProvider ? rarityProvider.getRarity(itemStack) : Rarity.VANILLA;
        commonInit();
    }

    public TooltipInstance(@NotNull List<String> tooltip, int cursorX, int cursorY, Rarity rarity) {
        this.tooltip = tooltip;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.rarity = rarity;
        commonInit();
    }

    public TooltipInstance(@NotNull List<String> tooltip, int cursorX, int cursorY) {
        this.tooltip = tooltip;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.rarity = Rarity.VANILLA;
        commonInit();
    }

    public void commonInit() {
        screen = Minecraft.INSTANCE.currentScreen;
        if (screen == null) {
            containerScreen = null;
            screenWidth = screenHeight = 0;
            setupTooltip();
            onScreenIsNull();
            return;
        }
        screenWidth = screen.width;
        screenHeight = screen.height;
        if (screen instanceof HandledScreen handledScreen) {
            containerScreen = handledScreen;
        }
        else {
            containerScreen = null;
        }
        setupTooltip();
    }

    public void setupTooltip() {
        if (itemStack != null) {
            tooltip = TooltipHelper.getTooltipForItemStack(simpleTip, itemStack, Minecraft.INSTANCE.player.inventory, containerScreen);
        }
    }

    public void render() {
        GL11.glEnable(GL11.GL_BLEND);
        Bounds bounds = getBounds(false);
        renderBackground(bounds, false);
        renderHeader();

        int xStart = bounds.x1();
        int yStart = bounds.y1();

        int xPos = xStart + getTextXOffset(false);
        int yOffset = getPadding(TooltipEdge.BOTTOM) + AMITextRenderer.FONT_HEIGHT;

        for (int i = 0; i < tooltip.size() - 1; i++) {
            int yPos = yStart + (yOffset * i) + getPadding(TooltipEdge.TOP);
            AMITextRenderer.INSTANCE.drawWithShadow(tooltip.get(i + 1), xPos, yPos, DEFAULT_FONT_COLOR.getRGB());
        }
    }

    public void renderHeader() {
        String headerText = tooltip.get(0);
        if (headerText == null || headerText.isEmpty()) {
            throw new RuntimeException("Header text is null or empty somehow");
        }

        Bounds bounds = getBounds(true);
        renderBackground(bounds, true);

        int leftPadding = getTextXOffset(true);
        int topPadding = getPadding(TooltipEdge.LEFT);
        int xPos = bounds.x1() + leftPadding;
        int yPos = bounds.y1() + topPadding;
        AMITextRenderer.INSTANCE.drawWithShadow(headerText, xPos, yPos, rarity.textColor.getRGB());
    }

    public void renderBackground(Bounds bounds, boolean header) {
        AMIDrawContext.INSTANCE.fill(bounds.x1(), bounds.y1(), bounds.x2(), bounds.y2(), header ? rarity.backgroundColor.getRGB() : DEFAULT_BACKGROUND_COLOR.getRGB());
        if (header) {
            drawIcons(bounds);
        }
    }

    public void drawIcons(Bounds bounds) {
        boolean[][] icon = rarity.headerCode.icon;
        int templateWidth = icon[0].length;
        List<Integer> edgesToStretch = new ArrayList<>();

        int x1 = bounds.x1();
        int y1 = bounds.y1();
        int x2 = bounds.x2();

        for (int y = 0; y < icon.length; y++) {
            for (int x = 0; x < icon[y].length; x++) {
                if(icon[y][x]) {
                    AMIDrawContext.INSTANCE.fill(x1 + x, y1 + y, x1 + x + 1, y1 + y + 1, rarity.iconColor.getRGB());
                    if (rarity.headerCode.edgesStretchAcross && x == templateWidth - 1) {
                        edgesToStretch.add(y);
                    }
                }
                if(icon[y][templateWidth - 1 - x]) {
                    AMIDrawContext.INSTANCE.fill(x2 - templateWidth + x, y1 + y, x2 - templateWidth + x + 1, y1 + y + 1, rarity.iconColor.getRGB());
                }
            }
        }

        edgesToStretch.forEach(edge -> AMIDrawContext.INSTANCE.fill(x1 + templateWidth, y1 + edge, x2 - templateWidth, y1 + 1 + edge, rarity.iconColor.getRGB()));
    }

    public Bounds getBounds(boolean header) {
        BiTuple<Integer, Integer> offsets = getOffset(isFlipped());
        int yOffset = 0;
        if (!header) {
            yOffset += getHeight(true);
        }
        yOffset += Math.min(0,
                screenHeight - (
                                cursorY + offsets.two() + getHeight(false) + getHeight(true)
                )
        );
        return new Bounds(
                cursorX + offsets.one(),
                cursorY + offsets.two() + yOffset,
                cursorX + offsets.one() + getWidth(),
                cursorY + offsets.two() + yOffset + getHeight(header)
        );
    }

    public int getHeaderHeight() {
        return AMITextRenderer.FONT_HEIGHT + getPadding(rarity.equals(Rarity.VANILLA) ? TooltipEdge.HEADER_BOTTOM : TooltipEdge.HEADER_TOP_BOTTOM);
    }

    // To allow for people to not crash the game if they're intentionally running this outside the default location (on a screen.)
    public void onScreenIsNull() {
        throw new RuntimeException("Screen is null when it shouldn't be");
    }

    /**
     * This applies to each entry in the tooltip, so increasing the top and bottom padding will increase the gap between rows.
     */
    public int getPadding(TooltipEdge edge) {
        return edge.padding;
    }

    public int getTextXOffset(boolean header) {
        if(!header) {
            return getPadding(TooltipEdge.LEFT);
        }
        return getPadding(TooltipEdge.LEFT) + rarity.headerCode.icon[0].length;
    }

    public int getLines(boolean header) {
        int lines = 1;
        if (!header) {
            lines = tooltip.size() - 1;
        }
        return lines;
    }

    public int getHeight(boolean header) {
        int lines = getLines(header);
        if (header) {
            return getHeaderHeight();
        }
        return getPadding(TooltipEdge.TOP) + (lines * getPadding(TooltipEdge.BOTTOM)) + (lines * AMITextRenderer.FONT_HEIGHT);
    }

    public int getWidth() {
        if (cachedTooltipWidth != 0) {
            return cachedTooltipWidth;
        }

        OptionalInt potentialWidth = tooltip.stream().mapToInt(AMITextRenderer.INSTANCE::getWidth).max();
        if (potentialWidth.isEmpty()) {
            return 0;
        }

        int headerWidth = AMITextRenderer.INSTANCE.getWidth(tooltip.get(0)) + rarity.headerCode.icon[0].length;

        int maxWidth = Math.max(potentialWidth.getAsInt(), headerWidth);

        return cachedTooltipWidth = maxWidth + getPadding(TooltipEdge.LEFT_RIGHT);
    }

    public boolean isFlipped() {
        return getOffset(false).one() + cursorX + getWidth() > screenWidth;
    }

    public BiTuple<Integer, Integer> getOffset(boolean flipped) {
        if (flipped) {
            return BiTuple.of(-9 - getWidth(), -15);
        }
        return BiTuple.of(9, -15);
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable HandledScreen getContainerScreen() {
        return containerScreen;
    }

    public record Bounds(int x1, int y1, int x2, int y2) {}
}
