package net.glasslauncher.mods.alwaysmoreitems;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.AnimatedDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.StaticDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.TickTimer;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.DrawableBlank;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.DrawableResource;

import javax.annotation.*;

public class DrawableHelper {

    @Nonnull
    public static AnimatedDrawable createAnimatedDrawable(@Nullable StaticDrawable drawable, int ticksPerCycle, @Nullable AnimatedDrawable.StartDirection startDirection, boolean inverted) {
        if (drawable == null) {
            AlwaysMoreItems.LOGGER.error("Null drawable, returning blank drawable", new NullPointerException());
            return new DrawableBlank(0, 0);
        }
        if (startDirection == null) {
            AlwaysMoreItems.LOGGER.error("Null startDirection, defaulting to Top", new NullPointerException());
            startDirection = AnimatedDrawable.StartDirection.TOP;
        }

        if (inverted) {
            if (startDirection == AnimatedDrawable.StartDirection.TOP) {
                startDirection = AnimatedDrawable.StartDirection.BOTTOM;
            } else if (startDirection == AnimatedDrawable.StartDirection.BOTTOM) {
                startDirection = AnimatedDrawable.StartDirection.TOP;
            } else if (startDirection == AnimatedDrawable.StartDirection.LEFT) {
                startDirection = AnimatedDrawable.StartDirection.RIGHT;
            } else {
                startDirection = AnimatedDrawable.StartDirection.LEFT;
            }
        }

        int tickTimerMaxValue;
        if (startDirection == AnimatedDrawable.StartDirection.TOP || startDirection == AnimatedDrawable.StartDirection.BOTTOM) {
            tickTimerMaxValue = drawable.getHeight();
        } else {
            tickTimerMaxValue = drawable.getWidth();
        }
        TickTimer tickTimer = new net.glasslauncher.mods.alwaysmoreitems.util.TickTimer(ticksPerCycle, tickTimerMaxValue, !inverted);
        return new net.glasslauncher.mods.alwaysmoreitems.gui.widget.DrawableAnimated(drawable, tickTimer, startDirection);
    }

    @Nonnull
    public static StaticDrawable createDrawable(@Nullable String resourceLocation, int u, int v, int width, int height, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        if (resourceLocation == null) {
            AlwaysMoreItems.LOGGER.error("Null resourceLocation, returning blank drawable", new NullPointerException());
            return new DrawableBlank(width, height);
        }
        return new DrawableResource(resourceLocation, u, v, width, height, paddingTop, paddingBottom, paddingLeft, paddingRight);
    }

    @Nonnull
    public static StaticDrawable createDrawable(@Nullable String resourceLocation, int u, int v, int width, int height) {
        if (resourceLocation == null) {
            AlwaysMoreItems.LOGGER.error("Null resourceLocation, returning blank drawable", new NullPointerException());
            return new DrawableBlank(width, height);
        }
        return new DrawableResource(resourceLocation, u, v, width, height);
    }

    public static AMIDrawable createBlankDrawable(int width, int height) {
        return new DrawableBlank(width, height);
    }
}
