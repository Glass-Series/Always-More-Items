package net.glasslauncher.mods.alwaysmoreitems.transfer;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferError;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferHandler;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeTransferUtil {

    public static RecipeTransferError getTransferRecipeError(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player) {
        return transferRecipe(recipeLayout, player, false, false);
    }

    public static boolean transferRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer) {
        RecipeTransferError error = transferRecipe(recipeLayout, player, maxTransfer, true);
        return error == null;
    }

    @Nullable
    private static RecipeTransferError transferRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        Screen parentScreen = OverlayScreen.INSTANCE.parent;
        if (parentScreen instanceof HandledScreen handledScreen) {
            ScreenHandler container = handledScreen.container;

            RecipeTransferHandler transferHandler = AlwaysMoreItems.getRecipeRegistry().getRecipeTransferHandler(container, recipeLayout.getRecipeCategory());
            if (transferHandler == null) {
                return RecipeTransferErrorInternal.instance;
            }

            return transferHandler.transferRecipe(container, recipeLayout, player, maxTransfer, doTransfer);
        }
        return RecipeTransferErrorInternal.instance;
    }
}
