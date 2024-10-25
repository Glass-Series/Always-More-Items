package net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public interface CraftingRecipeWrapper extends RecipeWrapper {

    @Override
    List<?> getInputs();

    @Override
    List<ItemStack> getOutputs();

    @Override
    void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY);

}
