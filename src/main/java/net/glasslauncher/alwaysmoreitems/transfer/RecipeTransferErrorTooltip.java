package net.glasslauncher.alwaysmoreitems.transfer;

import net.glasslauncher.alwaysmoreitems.AMITooltipSystem;
import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.gui.RecipeLayout;
import net.minecraft.client.Minecraft;

import javax.annotation.*;
import java.util.*;

public class RecipeTransferErrorTooltip implements IRecipeTransferError {
	@Nonnull
	private final String message;

	public RecipeTransferErrorTooltip(@Nonnull String message) {
		this.message = message;
	}

	@Override
	public Type getType() {
		return Type.USER_FACING;
	}

	@Override
	public void showError(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int containerX, int containerY, @Nonnull RecipeLayout recipeLayout) {
		AMITooltipSystem.drawTooltip(Collections.singletonList(message), mouseX, mouseY);
	}
}
