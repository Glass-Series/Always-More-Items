package net.glasslauncher.mods.alwaysmoreitems.transfer;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferError;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class RecipeTransferErrorInternal implements RecipeTransferError {
    public static final RecipeTransferErrorInternal instance = new RecipeTransferErrorInternal();

    private RecipeTransferErrorInternal() {

    }

    @Override
    public Type getType() {
        return Type.DEV;
    }

    @Override
    public void showError(@NotNull Minecraft minecraft, int mouseX, int mouseY, int containerX, int containerY, @NotNull RecipeLayout recipeLayout) {

    }
}
