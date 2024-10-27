package net.glasslauncher.mods.alwaysmoreitems.util;

import com.google.common.collect.ImmutableList;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;

public class RecipeCategoryComparator implements Comparator<RecipeCategory> {
    @Nonnull
    private final ImmutableList<RecipeCategory> recipeCategories;

    public RecipeCategoryComparator(@Nonnull List<RecipeCategory> recipeCategories) {
        this.recipeCategories = ImmutableList.copyOf(recipeCategories);
    }

    @Override
    public int compare(RecipeCategory recipeCategory1, RecipeCategory recipeCategory2) {
        Integer index1 = recipeCategories.indexOf(recipeCategory1);
        Integer index2 = recipeCategories.indexOf(recipeCategory2);
        return index1.compareTo(index2);
    }
}
