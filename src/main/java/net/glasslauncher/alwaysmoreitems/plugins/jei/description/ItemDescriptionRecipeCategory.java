package net.glasslauncher.alwaysmoreitems.plugins.jei.description;

import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.gui.IGuiItemStackGroup;
import net.glasslauncher.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;

import javax.annotation.*;

public class ItemDescriptionRecipeCategory implements IRecipeCategory {
	public static final int recipeWidth = 160;
	public static final int recipeHeight = 125;
	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;

	public ItemDescriptionRecipeCategory() {
		background = DrawableHelper.createBlankDrawable(recipeWidth, recipeHeight);
		localizedName = TranslationStorage.getInstance().get("gui.jei.category.itemDescription");
	}

	@Nonnull
	@Override
	public String getUid() {
		return VanillaRecipeCategoryUid.DESCRIPTION;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		int xPos = (recipeWidth - 18) / 2;
		guiItemStacks.init(0, false, xPos, 0);
		guiItemStacks.setFromRecipe(0, recipeWrapper.getOutputs());
	}
}
