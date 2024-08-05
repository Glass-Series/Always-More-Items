package net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.*;

/**
 * A recipe transfer handler moves items into a crafting area, based on the items in a recipe.
 * <p>
 * Implementing this interface gives full control over the recipe transfer process.
 * Mods that use a regular slotted inventory can use IRecipeTransferInfo instead, which is much simpler.
 */
public interface RecipeTransferHandler {
    /**
     * The container that this recipe transfer handler can use.
     */
    Class<? extends ScreenHandler> getContainerClass();

    /**
     * The type of recipe that this recipe transfer handler deals with.
     */
    String getRecipeCategoryUid();

    /**
     * @param container    the container to act on
     * @param recipeLayout the layout of the recipe, with information about the ingredients
     * @param player       the player, to do the slot manipulation
     * @param maxTransfer  if true, transfer as many items as possible. if false, transfer one set
     * @param doTransfer   if true, do the transfer. if false, check for errors but do not actually transfer the items
     * @return a recipe transfer error if the recipe can't be transferred. Return null on success.
     * @since AMI 2.20.0
     */
    @Nullable
    RecipeTransferError transferRecipe(@Nonnull ScreenHandler container, @Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer, boolean doTransfer);
}
