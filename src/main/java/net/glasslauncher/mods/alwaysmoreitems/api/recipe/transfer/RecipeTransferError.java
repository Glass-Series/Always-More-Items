package net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer;

import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

/**
 * A reason that the recipe transfer couldn't happen. See IRecipeTransferError.Type
 */
public interface RecipeTransferError {
    enum Type {
        /**
         * Errors where the Transfer handler is broken, or does not work, or the server is not present.
         * These errors will hide the recipe transfer button, but do not display anything to the user.
         */
        DEV,

        /**
         * Errors that the player can fix. Missing items, inventory full, etc.
         * Something informative will be shown to the player.
         */
        PLAYER
    }

    Type getType();

    /**
     * Called on USER_FACING errors
     */
    void showError(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int containerX, int containerY, @Nonnull RecipeLayout recipeLayout);
}
