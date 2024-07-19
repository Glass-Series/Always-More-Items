package net.glasslauncher.alwaysmoreitems.api.gui;

import net.minecraft.client.Minecraft;

import javax.annotation.*;

public interface IDrawable {

	int getWidth();

	int getHeight();

	void draw(@Nonnull Minecraft minecraft);

	void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset);

}
