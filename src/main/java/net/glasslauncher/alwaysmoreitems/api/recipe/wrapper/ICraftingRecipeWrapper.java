package net.glasslauncher.alwaysmoreitems.api.recipe.wrapper;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;

public interface ICraftingRecipeWrapper extends IRecipeWrapper {

	@Override
	List getInputs();

	@Override
	List<ItemStack> getOutputs();

	@Override
	void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY);

}
