package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;

import javax.annotation.*;

public class ShapelessRecipesHandler implements IRecipeHandler<ShapelessRecipe> {

	@Override
	@Nonnull
	public Class<ShapelessRecipe> getRecipeClass() {
		return ShapelessRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull ShapelessRecipe recipe) {
		return new ShapelessRecipesWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull ShapelessRecipe recipe) {
		if (((StationRecipe) recipe).getOutputs() == null) {
			return false;
		}
		int inputCount = 0;
		for (Object input : ((StationRecipe) recipe).getIngredients()) {
			if (input instanceof ItemStack) {
				inputCount++;
			} else {
				return false;
			}
		}
		return inputCount > 0;
	}
}
