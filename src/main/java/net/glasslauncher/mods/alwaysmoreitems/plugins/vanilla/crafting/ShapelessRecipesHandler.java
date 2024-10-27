package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;

import javax.annotation.Nonnull;

public class ShapelessRecipesHandler implements RecipeHandler<ShapelessRecipe> {

    @Override
    @Nonnull
    public Class<ShapelessRecipe> getRecipeClass() {
        return ShapelessRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    @Nonnull
    public RecipeWrapper getRecipeWrapper(@Nonnull ShapelessRecipe recipe) {
        return new ShapelessRecipesWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapelessRecipe recipe) {
        if (recipe.getOutput() == null) {
            return false;
        }
        int inputCount = 0;
        for (Object input : recipe.input) {
            if (input instanceof ItemStack) {
                inputCount++;
            } else {
                return false;
            }
        }
        return inputCount > 0;
    }
}
