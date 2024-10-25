package net.glasslauncher.mods.alwaysmoreitems.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * The IItemRegistry is provided by AMI and has some useful functions related to items.
 */
public interface ItemRegistry {

    /**
     * Returns a list of all the Items registered.
     */
    @Nonnull
    ImmutableList<ItemStack> getItemList();

    /**
     * Returns a list of all the Items that can be used as fuel in a vanilla furnace.
     */
    @Nonnull
    ImmutableList<ItemStack> getFuels();

    /**
     * Returns a mod name for the given item.
     */
    @Nonnull
    String getModNameForItem(@Nonnull Item item);

    @Nonnull
    ImmutableList<ItemStack> getItemListForModId(@Nonnull String modId);
}
