package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.item.ItemStack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Attach this to a public method inside your item class and return any special instances of your items inside an {@link ItemStack} {@link List}. Meta values are autodetected imprecisely if you don't implement this for your item class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubItemProvider {}
