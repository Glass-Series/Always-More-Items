package net.glasslauncher.alwaysmoreitems.gui.widget;

import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableAnimated;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;

import javax.annotation.*;

public class DrawableBlank implements IDrawableStatic, IDrawableAnimated {
	private final int width;
	private final int height;

	public DrawableBlank(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {

	}

	@Override
	public void draw(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset) {

	}
}
