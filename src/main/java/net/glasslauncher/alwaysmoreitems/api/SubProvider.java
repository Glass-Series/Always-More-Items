package net.glasslauncher.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

import java.util.*;

public interface SubProvider {

    /***
     * Return any special instances of your items here. Meta values are autodetected imprecisely.
     */
    List<ItemStack> getSubItems();
}
