package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * Required to be implemented in your recipe classes.
 * If you don't do this: Your. Recipes. Won't. Sync.
 * @see IModPlugin#deserializeRecipe(NbtCompound)
 */
public interface IAMISyncableRecipe {

    /**
     * Serialize your recipe to NbtCompund.
     * @return Your recipe in NbtCompound form.
     * @see IModPlugin#deserializeRecipe(NbtCompound)
     */
    NbtCompound exportRecipe();

    /**
     * The ID of the plugin that should handle deserialization of this recipe.
     */
    Identifier getPlugin();
}
