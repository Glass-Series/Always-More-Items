package net.glasslauncher.mods.alwaysmoreitems.transfer;

import net.glasslauncher.mods.alwaysmoreitems.AMITooltipSystem;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferError;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.minecraft.client.Minecraft;

import javax.annotation.*;
import java.util.*;

public class RecipeTransferErrorTooltip implements RecipeTransferError {
	@Nonnull
	private final String message;

	public RecipeTransferErrorTooltip(@Nonnull String message) {
		this.message = message;
	}

	@Override
	public Type getType() {
		return Type.PLAYER;
	}

	@Override
	public void showError(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int containerX, int containerY, @Nonnull RecipeLayout recipeLayout) {
		AMITooltipSystem.drawTooltip(Collections.singletonList(message), mouseX, mouseY, false);
	}
}
