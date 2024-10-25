package net.glasslauncher.mods.alwaysmoreitems.api;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.StackHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferHandlerHelper;

import javax.annotation.Nonnull;

/**
 * IAMIHelpers provides helpers and tools for addon mods.
 * Available to IModPlugins
 */
public interface AMIHelpers {

    /**
     * Helps with getting itemStacks from recipes.
     */
    @Nonnull
    StackHelper getStackHelper();

    /**
     * Used to stop AMI from displaying a specific item in the item list.
     */
    @Nonnull
    ItemBlacklist getItemBlacklist();

    /**
     * Used to tell AMI to ignore NBT tags when comparing items for recipes.
     */
    @Nonnull
    NbtIgnoreList getNbtIgnoreList();

    /**
     * Helps with the implementation of Recipe Transfer Handlers
     */
    @Nonnull
    RecipeTransferHandlerHelper recipeTransferHandlerHelper();

    /**
     * Reload AMI at runtime.
     * Used by mods that add and remove items or recipes like MineTweaker's /mt reload.
     */
    void reload();
}
