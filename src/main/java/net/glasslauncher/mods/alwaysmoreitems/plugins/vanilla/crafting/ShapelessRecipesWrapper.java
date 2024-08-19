package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.CraftingRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapelessRecipesWrapper extends VanillaRecipeWrapper implements CraftingRecipeWrapper {

	@Nonnull
	private final ShapelessRecipe recipe;

	public ShapelessRecipesWrapper(@Nonnull ShapelessRecipe recipe) {
		this.recipe = recipe;
		for (Object input : recipe.input) {
			if (input instanceof ItemStack itemStack) {
                if (itemStack.count != 1) {
					itemStack.count = 1;
				}
			}
		}
	}

	@Nonnull
	@Override
	public List<?> getInputs() {
		return recipe.input;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(recipe.getOutput());
	}
}
