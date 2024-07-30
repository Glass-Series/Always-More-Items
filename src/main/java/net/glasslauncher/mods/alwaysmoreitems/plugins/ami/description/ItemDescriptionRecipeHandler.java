package net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.*;
import java.util.*;

public class ItemDescriptionRecipeHandler implements IRecipeHandler<ItemDescriptionRecipe> {
	@Nonnull
	@Override
	public Class<ItemDescriptionRecipe> getRecipeClass() {
		return ItemDescriptionRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.DESCRIPTION;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull ItemDescriptionRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull ItemDescriptionRecipe recipe) {
		List<String> description = recipe.getDescription();
		return !description.isEmpty();
	}
}
