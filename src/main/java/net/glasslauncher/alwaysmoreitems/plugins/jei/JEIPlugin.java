package net.glasslauncher.alwaysmoreitems.plugins.jei;
import net.glasslauncher.alwaysmoreitems.AMIConfig;
import net.glasslauncher.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.alwaysmoreitems.api.IJeiHelpers;
import net.glasslauncher.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.alwaysmoreitems.plugins.jei.debug.DebugRecipe;
import net.glasslauncher.alwaysmoreitems.plugins.jei.debug.DebugRecipeCategory;
import net.glasslauncher.alwaysmoreitems.plugins.jei.debug.DebugRecipeHandler;
import net.glasslauncher.alwaysmoreitems.plugins.jei.description.ItemDescriptionRecipeCategory;
import net.glasslauncher.alwaysmoreitems.plugins.jei.description.ItemDescriptionRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

@net.glasslauncher.alwaysmoreitems.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
	private IJeiHelpers jeiHelpers;

	@Override
	public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

	}

	@Override
	public void register(IModRegistry registry) {

		registry.addRecipeCategories(
				new ItemDescriptionRecipeCategory()
		);

		registry.addRecipeHandlers(
				new ItemDescriptionRecipeHandler()
		);

		if (AMIConfig.isDebugModeEnabled()) {
			registry.addDescription(
                            new ItemStack(Item.WOODEN_DOOR),
					"description.jei.wooden.door.1", // actually 2 lines
					"description.jei.wooden.door.2",
					"description.jei.wooden.door.3"
			);

			registry.addRecipeCategories(new DebugRecipeCategory());
			registry.addRecipeHandlers(new DebugRecipeHandler());
			registry.addRecipes(Arrays.asList(
					new DebugRecipe(),
					new DebugRecipe()
			));
		}
	}

	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

	}
}
