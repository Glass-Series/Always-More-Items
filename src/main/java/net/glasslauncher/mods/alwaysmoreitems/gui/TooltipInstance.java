package net.glasslauncher.mods.alwaysmoreitems.gui;

import net.glasslauncher.mods.alwaysmoreitems.api.Rarity;
import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.ItemStackRenderer;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.ImageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.file.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * This is a special class that renders AFTER the whole GUI has been rendered. This garuantees no weird item overlapping.
 * Note that the last mod to register an instance of this with the controller takes precedent if there's multiple tooltips with the same priority.
 * Extra note, you can render multiple tooltips, if absolutely required, by using a negative priority.
 */
public class TooltipInstance {
    public static final Color DEFAULT_FONT_COLOR = new Color(255, 255, 255, 255);
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 192);
    public static final Dimension DEFAULT_OFFSET = new Dimension(9, -15);
    private static final ItemStackRenderer ITEM_STACK_RENDERER = new ItemStackRenderer();

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

    protected List<Object> tooltip;

    protected int cachedTooltipWidth = 0;

    public TooltipInstance(@NotNull String simpleTip, @NotNull ItemStack itemStack, int cursorX, int cursorY) {
        this.simpleTip = simpleTip;
        this.itemStack = itemStack;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        rarity = itemStack.getItem() instanceof RarityProvider rarityProvider ? rarityProvider.getRarity(itemStack) : Rarity.VANILLA;
        commonInit();
    }

    public TooltipInstance(@NotNull List<Object> tooltip, int cursorX, int cursorY, Rarity rarity) {
        this.tooltip = tooltip;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.rarity = rarity;
        commonInit();
    }

    public TooltipInstance(@NotNull List<Object> tooltip, int cursorX, int cursorY) {
        this.tooltip = new ArrayList<>(tooltip);
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
            tooltip = new ArrayList<>(TooltipHelper.getTooltipForItemStack(simpleTip, itemStack, Minecraft.INSTANCE.player.inventory, containerScreen));
            tooltip.add(Divider.INSTANCE);
            tooltip.add(itemStack);
            tooltip.add(Divider.INSTANCE);
            tooltip.add(new Image("/assets/alwaysmoreitems/icon.png"));
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
        int spacing = getPadding(TooltipEdge.SPACING);

        int yPos = yStart + getPadding(TooltipEdge.TOP);

        for (int i = 0; i < tooltip.size() - 1; i++) {
            Object line = tooltip.get(i + 1);
            renderLine(line, xPos, yPos, DEFAULT_FONT_COLOR);
            yPos += getLineHeight(line) + spacing;
        }
    }

    public int getLineHeight(Object line) {
        if (line instanceof String) {
            return AMITextRenderer.FONT_HEIGHT;
        }
        if (line instanceof Divider) {
            return 1;
        }
        if (line instanceof ItemStack) {
            return 16;
        }
        if (line instanceof Image image) {
            return image.height;
        }

        return 0;
    }

    public int getLineWidth(Object line) {
        if (line instanceof String string) {
            return AMITextRenderer.INSTANCE.getWidth(string);
        }
        if (line instanceof Divider) {
            return 1;
        }
        if (line instanceof ItemStack) {
            return 16;
        }
        if (line instanceof Image image) {
            try {
                return image.width;
            }
            catch (Exception e) {
                AlwaysMoreItems.LOGGER.error(e);
                return 0;
            }
        }

        return 0;
    }

    public void renderLine(Object line, int x, int y, Color color) {
        if (line instanceof String string) {
            AMITextRenderer.INSTANCE.drawWithShadow(string, x, y, color.getRGB());
        }
        else if (line instanceof Divider) {
            AMIDrawContext.INSTANCE.fill(x, y, x + getMaxLineWidth(), y + 1, color.getRGB());
        }
        else if (line instanceof ItemStack lineStack) {
            RenderHelper.enableItemLighting();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            ITEM_STACK_RENDERER.draw(Minecraft.INSTANCE, x, y, lineStack);
            RenderHelper.disableItemLighting();
        }
        else if (line instanceof Image image) {
            RenderHelper.bindTexture(image.image);
            AMIDrawContext.INSTANCE.drawTexture(x, y, 1, 1, image.width, image.height);
        }
    }

    public void renderHeader() {
        String headerText = (String) tooltip.get(0); // Header should always be a string.
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
        Dimension offsets = getOffset(isFlipped());
        int yOffset = 0;
        if (!header) {
            yOffset += getHeight(true);
        }
        yOffset += Math.min(0,
                screenHeight - (
                                cursorY + offsets.height + getHeight(false) + getHeight(true)
                )
        );
        yOffset += Math.max(0, -(cursorY + offsets.height)); // But it could also be too high up, so account for that too.
        return new Bounds(
                cursorX + offsets.width,
                cursorY + offsets.height + yOffset,
                cursorX + offsets.width + getWidth(),
                cursorY + offsets.height + yOffset + getHeight(header)
        );
    }

    public int getHeaderHeight() {
        return AMITextRenderer.FONT_HEIGHT + getPadding(rarity.equals(Rarity.VANILLA) ? TooltipEdge.HEADER_VANILLA : TooltipEdge.HEADER_WITH_RARITY);
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
        ArrayList<Object> headerlessTip = new ArrayList<>(tooltip);
        headerlessTip.remove(0);
        AtomicInteger atomicInteger = new AtomicInteger();
        headerlessTip.forEach(entry -> atomicInteger.addAndGet(getLineHeight(entry)));
        return getPadding(TooltipEdge.TOP) + (lines * getPadding(TooltipEdge.BOTTOM)) + atomicInteger.get();
    }

    public int getWidth() {
        if (cachedTooltipWidth != 0) {
            return cachedTooltipWidth;
        }

        int maxWidth = getMaxLineWidth();

        return cachedTooltipWidth = maxWidth + getPadding(TooltipEdge.LEFT_RIGHT);
    }

    public int getMaxLineWidth() {
        OptionalInt potentialWidth = tooltip.stream().mapToInt(this::getLineWidth).max();
        if (potentialWidth.isEmpty()) {
            return 0;
        }

        int headerWidth = AMITextRenderer.INSTANCE.getWidth((String) tooltip.get(0)) + rarity.headerCode.icon[0].length;

        return Math.max(potentialWidth.getAsInt(), headerWidth);
    }

    public boolean isFlipped() {
        return getOffset(false).width + cursorX + getWidth() > screenWidth;
    }

    public Dimension getOffset(boolean flipped) {
        if (flipped) {
            return new Dimension(-9 - getWidth(), -15);
        }
        return DEFAULT_OFFSET;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable HandledScreen getContainerScreen() {
        return containerScreen;
    }

    public record Bounds(int x1, int y1, int x2, int y2) {}

    public record Dimension(int width, int height) {}

    public static class Image {
        public final String image;
        public final int width;
        public final int height;

        public Image(String image) {
            this.image = image;
            try {
                Dimension dimension = ImageUtil.getImageDimension(Paths.get(this.getClass().getResource(image).toURI()).toFile());
                width = dimension.width;
                height = dimension.height;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Divider {
        @SuppressWarnings("InstantiationOfUtilityClass")
        public static final Divider INSTANCE = new Divider();
        private Divider() {}
    }
}
