package net.glasslauncher.mods.alwaysmoreitems.transfer;

import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.*;

public class RecipeTransferUtil {
	public static IRecipeTransferError getTransferRecipeError(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player) {
		return transferRecipe(recipeLayout, player, false, false);
	}

	public static boolean transferRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer) {
		IRecipeTransferError error = transferRecipe(recipeLayout, player, maxTransfer, true);
		return error == null;
	}

	@Nullable
	private static IRecipeTransferError transferRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
		Screen parentScreen = OverlayScreen.INSTANCE.parent;
		if (parentScreen instanceof HandledScreen handledScreen) {
			ScreenHandler container = handledScreen.container;

			IRecipeTransferHandler transferHandler = AlwaysMoreItems.getRecipeRegistry().getRecipeTransferHandler(container, recipeLayout.getRecipeCategory());
			if (transferHandler == null) {
				if (doTransfer) {
					AlwaysMoreItems.LOGGER.error("No Recipe Transfer handler for container {}", container.getClass());
				}
				AlwaysMoreItems.LOGGER.warn("No Recipe Transfer handler for container {}", container.getClass());
				return RecipeTransferErrorInternal.instance;
			}

			return transferHandler.transferRecipe(container, recipeLayout, player, maxTransfer, doTransfer);
		}
		return RecipeTransferErrorInternal.instance;
	}
}
