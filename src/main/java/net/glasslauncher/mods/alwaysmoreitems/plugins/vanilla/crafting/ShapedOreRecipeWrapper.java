package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.ShapedCraftingRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapedOreRecipeWrapper extends VanillaRecipeWrapper implements ShapedCraftingRecipeWrapper {

	@Nonnull
	private final StationShapedRecipe recipe;

    public ShapedOreRecipeWrapper(@Nonnull StationShapedRecipe recipe) {
		this.recipe = recipe;
		for (Object input : this.recipe.getIngredients()) {
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
		return Arrays.asList(recipe.getIngredients());
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return List.of(recipe.getOutputs());
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
