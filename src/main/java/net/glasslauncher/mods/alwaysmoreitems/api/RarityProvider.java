package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

/**
 * You *can* just put an enum value into your item names, but this is cleaner for your code.
 * Use {@link ItemRarityProvider} instead.
 */
@Deprecated(forRemoval = true)
public interface RarityProvider {
    Rarity getRarity(ItemStack itemStack);
}
