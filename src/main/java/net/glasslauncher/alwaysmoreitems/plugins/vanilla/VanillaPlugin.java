package net.glasslauncher.alwaysmoreitems.plugins.vanilla;

import net.glasslauncher.alwaysmoreitems.ContainerFakeWorkbench;
import net.glasslauncher.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferRegistry;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting.ShapedOreRecipeHandler;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting.ShapedRecipesHandler;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting.ShapelessOreRecipeHandler;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting.ShapelessRecipesHandler;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipeHandler;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipeMaker;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace.FurnaceFuelCategory;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace.FurnaceSmeltingCategory;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipeHandler;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipeMaker;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.screen.FurnaceScreenHandler;

public class VanillaPlugin implements IModPlugin {
	private IItemRegistry itemRegistry;
	private IAMIHelpers amiHelpers;

	@Override
	public String getName() {
		return "Vanilla";
	}

	@Override
	public void onAMIHelpersAvailable(IAMIHelpers amiHelpers) {
		this.amiHelpers = amiHelpers;
	}

	@Override
	public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCategories(
				new CraftingRecipeCategory(),
				new FurnaceFuelCategory(),
				new FurnaceSmeltingCategory()
		);

		registry.addRecipeHandlers(
				new ShapedOreRecipeHandler(),
				new ShapedRecipesHandler(),
				new ShapelessOreRecipeHandler(),
				new ShapelessRecipesHandler(),
				new FuelRecipeHandler(),
				new SmeltingRecipeHandler()
		);

		registry.addRecipeClickArea(CraftingScreen.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeClickArea(FurnaceScreen.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		recipeTransferRegistry.addRecipeTransferHandler(ContainerFakeWorkbench.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		recipeTransferRegistry.addRecipeTransferHandler(FurnaceScreenHandler.class, VanillaRecipeCategoryUid.SMELTING, 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(FurnaceScreenHandler.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);

		registry.addRecipes(CraftingRecipeManager.getInstance().getRecipes());
		registry.addRecipes(SmeltingRecipeMaker.getFurnaceRecipes(amiHelpers));
		registry.addRecipes(FuelRecipeMaker.getFuelRecipes(itemRegistry, amiHelpers));
	}

	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

	}
}
