package net.glasslauncher.mods.alwaysmoreitems.testmod.recipe;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;

import javax.annotation.*;

public class DebugRecipeHandler implements RecipeHandler<DebugRecipe> {
    @Nonnull
    @Override
    public Class<DebugRecipe> getRecipeClass() {
        return DebugRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "debug";
    }

    @Nonnull
    @Override
    public RecipeWrapper getRecipeWrapper(@Nonnull DebugRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull DebugRecipe recipe) {
        return true;
    }
}
