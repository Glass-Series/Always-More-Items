package net.glasslauncher.mods.alwaysmoreitems.api;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IStackHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandlerHelper;

import javax.annotation.*;

/**
 * IAMIHelpers provides helpers and tools for addon mods.
 * Available to IModPlugins
 */
public interface IAMIHelpers {

    /**
     * Helps with getting itemStacks from recipes.
     */
    @Nonnull
    IStackHelper getStackHelper();

    /**
     * Used to stop AMI from displaying a specific item in the item list.
     */
    @Nonnull
    IItemBlacklist getItemBlacklist();

    /**
     * Used to tell AMI to ignore NBT tags when comparing items for recipes.
     */
    @Nonnull
    INbtIgnoreList getNbtIgnoreList();

    /**
     * Helps with the implementation of Recipe Transfer Handlers
     */
    @Nonnull
    IRecipeTransferHandlerHelper recipeTransferHandlerHelper();

    /**
     * Reload AMI at runtime.
     * Used by mods that add and remove items or recipes like MineTweaker's /mt reload.
     */
    void reload();
}
