package net.glasslauncher.mods.alwaysmoreitems.api;

import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * The main class for a plugin. Everything communicated between a mod and AMI is through this class.
 * IModPlugins must have the @AMIPlugin annotation to get loaded by AMI.
 * This class must not import anything that could be missing at runtime (i.e. code from any other mod).
 */
public interface ModPluginProvider {
    String getName();

    Identifier getId();

    /**
     * Called when the IAMIHelpers is available.
     * IModPlugins should store IAMIHelpers here if they need it.
     */
    void onAMIHelpersAvailable(AMIHelpers amiHelpers);

    /**
     * Called when the IItemRegistry is available, before register.
     */
    void onItemRegistryAvailable(ItemRegistry itemRegistry);

    /**
     * Register this mod plugin with the mod registry.
     * Called just before the game launches.
     */
    void register(ModRegistry registry);

    /**
     * Called when the IRecipeRegistry is available, after all mods have registered.
     */
    void onRecipeRegistryAvailable(RecipeRegistry recipeRegistry);

    /**
     * Called on each recipe given to the client from the server.
     * Don't you _DARE_ blindly trust the contents of the nbtcompound, unless you like RCE exploits.
     * @see SyncableRecipe
     */
    SyncableRecipe deserializeRecipe(NbtCompound recipe);

    /**
     * Called when the item blacklist needs to be updated. Can be called multiple times.
     */
    void updateBlacklist(AMIHelpers amiHelpers);
}
