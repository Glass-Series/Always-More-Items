package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.*;

public class SmeltingRecipeHandler implements RecipeHandler<SmeltingRecipe> {

	@Override
	@Nonnull
	public Class<SmeltingRecipe> getRecipeClass() {
		return SmeltingRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.SMELTING;
	}

	@Override
	@Nonnull
	public RecipeWrapper getRecipeWrapper(@Nonnull SmeltingRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull SmeltingRecipe recipe) {
		return !recipe.getInputs().isEmpty() && !recipe.getOutputs().isEmpty();
	}

}
