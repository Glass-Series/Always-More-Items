package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import javax.annotation.*;

public interface NbtIgnoreList {
    /**
     * Tell AMI to ignore NBT tags on a specific item when comparing items for recipes.
     * NBT is automatically ignored on items that don't have subtypes.
     */
    void ignoreNbtTagNames(@Nonnull Item item, String... nbtTagNames);

    /**
     * Check to see if an NBT tag is ignored.
     */
    boolean isNbtTagIgnored(@Nonnull String nbtTagName);

    /**
     * Get NBT from an itemStack, minus the NBT that is being ignored.
     * Returns null if the itemStack has no NBT.
     */
    @Nullable
    NbtCompound getNbt(@Nonnull ItemStack itemStack);
}
