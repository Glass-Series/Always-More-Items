package net.glasslauncher.mods.alwaysmoreitems.testmod.init;

import net.glasslauncher.mods.alwaysmoreitems.api.AMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.api.ItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.ModPluginProvider;
import net.glasslauncher.mods.alwaysmoreitems.api.ModRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.testmod.TestMod;
import net.glasslauncher.mods.alwaysmoreitems.testmod.recipe.DebugRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.testmod.recipe.DebugRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugPlugin implements ModPluginProvider {

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    public Identifier getId() {
        return TestMod.NAMESPACE.id("debug");
    }

    @Override
    public void onAMIHelpersAvailable(AMIHelpers amiHelpers) {

    }

    @Override
    public void onItemRegistryAvailable(ItemRegistry itemRegistry) {

    }

    @Override
    public void register(ModRegistry registry) {

        registry.addDescription(
                new ItemStack(Item.WOODEN_DOOR),
                "description.alwaysmoreitemstest.wooden.door.1", // actually 2 lines
                "description.alwaysmoreitemstest.wooden.door.2",
                "description.alwaysmoreitemstest.wooden.door.3"
        );

        registry.addRecipeCategories(new DebugRecipeCategory());
        registry.addRecipeHandlers(new DebugRecipeHandler());
//        registry.addRecipes(Arrays.asList(
//                new DebugRecipe(),
//                new DebugRecipe()
//        ));
    }

    @Override
    public void onRecipeRegistryAvailable(RecipeRegistry recipeRegistry) {

    }

    @Override
    public SyncableRecipe deserializeRecipe(NbtCompound recipe) {
        return null;
    }
}
