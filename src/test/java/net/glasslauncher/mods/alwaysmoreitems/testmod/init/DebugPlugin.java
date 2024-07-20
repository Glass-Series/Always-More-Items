package net.glasslauncher.mods.alwaysmoreitems.testmod.init;

import net.glasslauncher.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.testmod.recipe.DebugRecipe;
import net.glasslauncher.mods.alwaysmoreitems.testmod.recipe.DebugRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.testmod.recipe.DebugRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class DebugPlugin implements IModPlugin {

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    public void onAMIHelpersAvailable(IAMIHelpers amiHelpers) {

    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

    }

    @Override
    public void register(IModRegistry registry) {

        registry.addDescription(
                new ItemStack(Item.WOODEN_DOOR),
                "description.alwaysmoreitemstest.wooden.door.1", // actually 2 lines
                "description.alwaysmoreitemstest.wooden.door.2",
                "description.alwaysmoreitemstest.wooden.door.3"
        );

        registry.addRecipeCategories(new DebugRecipeCategory());
        registry.addRecipeHandlers(new DebugRecipeHandler());
        registry.addRecipes(Arrays.asList(
                new DebugRecipe(),
                new DebugRecipe()
        ));
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

    }
}
