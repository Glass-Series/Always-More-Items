package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.*;

public class FuelRecipeHandler implements IRecipeHandler<FuelRecipe> {
	@Override
	@Nonnull
	public Class<FuelRecipe> getRecipeClass() {
		return FuelRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.FUEL;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull FuelRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull FuelRecipe recipe) {
		return !recipe.getInputs().isEmpty() && recipe.getOutputs().isEmpty();
	}
}
