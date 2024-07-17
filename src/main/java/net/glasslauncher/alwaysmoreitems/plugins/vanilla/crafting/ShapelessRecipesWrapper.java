package net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.alwaysmoreitems.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapelessRecipesWrapper extends VanillaRecipeWrapper implements ICraftingRecipeWrapper {

	@Nonnull
	private final ShapelessRecipe recipe;

	public ShapelessRecipesWrapper(@Nonnull ShapelessRecipe recipe) {
		this.recipe = recipe;
		for (Object input : ((StationRecipe) recipe).getIngredients()) {
			if (input instanceof ItemStack) {
				ItemStack itemStack = (ItemStack) input;
				if (itemStack.count != 1) {
					itemStack.count = 1;
				}
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
}
