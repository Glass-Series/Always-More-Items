package net.glasslauncher.alwaysmoreitems.transfer;

import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.gui.RecipeLayout;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class RecipeTransferErrorInternal implements IRecipeTransferError {
	public static final RecipeTransferErrorInternal instance = new RecipeTransferErrorInternal();

	private RecipeTransferErrorInternal() {

	}

	@Override
	public Type getType() {
		return Type.INTERNAL;
	}

	@Override
	public void showError(@NotNull Minecraft minecraft, int mouseX, int mouseY, int containerX, int containerY, @NotNull RecipeLayout recipeLayout) {

	}
}
