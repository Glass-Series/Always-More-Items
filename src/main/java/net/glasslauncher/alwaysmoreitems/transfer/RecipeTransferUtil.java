package net.glasslauncher.alwaysmoreitems.transfer;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.alwaysmoreitems.gui.RecipeLayout;
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
		ScreenHandler container = player.container;

		IRecipeTransferHandler transferHandler = AlwaysMoreItems.getRecipeRegistry().getRecipeTransferHandler(container, recipeLayout.getRecipeCategory());
		if (transferHandler == null) {
			if (doTransfer) {
				AlwaysMoreItems.LOGGER.error("No Recipe Transfer handler for container {}", container.getClass());
			}
			return RecipeTransferErrorInternal.instance;
		}

		try {
			return transferHandler.transferRecipe(container, recipeLayout, player, maxTransfer, doTransfer);
		} catch (AbstractMethodError ignored) {
			// older transferHandlers do not have the new method
			return transferHandler.transferRecipe(container, recipeLayout, player, doTransfer);
		}
	}
}
