package net.glasslauncher.mods.alwaysmoreitems.gui;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.alwaysmoreitems.api.AMITooltipModifier;
import net.glasslauncher.mods.alwaysmoreitems.api.ItemRarityProvider;
import net.glasslauncher.mods.alwaysmoreitems.api.Rarity;
import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.ItemStackRenderer;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.ImageUtil;
import net.glasslauncher.mods.alwaysmoreitems.util.MethodFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a special class that renders AFTER the whole GUI has been rendered. This garuantees no weird overlapping.
 * Note that the last mod to set the instance is the one that controls the tooltip.
 * TODO: Add a formatting system, AMI 1.0 has taken long enough to release. The way I plan do to it will be backwards compatible in the API, so I'm not worried - calm
 */
public class Tooltip {
    public static final Tooltip INSTANCE = new Tooltip();

    public static final Color DEFAULT_FONT_COLOR = new Color(255, 255, 255, 255);
    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 192);
    public static final Dimension DEFAULT_OFFSET = new Dimension(12, -12);
    private static final ItemStackRenderer ITEM_STACK_RENDERER = new ItemStackRenderer();

    /**
     * The original "vanilla" tooltip. Typically, the item name.
     */
    @Nullable
    protected String simpleTip;
    /**
     * The itemstack that this tooltip is rendering for. This is not guaranteed to exist, due to the ability to draw tooltips in any context.
     */
    @Nullable
    protected ItemStack itemStack;
    /**
     * The mouse cursor X in minecraft GUI pixels.
     */
    protected int cursorX;
    /**
     * The mouse cursor Y in minecraft GUI pixels.
     */
    protected int cursorY;
    /**
     * The provided rarity to draw the tooltip with.
     */
    protected Rarity rarity;
    /**
     * The screen width in minecraft GUI pixels.
     */
    protected int screenWidth;
    /**
     * The screen height in minecraft GUI pixels.
     */
    protected int screenHeight;
    @Nullable
    protected Screen screen;
    @Nullable
    protected HandledScreen containerScreen;

    protected List<Object> tooltip;

    protected int cachedTooltipWidth = 0;

    /**
     * You should only instantiate one of these for whatever functionality you need it for.
     * If you're using a real tooltip, use INSTANCE instead, it is already managed for you, and all you need to do is set the tooltip.
     */
    public Tooltip() {}

    public void setTooltip(@NotNull ItemStack itemStack, int cursorX, int cursorY) {
        this.simpleTip = null;
        this.itemStack = itemStack;
        this.tooltip = null;
        this.cursorX = cursorX;
        this.cursorY = cursorY;

        Method foundMethod = MethodFinder.findMethodWithAnnotation(itemStack.getItem().getClass(), ItemRarityProvider.class);
        if (foundMethod != null) {
            try {
                rarity = (Rarity) foundMethod.invoke(itemStack.getItem(), itemStack);
            } catch (IllegalAccessException e) {
                AlwaysMoreItems.LOGGER.error("Potentially private method for {}", itemStack.getItem().getClass().getName(), e);
            } catch (InvocationTargetException e) {
                AlwaysMoreItems.LOGGER.error("Potentially malformed method for {}", itemStack.getItem().getClass().getName(), e);
            }
        }

        if (this.rarity == null) { // Fallback to legacy system
            //noinspection removal
            this.rarity = (itemStack.getItem() instanceof RarityProvider rarityProvider) ? rarityProvider.getRarity(itemStack) : Rarity.VANILLA;
        }
        commonInit();
    }

    public void setTooltip(@NotNull List<Object> tooltip, int cursorX, int cursorY, Rarity rarity) {
        this.simpleTip = null;
        this.itemStack = null;
        this.tooltip = tooltip;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.rarity = rarity;
        commonInit();
    }

    public void setTooltip(@NotNull List<Object> tooltip, int cursorX, int cursorY) {
        this.simpleTip = null;
        this.itemStack = null;
        this.tooltip = new ArrayList<>(tooltip);
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.rarity = Rarity.VANILLA;
        if (AMIConfig.showRarities() && !tooltip.isEmpty() && tooltip.get(0) instanceof String tooltipString && tooltipString.startsWith(String.valueOf(Rarity.HeaderCode.PREFIX_CHARACTER))) {
            rarity = Rarity.AMI_RARITIES_BY_CODE.get(tooltipString.charAt(1));
        }
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
            simpleTip = TranslationStorage.getInstance().get(itemStack.getTranslationKey() + ".name");
            tooltip = new ArrayList<>(TooltipHelper.getTooltipForItemStack(simpleTip, itemStack, Minecraft.INSTANCE.player.inventory, containerScreen));

            Method foundMethod = MethodFinder.findMethodWithAnnotation(itemStack.getItem().getClass(), AMITooltipModifier.class);
            if (foundMethod != null) {
                try {
                    foundMethod.invoke(itemStack.getItem(), itemStack, tooltip);
                } catch (IllegalAccessException e) {
                    AlwaysMoreItems.LOGGER.error("Potentially private method for {}", itemStack.getItem().getClass().getName(), e);
                } catch (InvocationTargetException e) {
                    AlwaysMoreItems.LOGGER.error("Potentially malformed method for {}", itemStack.getItem().getClass().getName(), e);
                }
            }

            if (AMIConfig.isDebugModeEnabled()) {
                String extras = "";
                if (itemStack.getDamage() != 0) {
                    extras += ":" + itemStack.getDamage();
                }
                tooltip.set(0, tooltip.get(0) + " " + itemStack.itemId + extras);

                tooltip.add(1, Formatting.GRAY + AMITextRenderer.ITALICS + ItemRegistry.INSTANCE.getId(itemStack.getItem()));

                List<TagKey<Item>> tags = itemStack.getItem().getRegistryEntry().streamTags().toList();
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !tags.isEmpty()) {
                    tooltip.add(Tooltip.Divider.INSTANCE);

                    for (TagKey<Item> tag : tags) {
                        tooltip.add(Formatting.GRAY + AMITextRenderer.ITALICS + tag.id());
                    }

                    tooltip.add(Tooltip.Divider.INSTANCE);
                }
                else if (!tags.isEmpty()) {
                    tooltip.add(Formatting.DARK_GRAY + AMITextRenderer.ITALICS + "Hold CTRL to see " + tags.size() + (tags.size() == 1 ? " tag..." : " tags..."));
                }
            }

            if (AMIConfig.showNbtCount()) {
                tooltip.add(Formatting.GRAY + AMITextRenderer.ITALICS + "NBT Count: " + itemStack.getStationNbt().values().size());
            }
            if (AMIConfig.showModNames()) {
                tooltip.add(Formatting.BLUE + AMITextRenderer.ITALICS + AlwaysMoreItems.getItemRegistry().getModNameForItem(itemStack.getItem()));
            }
        }
    }

    public void clear() {
        tooltip = null;
        rarity = null;
        cachedTooltipWidth = 0;
    }

    public void render() {
        if (isEmpty()) {
            return;
        }
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
        if (line instanceof ItemStack) {
            return 16;
        }
        if (line instanceof Element<?> element) {
            return element.getHeight();
        }

        return 0;
    }

    public int getLineWidth(Object line) {
        if (line instanceof String string) {
            return AMITextRenderer.INSTANCE.getWidth(string);
        }
        if (line instanceof ItemStack) {
            return 16;
        }
        if (line instanceof Element<?> element) {
            return element.getWidth();
        }

        return 0;
    }

    public void renderLine(Object line, int x, int y, Color color) {
        if (line instanceof String string) {
            AMITextRenderer.INSTANCE.drawWithShadow(string, x, y, color.getRGB());
        }
        else if (line instanceof ItemStack lineStack) {
            RenderHelper.enableItemLighting();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            ITEM_STACK_RENDERER.draw(Minecraft.INSTANCE, x, y, lineStack);
            RenderHelper.disableItemLighting();
        }
        else if (line instanceof Element<?> element) {
            element.render(x, y, getMaxLineWidth(), getLineHeight(line));
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
            return cachedTooltipWidth + getPadding(TooltipEdge.LEFT_RIGHT);
        }

        int maxWidth = getMaxLineWidth();

        return maxWidth + getPadding(TooltipEdge.LEFT_RIGHT);
    }

    public int getMaxLineWidth() {
        if (cachedTooltipWidth != 0) {
            return cachedTooltipWidth;
        }

        OptionalInt potentialWidth = tooltip.stream().mapToInt(this::getLineWidth).max();
        if (potentialWidth.isEmpty()) {
            return 0;
        }

        int headerWidth = AMITextRenderer.INSTANCE.getWidth((String) tooltip.get(0)) + rarity.headerCode.icon[0].length;

        return cachedTooltipWidth = Math.max(potentialWidth.getAsInt(), headerWidth);
    }

    public boolean isFlipped() {
        return getOffset(false).width + cursorX + getWidth() > screenWidth;
    }

    public Dimension getOffset(boolean flipped) {
        if (flipped) {
            return new Dimension(-12 - getWidth(), -12);
        }
        return DEFAULT_OFFSET;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable HandledScreen getContainerScreen() {
        return containerScreen;
    }

    public boolean isEmpty() {
        return tooltip == null || tooltip.isEmpty();
    }

    public static class Element<T> {
        @Getter
        private final T content;
        @Getter
        private final int width;
        @Getter
        private final int height;
        private final Renderer<T> renderer;
        @Getter
        private final Alignment alignment;
        @Getter @Setter
        private Color color = Color.WHITE;

        public Element(T content, int width, int height, Renderer<T> renderer) {
            this.content = content;
            this.width = width;
            this.height = height;
            this.alignment = Alignment.TOP_LEFT;
            this.renderer = renderer;
        }

        public Element(T content, int width, int height, Alignment alignment, Renderer<T> renderer) {
            this.content = content;
            this.width = width;
            this.height = height;
            this.alignment = alignment;
            this.renderer = renderer;
        }

        public void render(int x, int y, int maxLineWidth, int lineHeight) {
            OffsetDimension dimension = computeAlignment(maxLineWidth, lineHeight);
            renderer.render(x + dimension.x, y + dimension.y, maxLineWidth, lineHeight, this);
        }

        public OffsetDimension computeAlignment(int maxLineWidth, int lineHeight) {
            return alignment.compute(width, height, maxLineWidth, lineHeight);
        }
    }

    private static final OffsetDimension ZERO_DIMENSION = new OffsetDimension(0, 0);

    @FunctionalInterface
    public interface Alignment {
        Alignment TOP_LEFT = ((width, height, maxLineWidth, lineHeight) -> ZERO_DIMENSION);
        Alignment TOP_RIGHT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(maxLineWidth - width, 0));
        Alignment TOP_MIDDLE = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension((maxLineWidth / 2) - (width / 2), 0));
        Alignment BOTTOM_LEFT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(0, lineHeight - height));
        Alignment BOTTOM_RIGHT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(maxLineWidth - width, lineHeight - height));
        Alignment BOTTOM_MIDDLE = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension((maxLineWidth / 2) - (width / 2), lineHeight - height));
        Alignment CENTER = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension((maxLineWidth / 2) - (width / 2), (lineHeight / 2) - (height / 2)));

        OffsetDimension compute(int width, int height, int maxLineWidth, int lineHeight);
    }

    @FunctionalInterface
    public interface Renderer<T> {
        void render(int x, int y, int maxLineWidth, int lineHeight, Element<T> element);
    }

    public record Bounds(int x1, int y1, int x2, int y2) {}

    public record Dimension(int width, int height) {}
    public record OffsetDimension(int x, int y) {} // Purely here for readability's sake

    /**
     * Used for caching information about images. You should only instantiate this class once, ideally, though this is backed by a cache.
     * NOTE: There seems to be a limit of 32x32 due to limitations, but frankly, if you need an image larger than this, you shouldn't be putting it in a tooltip.
     */
    public static class Image extends Element<String> {
        public static final Renderer<String> IMAGE_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {
            Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId(element.content));
            GL11.glColor3d(element.color.getRed() / 255d, element.color.getBlue() / 255d, element.color.getGreen() / 255d);
            OffsetDimension dimension = element.computeAlignment(maxLineWidth, lineHeight);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            AMIDrawContext.INSTANCE.drawTexture(x + dimension.x, y + dimension.y, element.width, element.height, element.width, element.height, 0, 0);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4d(1, 1, 1, 1);
        };

        public Image(String image) {
            super(image, getDimension(image).width, getDimension(image).height, IMAGE_RENDERER);
        }

        public Image(String image, Alignment alignment) {
            super(image, getDimension(image).width, getDimension(image).height, alignment, IMAGE_RENDERER);
        }

        public Image(String image, Alignment alignment, Color color) {
            super(image, getDimension(image).width, getDimension(image).height, alignment, IMAGE_RENDERER);
            setColor(color);
        }

        private static Dimension getDimension(String image) {
            try {
                return ImageUtil.getImageDimension(Paths.get(Image.class.getResource(image).toURI()).toFile());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Text extends Element<String> {
        public static final Renderer<String> TEXT_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {
            AMITextRenderer.INSTANCE.renderStringAtPos(element.content, x, y, element.getColor(), true);
        };

        public Text(String content) {
            super(content, AMITextRenderer.INSTANCE.getWidth(content), AMITextRenderer.FONT_HEIGHT, TEXT_RENDERER);
        }

        public Text(String content, Alignment alignment) {
            super(content, AMITextRenderer.INSTANCE.getWidth(content), AMITextRenderer.FONT_HEIGHT, alignment, TEXT_RENDERER);
        }

        public Text(String content, Alignment alignment, Color color) {
            super(content, AMITextRenderer.INSTANCE.getWidth(content), AMITextRenderer.FONT_HEIGHT, alignment, TEXT_RENDERER);
            this.setColor(color);
        }

        public Text(String content, Renderer<String> renderer) {
            super(content, AMITextRenderer.INSTANCE.getWidth(content), AMITextRenderer.FONT_HEIGHT, renderer);
        }

        public Text(String content, Alignment alignment, Renderer<String> renderer) {
            super(content, AMITextRenderer.INSTANCE.getWidth(content), AMITextRenderer.FONT_HEIGHT, alignment, renderer);
        }

        public Text(String content, Alignment alignment, Renderer<String> renderer, Color color) {
            super(content, AMITextRenderer.INSTANCE.getWidth(content), AMITextRenderer.FONT_HEIGHT, alignment, renderer);
            this.setColor(color);
        }
    }

    /**
     * When inserted into a tooltip, a horizonal line is rendered.
     * Don't instantiate, use the instance.
     */
    public static class Divider extends Element<Object> {
        public static final Renderer<Object> DIVIDER_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {
            AMIDrawContext.INSTANCE.fill(x, y, maxLineWidth, element.height, element.color.getRGB());
        };

        public static final Divider INSTANCE = new Divider(null, 1, 1);

        private Divider(Object content, int width, int height) {
            super(content, width, height, DIVIDER_RENDERER);
        }
    }

    /**
     * When inserted into a tooltip, a vertical line is rendered.
     * Don't instantiate, use the instance.
     */
    public static class VerticalDivider extends Element<Object> {
        public static final Renderer<Object> VERTICAL_DIVIDER_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {
            AMIDrawContext.INSTANCE.fill(x + (int) Math.floor(element.width / 2d), y, x + (int) Math.floor(element.width / 2d) + 1, y + lineHeight, element.color.getRGB());
        };

        public static final VerticalDivider INSTANCE = new VerticalDivider(null, 3, 1);

        private VerticalDivider(Object content, int width, int height) {
            super(content, width, height, VERTICAL_DIVIDER_RENDERER);
        }
    }

    /**
     * When inserted into a tooltip, a vertical line is rendered.
     * Don't instantiate, use the instance.
     */
    public static class VerticalSpace extends Element<Object> {
        public static final Renderer<Object> DIVIDER_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {};

        public static final VerticalSpace INSTANCE = new VerticalSpace(null, 3, 1);

        private VerticalSpace(Object content, int width, int height) {
            super(content, width, height, DIVIDER_RENDERER);
        }
    }

    /**
     * Contains a collection of elements, which are rendered on the same line.
     */
    public static class Line extends Element<Element<?>[]> {
        public static final Renderer<Element<?>[]> LINE_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {
            int width = 0;
            for (Element<?> child : element.content) {
                child.render(x + width, y, maxLineWidth, lineHeight);
                width += child.width;
            }
        };

        private Line(Element<?>[] content, int width, int height) {
            super(content, width, height, LINE_RENDERER);
        }

        public static Line create(Element<?>... content) {
            int width = 0;
            int height = 0;
            for (Element<?> child : content) {
                width += child.width;
                if (child.height > height) {
                    height = child.height;
                }
            }

            return new Line(content, width, height);
        }

    }
}
