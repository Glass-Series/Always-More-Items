package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ItemBlacklist {
    /**
     * Stop AMI from displaying a specific item in the item list.
     * Use OreDictionary.WILDCARD_VALUE meta for wildcard.
     * Items blacklisted with this API can't be seen in the config or in edit mode.
     */
    void addItemToBlacklist(@Nonnull ItemStack itemStack);

    /**
     * Undo blacklisting an item.
     * This is for mods that hide items initially and reveal them when certain conditions are met.
     * Items blacklisted by the user in the config will remain hidden.
     */
    void removeItemFromBlacklist(@Nonnull ItemStack itemStack);

    /**
     * Returns true if the item is blacklisted and will not be displayed in the item list.
     */
    boolean isItemBlacklisted(@Nonnull ItemStack itemStack);

    /**
     * Returns true if the item is blacklisted in the api and will not be displayed in the item list ever.
     */
    boolean isItemAPIBlacklisted(@Nonnull ItemStack itemStack);
}
