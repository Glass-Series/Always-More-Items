package net.glasslauncher.alwaysmoreitems.api.recipe.wrapper;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public interface ICraftingRecipeWrapper extends IRecipeWrapper {

	@Override
	List getInputs();

	@Override
	List<ItemStack> getOutputs();

	@Override
	void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY);

}
