package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;

import javax.annotation.Nonnull;

public class ShapedRecipesHandler implements RecipeHandler<ShapedRecipe> {

    @Override
    @Nonnull
    public Class<ShapedRecipe> getRecipeClass() {
        return ShapedRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    @Nonnull
    public RecipeWrapper getRecipeWrapper(@Nonnull ShapedRecipe recipe) {
        return new ShapedRecipesWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapedRecipe recipe) {
        if (recipe.getOutput() == null) {
            return false;
        }
        int inputCount = 0;
        for (ItemStack input : recipe.input) {
            if (input != null) {
                inputCount++;
            }
        }
        return inputCount > 0;
    }
}
