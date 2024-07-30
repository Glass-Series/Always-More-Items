package net.glasslauncher.mods.alwaysmoreitems.util;

import com.google.common.collect.ImmutableList;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeCategory;

import javax.annotation.*;
import java.util.*;

public class RecipeCategoryComparator implements Comparator<IRecipeCategory> {
	@Nonnull
	private final ImmutableList<IRecipeCategory> recipeCategories;

	public RecipeCategoryComparator(@Nonnull List<IRecipeCategory> recipeCategories) {
		this.recipeCategories = ImmutableList.copyOf(recipeCategories);
	}

	@Override
	public int compare(IRecipeCategory recipeCategory1, IRecipeCategory recipeCategory2) {
		Integer index1 = recipeCategories.indexOf(recipeCategory1);
		Integer index2 = recipeCategories.indexOf(recipeCategory2);
		return index1.compareTo(index2);
	}
}
