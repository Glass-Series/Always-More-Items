package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.ShapedCraftingRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.ShapedRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class ShapedRecipesWrapper extends VanillaRecipeWrapper implements ShapedCraftingRecipeWrapper {

	@Nonnull
	private final ShapedRecipe recipe;

	public ShapedRecipesWrapper(@Nonnull ShapedRecipe recipe) {
		this.recipe = recipe;
		for (ItemStack itemStack : recipe.input) {
			if (itemStack != null && itemStack.count != 1) {
				itemStack.count = 1;
			}
		}
	}

	@Nonnull
	@Override
	public List<?> getInputs() {
		return Arrays.asList(recipe.input);
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return Arrays.asList(recipe.getOutput());
	}

	@Override
	public int getWidth() {
		return recipe.width;
	}

	@Override
	public int getHeight() {
		return recipe.height;
	}
}
