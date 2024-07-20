package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.alwaysmoreitems.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;

import javax.annotation.*;
import java.util.*;

public class FuelRecipeMaker {

	@Nonnull
	public static List<FuelRecipe> getFuelRecipes(IItemRegistry itemRegistry, IAMIHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		List<ItemStack> fuelStacks = itemRegistry.getFuels();
		List<FuelRecipe> fuelRecipes = new ArrayList<>(fuelStacks.size());
		for (ItemStack fuelStack : fuelStacks) {
			if (fuelStack == null) {
				continue;
			}

			List<ItemStack> fuels = stackHelper.getSubtypes(fuelStack);
			removeNoBurnTime(fuels);
			if (fuels.isEmpty()) {
				continue;
			}
			int burnTime = getBurnTime(fuels.get(0));
			fuelRecipes.add(new FuelRecipe(fuels, burnTime));
		}
		return fuelRecipes;
	}

	private static void removeNoBurnTime(Collection<ItemStack> itemStacks) {
        itemStacks.removeIf(itemStack -> getBurnTime(itemStack) == 0);
	}

	private static int getBurnTime(ItemStack itemStack) {
		return FuelRegistry.getFuelTime(itemStack);
	}
}
