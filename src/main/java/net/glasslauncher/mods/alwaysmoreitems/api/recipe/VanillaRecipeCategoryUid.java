package net.glasslauncher.mods.alwaysmoreitems.api.recipe;

/**
 * List of vanilla recipe category UIDs, so that addons with their own vanilla recipe handlers can use them.
 */
public class VanillaRecipeCategoryUid {
    private VanillaRecipeCategoryUid() {

    }

    public static final String CRAFTING = "minecraft.crafting";
    public static final String SMELTING = "minecraft.smelting";
    public static final String FUEL = "minecraft.fuel";

    public static final String DESCRIPTION = "alwaysmoreitems.description";
}
