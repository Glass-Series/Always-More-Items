package net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.*;

/**
 * Gives AMI the information it needs to transfer recipes from a slotted inventory into the crafting area.
 * <p>
 * Most plugins should create new IRecipeTransferInfo instances with IModRegistry.createRecipeTransferHelper.
 * Containers with slot ranges that contain gaps or other oddities can implement this interface directly.
 * Containers that need full control over the recipe transfer or do not use slots can implement IRecipeTransferHandler.
 */
public interface RecipeTransferInfo {
    /**
     * Return the container class that this recipe transfer helper supports
     */
    Class<? extends ScreenHandler> getContainerClass();

    /**
     * Return the recipe category that this container can handle.
     */
    String getRecipeCategoryUid();

    /**
     * Return a list of slots for the recipe area.
     */
    List<Slot> getRecipeSlots(ScreenHandler container);

    /**
     * Return a list of slots that the transfer can use to get items for crafting, or place leftover items.
     */
    List<Slot> getInventorySlots(ScreenHandler container);
}
