package net.glasslauncher.alwaysmoreitems;

import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableAnimated;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableStatic;
import net.glasslauncher.alwaysmoreitems.api.gui.ITickTimer;
import net.glasslauncher.alwaysmoreitems.gui.widget.DrawableAnimated;
import net.glasslauncher.alwaysmoreitems.gui.widget.DrawableBlank;
import net.glasslauncher.alwaysmoreitems.gui.widget.DrawableResource;
import net.glasslauncher.alwaysmoreitems.util.TickTimer;
import net.minecraft.client.Minecraft;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import javax.annotation.*;
import java.util.*;
import java.util.stream.*;

public class DrawableHelper {

    @Nonnull
    public static IDrawableAnimated createAnimatedDrawable(@Nullable IDrawableStatic drawable, int ticksPerCycle, @Nullable IDrawableAnimated.StartDirection startDirection, boolean inverted) {
        if (drawable == null) {
            AlwaysMoreItems.LOGGER.error("Null drawable, returning blank drawable", new NullPointerException());
            return new DrawableBlank(0, 0);
        }
        if (startDirection == null) {
            AlwaysMoreItems.LOGGER.error("Null startDirection, defaulting to Top", new NullPointerException());
            startDirection = IDrawableAnimated.StartDirection.TOP;
        }

        if (inverted) {
            if (startDirection == IDrawableAnimated.StartDirection.TOP) {
                startDirection = IDrawableAnimated.StartDirection.BOTTOM;
            } else if (startDirection == IDrawableAnimated.StartDirection.BOTTOM) {
                startDirection = IDrawableAnimated.StartDirection.TOP;
            } else if (startDirection == IDrawableAnimated.StartDirection.LEFT) {
                startDirection = IDrawableAnimated.StartDirection.RIGHT;
            } else {
                startDirection = IDrawableAnimated.StartDirection.LEFT;
            }
        }

        int tickTimerMaxValue;
        if (startDirection == IDrawableAnimated.StartDirection.TOP || startDirection == IDrawableAnimated.StartDirection.BOTTOM) {
            tickTimerMaxValue = drawable.getHeight();
        } else {
            tickTimerMaxValue = drawable.getWidth();
        }
        ITickTimer tickTimer = new TickTimer(ticksPerCycle, tickTimerMaxValue, !inverted);
        return new DrawableAnimated(drawable, tickTimer, startDirection);
    }

    @Nonnull
    public static IDrawableStatic createDrawable(@Nullable String resourceLocation, int u, int v, int width, int height, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        if (resourceLocation == null) {
            AlwaysMoreItems.LOGGER.error("Null resourceLocation, returning blank drawable", new NullPointerException());
            return new DrawableBlank(width, height);
        }
        return new DrawableResource(resourceLocation, u, v, width, height, paddingTop, paddingBottom, paddingLeft, paddingRight);
    }

    @Nonnull
    public static IDrawableStatic createDrawable(@Nullable String resourceLocation, int u, int v, int width, int height) {
        if (resourceLocation == null) {
            AlwaysMoreItems.LOGGER.error("Null resourceLocation, returning blank drawable", new NullPointerException());
            return new DrawableBlank(width, height);
        }
        return new DrawableResource(resourceLocation, u, v, width, height);
    }

    public static IDrawable createBlankDrawable(int width, int height) {
        return new DrawableBlank(width, height);
    }

    /**
     * Draws a tooltip for you. Adds 9 and -15 to your mouseX and Y for you, so you don't need to offset it yourself.
     */
    public static void drawTooltip(List<String> tooltip, int mouseX, int mouseY) {
        if (tooltip != null) tooltip.stream().mapToInt(AMITextRenderer.INSTANCE::getWidth).max().ifPresent(tooltipWidth -> {
            int tooltipX = mouseX + 9;
            int tooltipY = mouseY - 15;
            AMIDrawContext.INSTANCE.fill(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + (8 * tooltip.size()) + (3 * tooltip.size()), -1073741824);
            IntStream.range(0, tooltip.size()).forEach(currentTooltip -> Minecraft.INSTANCE.textRenderer.draw(tooltip.get(currentTooltip), tooltipX + 1, tooltipY + (8 * currentTooltip) + (3 * currentTooltip) + 1, -1, true));
            IntStream.range(0, tooltip.size()).forEach(currentTooltip -> Minecraft.INSTANCE.textRenderer.draw(tooltip.get(currentTooltip), tooltipX, tooltipY + (8 * currentTooltip) + (3 * currentTooltip), -1, false));
        });
    }

    /**
     * Not part of drawTooltip because there are cases where this behavior might not be desirable. Also keeps the code easier to maintain for myself.
     * @param mouseX Your mouse X position.
     * @param mouseY Your mouse Y position.
     * @param currentTooltip The tooltip you're trying to render.
     * @param width The width of your screen.
     * @param height The height of your screen.
     * @return A BiTuple containing the offsets for the tooltip itself. You will need to add mouseX, mouseY, and potentially backgroundWidth/Height changes for containers.
     */
    public static BiTuple<Integer, Integer> getTooltipOffsets(int mouseX, int mouseY, List<String> currentTooltip, int width, int height) {
        int tooltipXOffset = 9;
        int tooltipYOffset = -15;
        if (currentTooltip != null && !currentTooltip.isEmpty()) {
            // Take away the extra padding (3) and the header+1 (9ea + 3ea) (which is above the cursor) from the equation
            int tooltipYHitbox = (currentTooltip.size() * (AMITextRenderer.FONT_HEIGHT + 3)) - 12 - 18;
            // The extra single padding and line? Fuck knows, I'm done pretending I know what I'm doing here.
            if (mouseY > height + tooltipYOffset - tooltipYHitbox + 15) {
                // render above
                tooltipYOffset -= tooltipYHitbox;
            }
            else if (mouseY < -tooltipYOffset) {
                tooltipYOffset = 0;
            }
            int maxTipLength = AMITextRenderer.INSTANCE.getWidth(currentTooltip.stream().reduce((s, s2) -> s.length() > s2.length() ? s : s2).get());
            if (mouseX + tooltipXOffset + maxTipLength > width) {
                tooltipXOffset = -tooltipXOffset - maxTipLength;
            }
        }
        // To account for the inbult offset of drawTooltip, yes, technically wasteful, but easier to think about.
        tooltipXOffset -= 9;
        tooltipYOffset += 15;
        return BiTuple.of(tooltipXOffset, tooltipYOffset);
    }
}
