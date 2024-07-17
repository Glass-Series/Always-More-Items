package net.glasslauncher.alwaysmoreitems.plugins.jei.debug;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;

import javax.annotation.*;

public class DebugRecipeHandler implements IRecipeHandler<DebugRecipe> {
	@Nonnull
	@Override
	public Class<DebugRecipe> getRecipeClass() {
		return DebugRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "debug";
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull DebugRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull DebugRecipe recipe) {
		return true;
	}
}
