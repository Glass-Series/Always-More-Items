package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface SubProvider {

    /***
     * Return any special instances of your items here. Meta values are autodetected imprecisely.
     */
    List<ItemStack> getSubItems();
}
