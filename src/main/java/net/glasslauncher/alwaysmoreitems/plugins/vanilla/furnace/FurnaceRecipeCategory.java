package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableAnimated;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableStatic;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;

import javax.annotation.*;

public abstract class FurnaceRecipeCategory implements IRecipeCategory {
	protected static final int inputSlot = 0;
	protected static final int fuelSlot = 1;
	protected static final int outputSlot = 2;

	@Nonnull
	protected final IDrawableAnimated flame;
	@Nonnull
	protected final IDrawableAnimated arrow;

	public FurnaceRecipeCategory() {

		IDrawableStatic flameDrawable = DrawableHelper.createDrawable("/gui/furnace.png", 176, 0, 14, 14);
		flame = DrawableHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

		IDrawableStatic arrowDrawable = DrawableHelper.createDrawable("/gui/furnace.png", 176, 14, 24, 17);
		this.arrow = DrawableHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}
}
