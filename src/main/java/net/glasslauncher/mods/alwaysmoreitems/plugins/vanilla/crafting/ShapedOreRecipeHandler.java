package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapedOreRecipeHandler implements IRecipeHandler<StationShapedRecipe> {

	@Override
	@Nonnull
	public Class<StationShapedRecipe> getRecipeClass() {
		return StationShapedRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull StationShapedRecipe recipe) {
		return new ShapedOreRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull StationShapedRecipe recipe) {
		if (recipe.getOutputs() == null) {
			return false;
		}
		int inputCount = 0;
		for (Object input : recipe.getIngredients()) {
			if (input instanceof List) {
				if (((List) input).isEmpty()) {
					return false;
				}
			}
			if (input != null) {
				inputCount++;
			}
		}
		return inputCount > 0;
	}
}
