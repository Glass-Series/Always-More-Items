package net.glasslauncher.alwaysmoreitems.api.gui;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;

public interface IDrawable {

	int getWidth();

	int getHeight();

	void draw(@Nonnull Minecraft minecraft);

	void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset);

}
