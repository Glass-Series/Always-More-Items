package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attach this to a public method inside your item and return the {@link Rarity} for the provided {@link ItemStack}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ItemRarityProvider {
}
