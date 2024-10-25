package net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer;

import net.minecraft.screen.ScreenHandler;

import javax.annotation.Nonnull;

/**
 * Register recipe transfer handlers here to give AMI the information it needs to transfer recipes into the crafting area.
 */
public interface RecipeTransferRegistry {
    /**
     * Basic method for adding a recipe transfer handler.
     *
     * @param containerClass     the class of the container that this recipe transfer handler is for
     * @param recipeCategoryUid  the recipe categories that this container can use
     * @param recipeSlotStart    the first slot for recipe inputs
     * @param recipeSlotCount    the number of slots for recipe inputs
     * @param inventorySlotStart the first slot of the available inventory (usually player inventory)
     * @param inventorySlotCount the number of slots of the available inventory
     */
    void addRecipeTransferHandler(@Nonnull Class<? extends ScreenHandler> containerClass, @Nonnull String recipeCategoryUid, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount);

    /**
     * Advanced method for adding a recipe transfer handler.
     * <p>
     * Use this when recipe slots or inventory slots are spread out in different number ranges.
     */
    void addRecipeTransferHandler(@Nonnull RecipeTransferInfo recipeTransferInfo);

    /**
     * Complete control over recipe transfer.
     * <p>
     * Use this when the container has a non-standard inventory or crafting area.
     */
    void addRecipeTransferHandler(@Nonnull RecipeTransferHandler recipeTransferHandler);
}
