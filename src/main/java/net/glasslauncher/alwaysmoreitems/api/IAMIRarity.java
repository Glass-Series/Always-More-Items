package net.glasslauncher.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

/**
 * You *can* just put an enum value into your item names, as that's what's done internally, but this is cleaner for your code.
 */
public interface IAMIRarity {

    AMIRarity getRarity(ItemStack itemStack);
}
