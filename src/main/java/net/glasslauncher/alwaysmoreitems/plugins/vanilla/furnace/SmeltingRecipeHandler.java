package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.*;

public class SmeltingRecipeHandler implements IRecipeHandler<SmeltingRecipe> {

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
	public IRecipeWrapper getRecipeWrapper(@Nonnull SmeltingRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull SmeltingRecipe recipe) {
		return !recipe.getInputs().isEmpty() && !recipe.getOutputs().isEmpty();
	}

}
