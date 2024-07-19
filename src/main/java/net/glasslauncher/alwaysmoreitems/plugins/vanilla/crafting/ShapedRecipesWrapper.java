package net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.alwaysmoreitems.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.ShapedRecipe;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.recipe.StationRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapedRecipesWrapper extends VanillaRecipeWrapper implements IShapedCraftingRecipeWrapper {

	@Nonnull
	private final ShapedRecipe recipe;

	public ShapedRecipesWrapper(@Nonnull ShapedRecipe recipe) {
		this.recipe = recipe;
		for (ItemStack itemStack : ((StationRecipe) recipe).getIngredients()) {
			if (itemStack != null && itemStack.count != 1) {
				itemStack.count = 1;
			}
		}
	}

	@Nonnull
	@Override
	public List getInputs() {
		return Arrays.asList(((StationRecipe) recipe).getIngredients());
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return Arrays.asList(((StationRecipe) recipe).getOutputs());
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}
}