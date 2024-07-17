package net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapelessOreRecipeHandler implements IRecipeHandler<StationShapelessRecipe> {

	@Override
	@Nonnull
	public Class<StationShapelessRecipe> getRecipeClass() {
		return StationShapelessRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull StationShapelessRecipe recipe) {
		return new ShapelessOreRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull StationShapelessRecipe recipe) {
		if (recipe.getOutputs() == null) {
			return false;
		}
		int inputCount = 0;
		for (Object input : recipe.getIngredients()) {
			if (input instanceof List list) {
				if (list.isEmpty()) {
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
