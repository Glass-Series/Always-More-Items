package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.gui.IGuiItemStackGroup;
import net.glasslauncher.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;

import javax.annotation.*;

public class FurnaceSmeltingCategory extends FurnaceRecipeCategory {
	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;

	public FurnaceSmeltingCategory() {
		super();
		background = DrawableHelper.createDrawable("/gui/furnace.png", 55, 16, 82, 54);
		localizedName = TranslationStorage.getInstance().get("gui.ami.category.smelting");
	}

	@Override
	@Nonnull
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		flame.draw(minecraft, 2, 20);
		arrow.draw(minecraft, 24, 18);
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public String getUid() {
		return VanillaRecipeCategoryUid.SMELTING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(inputSlot, true, 0, 0);
		guiItemStacks.init(outputSlot, false, 60, 18);

		guiItemStacks.setFromRecipe(inputSlot, recipeWrapper.getInputs());
		guiItemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());
	}
}
