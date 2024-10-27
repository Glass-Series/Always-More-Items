package net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemDescriptionRecipeHandler implements RecipeHandler<ItemDescriptionRecipe> {
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
    public RecipeWrapper getRecipeWrapper(@Nonnull ItemDescriptionRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull ItemDescriptionRecipe recipe) {
        List<String> description = recipe.getDescription();
        return !description.isEmpty();
    }
}
