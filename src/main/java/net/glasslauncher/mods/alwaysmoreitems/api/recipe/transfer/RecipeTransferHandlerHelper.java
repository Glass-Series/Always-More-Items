package net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer;

import javax.annotation.*;
import java.util.*;

/**
 * Helper functions for implementing an IRecipeTransferHandler
 */
public interface RecipeTransferHandlerHelper {
    /**
     * Create an error with type INTERNAL.
     * It is recommended that you also log a message to the console.
     */
    RecipeTransferError createInternalError();

    /**
     * Create an error with type USER_FACING that shows a tooltip.
     *
     * @param tooltipMessage the message to show on the tooltip for the recipe transfer button.
     */
    RecipeTransferError createUserErrorWithTooltip(@Nonnull String tooltipMessage);

    /**
     * Create an error with type USER_FACING that shows a tooltip and highlights missing item slots.
     *
     * @param tooltipMessage   the message to show on the tooltip for the recipe transfer button.
     * @param missingItemSlots the slot indexes for items that are missing. Must not be empty.
     *                         Slots are indexed according to itemStackGroup.getGuiIngredients()
     */
    RecipeTransferError createUserErrorForSlots(@Nonnull String tooltipMessage, @Nonnull Collection<Integer> missingItemSlots);
}
