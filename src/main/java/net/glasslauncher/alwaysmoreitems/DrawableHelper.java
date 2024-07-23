package net.glasslauncher.alwaysmoreitems;

import net.glasslauncher.alwaysmoreitems.api.AMIRarity;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableAnimated;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableStatic;
import net.glasslauncher.alwaysmoreitems.api.gui.ITickTimer;
import net.glasslauncher.alwaysmoreitems.gui.widget.DrawableAnimated;
import net.glasslauncher.alwaysmoreitems.gui.widget.DrawableBlank;
import net.glasslauncher.alwaysmoreitems.gui.widget.DrawableResource;
import net.glasslauncher.alwaysmoreitems.util.TickTimer;
import net.glasslauncher.mods.gcapi.api.CharacterUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import javax.annotation.*;
import java.awt.*;
import java.util.*;
import java.util.List;
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
}
