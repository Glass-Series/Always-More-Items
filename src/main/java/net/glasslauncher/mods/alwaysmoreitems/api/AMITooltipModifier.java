package net.glasslauncher.mods.alwaysmoreitems.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Attach this to a public method inside your item class with an ItemStack parameter and {@link java.util.ArrayList<Object>}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AMITooltipModifier {
}
