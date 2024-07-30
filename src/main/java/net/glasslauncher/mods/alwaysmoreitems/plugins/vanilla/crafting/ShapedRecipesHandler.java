package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.ShapedRecipe;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.recipe.StationRecipe;

import javax.annotation.*;

public class ShapedRecipesHandler implements IRecipeHandler<ShapedRecipe> {

	@Override
	@Nonnull
	public Class<ShapedRecipe> getRecipeClass() {
		return ShapedRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull ShapedRecipe recipe) {
		return new ShapedRecipesWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull ShapedRecipe recipe) {
		if (((StationRecipe) recipe).getOutputs() == null) {
			return false;
		}
		int inputCount = 0;
		for (ItemStack input : ((StationRecipe) recipe).getIngredients()) {
			if (input != null) {
				inputCount++;
			}
		}
		return inputCount > 0;
	}
}
