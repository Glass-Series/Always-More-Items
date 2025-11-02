package net.glasslauncher.mods.alwaysmoreitems.plugins.multiblock;

import net.glasslauncher.mods.alwaysmoreitems.api.*;
import net.glasslauncher.mods.alwaysmoreitems.registry.multiblock.MultiBlockRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public class MultiblockPluginProvider implements ModPluginProvider {
    @Override
    public String getName() {
        return "Multiblock Tab";
    }

    @Override
    public Identifier getId() {
        return AlwaysMoreItems.NAMESPACE.id("alwaysmoreitems");
    }

    @Override
    public void onAMIHelpersAvailable(AMIHelpers amiHelpers) {

    }

    @Override
    public void onItemRegistryAvailable(ItemRegistry itemRegistry) {

    }

    @Override
    public void register(ModRegistry registry) {
        registry.addRecipeCategories(new MultiBlockRecipeCategory());

        registry.addRecipeHandlers(new MultiBlockRecipeHandler());
        registry.addRecipes(MultiBlockRecipeRegistry.INSTANCE.getRecipes());

    }

    @Override
    public void onRecipeRegistryAvailable(RecipeRegistry recipeRegistry) {

    }

    @Override
    public SyncableRecipe deserializeRecipe(NbtCompound recipe) {
        return null;
    }

    @Override
    public void updateBlacklist(AMIHelpers amiHelpers) {

    }
}
