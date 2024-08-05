package net.glasslauncher.mods.alwaysmoreitems.api.gui;

import net.minecraft.client.Minecraft;

import javax.annotation.*;

public interface AMIDrawable {

    int getWidth();

    int getHeight();

    void draw(@Nonnull Minecraft minecraft);

    void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset);

}
