package net.glasslauncher.mods.alwaysmoreitems.api.recipe;

import javax.annotation.Nonnull;

/**
 * An IRecipeHandler provides information about one Recipe Class.
 */
public interface RecipeHandler<T> {

    /**
     * Returns the class of the Recipe handled by this IRecipeHandler.
     */
    @Nonnull
    Class<T> getRecipeClass();

    /**
     * Returns this recipe's category unique id.
     */
    @Nonnull
    String getRecipeCategoryUid();

    /**
     * Returns a recipe wrapper for the given recipe.
     */
    @Nonnull
    RecipeWrapper getRecipeWrapper(@Nonnull T recipe);

    /**
     * Returns true if a recipe is valid and can be used.
     */
    boolean isRecipeValid(@Nonnull T recipe);
}
