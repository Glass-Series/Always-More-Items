package net.glasslauncher.mods.alwaysmoreitems.gui;

import net.minecraft.client.gui.DrawContext;

/**
 * Done this way so folk changing how tooltips are typically rendered probably don't have to do much for AMI compat.
 * - 3 days before I ended up changing the entire system beyond recognition anyways.
 */
public class AMIDrawContext extends DrawContext {
    public static final AMIDrawContext INSTANCE = new AMIDrawContext();

    @Override
    public void fillGradient(int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        super.fillGradient(startX, startY, endX, endY, colorStart, colorEnd);
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int color) {
        super.fill(x1, y1, x2, y2, color);
    }

    @Override
    public void drawVerticalLine(int x, int y1, int y2, int color) {
        super.drawVerticalLine(x, y1, y2, color);
    }

    @Override
    public void drawHorizontalLine(int x1, int x2, int y, int color) {
        super.drawHorizontalLine(x1, x2, y, color);
    }
}
