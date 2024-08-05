package net.glasslauncher.mods.alwaysmoreitems.api.gui;

import net.minecraft.client.Minecraft;

import javax.annotation.*;

public interface StaticDrawable extends AMIDrawable {
    /**
     * Draw only part of the image, by masking off parts of it
     */
    void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight);
}
