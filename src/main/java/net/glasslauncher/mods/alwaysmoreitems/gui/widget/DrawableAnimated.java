package net.glasslauncher.mods.alwaysmoreitems.gui.widget;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AnimatedDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.StaticDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.TickTimer;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class DrawableAnimated implements AnimatedDrawable {
    private final StaticDrawable drawable;
    private final TickTimer tickTimer;
    private final StartDirection startDirection;

    public DrawableAnimated(StaticDrawable drawable, TickTimer tickTimer, StartDirection startDirection) {
        this.drawable = drawable;
        this.tickTimer = tickTimer;
        this.startDirection = startDirection;
    }

    @Override
    public int getWidth() {
        return drawable.getWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getHeight();
    }

    @Override
    public void draw(@Nonnull Minecraft minecraft) {
        draw(minecraft, 0, 0);
    }

    @Override
    public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset) {
        int maskLeft = 0;
        int maskRight = 0;
        int maskTop = 0;
        int maskBottom = 0;

        int animationValue = tickTimer.getValue();

        switch (startDirection) {
            case TOP:
                maskBottom = animationValue;
                break;
            case BOTTOM:
                maskTop = animationValue;
                break;
            case LEFT:
                maskRight = animationValue;
                break;
            case RIGHT:
                maskLeft = animationValue;
                break;
        }

        drawable.draw(minecraft, xOffset, yOffset, maskTop, maskBottom, maskLeft, maskRight);
    }
}
