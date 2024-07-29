package net.glasslauncher.alwaysmoreitems.transfer;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandlerHelper;

import javax.annotation.*;
import java.util.*;

public class RecipeTransferHandlerHelper implements IRecipeTransferHandlerHelper {
	@Override
	public IRecipeTransferError createInternalError() {
		AlwaysMoreItems.LOGGER.warn("Internal error created", new Throwable());
		return RecipeTransferErrorInternal.instance;
	}

	@Override
	public IRecipeTransferError createUserErrorWithTooltip(@Nullable String tooltipMessage) {
		if (tooltipMessage == null) {
			AlwaysMoreItems.LOGGER.error("Null tooltipMessage", new NullPointerException());
			return RecipeTransferErrorInternal.instance;
		}
		return new RecipeTransferErrorTooltip(tooltipMessage);
	}

	@Override
	public IRecipeTransferError createUserErrorForSlots(@Nullable String tooltipMessage, @Nullable Collection<Integer> missingItemSlots) {
		if (tooltipMessage == null) {
			AlwaysMoreItems.LOGGER.error("Null tooltipMessage", new NullPointerException());
			return RecipeTransferErrorInternal.instance;
		}
		if (missingItemSlots == null) {
			AlwaysMoreItems.LOGGER.error("Null missingItemSlots", new NullPointerException());
			return RecipeTransferErrorInternal.instance;
		}
		if (missingItemSlots.isEmpty()) {
			AlwaysMoreItems.LOGGER.error("Empty missingItemSlots", new IllegalArgumentException());
			return RecipeTransferErrorInternal.instance;
		}

		return new RecipeTransferErrorSlots(tooltipMessage, missingItemSlots);
	}
}
