package net.glasslauncher.mods.alwaysmoreitems.transfer;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferError;

import javax.annotation.*;
import java.util.*;

public class RecipeTransferHandlerHelper implements net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferHandlerHelper {
	@Override
	public RecipeTransferError createInternalError() {
		AlwaysMoreItems.LOGGER.warn("Internal error created", new Throwable());
		return RecipeTransferErrorInternal.instance;
	}

	@Override
	public RecipeTransferError createUserErrorWithTooltip(@Nullable String tooltipMessage) {
		if (tooltipMessage == null) {
			AlwaysMoreItems.LOGGER.error("Null tooltipMessage", new NullPointerException());
			return RecipeTransferErrorInternal.instance;
		}
		return new RecipeTransferErrorTooltip(tooltipMessage);
	}

	@Override
	public RecipeTransferError createUserErrorForSlots(@Nullable String tooltipMessage, @Nullable Collection<Integer> missingItemSlots) {
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
