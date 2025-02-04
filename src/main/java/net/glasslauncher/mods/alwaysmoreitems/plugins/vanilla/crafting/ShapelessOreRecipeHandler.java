package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class ShapelessOreRecipeHandler implements RecipeHandler<StationShapelessRecipe> {

    @Override
    @Nonnull
    public Class<StationShapelessRecipe> getRecipeClass() {
        return StationShapelessRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    @Nonnull
    public RecipeWrapper getRecipeWrapper(@Nonnull StationShapelessRecipe recipe) {
        return new ShapelessOreRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull StationShapelessRecipe recipe) {
        if (recipe.getIngredients() == null) {
            return false;
        }
        int inputCount = 0;
        for (Object input : recipe.getIngredients()) {
            if (input instanceof List list) {
                if (list.isEmpty()) {
                    return false;
                }
            }
            if (input != null) {
                inputCount++;
            }
        }
        return inputCount > 0;
    }
}
