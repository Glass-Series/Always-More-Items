package net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description;

import net.glasslauncher.mods.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IGuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
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
		localizedName = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.itemDescription");
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
