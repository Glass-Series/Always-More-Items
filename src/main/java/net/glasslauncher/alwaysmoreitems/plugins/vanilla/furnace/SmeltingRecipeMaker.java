package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.alwaysmoreitems.api.SubProvider;
import net.glasslauncher.alwaysmoreitems.api.recipe.IStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;
import net.modificationstation.stationapi.api.tag.TagKey;

import javax.annotation.*;
import java.util.*;

public class SmeltingRecipeMaker {

	@Nonnull
	public static List<SmeltingRecipe> getFurnaceRecipes(IAMIHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
        //noinspection unchecked
        Map<?, ItemStack> smeltingMap = SmeltingRecipeManager.getInstance().getRecipes();

		List<SmeltingRecipe> recipes = new ArrayList<>();

		for (Map.Entry<?, ItemStack> itemStackItemStackEntry : smeltingMap.entrySet()) {

			List<ItemStack> inputs;

			Object objItem = itemStackItemStackEntry.getKey();
			if (objItem instanceof TagKey<?> key) {
                //noinspection unchecked
                Optional<RegistryEntryList.Named<Item>> instanceEntryList = ItemRegistry.INSTANCE.getEntryList((TagKey<Item>) key);
				if (instanceEntryList.isEmpty()) {
					continue;
				}
				inputs = new ArrayList<>();
				instanceEntryList.ifPresent(
						registryEntries -> registryEntries.forEach(itemRegistryEntry -> {
							inputs.addAll(((SubProvider) itemRegistryEntry.value()).getSubItems());
						})
				);
			}
			else if (objItem instanceof ItemStack itemStack) {
				inputs = stackHelper.getSubtypes(itemStack);
			}
			else {
				inputs = Collections.singletonList(new ItemStack((int) objItem, 1, 0));
			}

			ItemStack output = itemStackItemStackEntry.getValue();

			SmeltingRecipe recipe = new SmeltingRecipe(inputs, output);
			recipes.add(recipe);
		}

		return recipes;
	}

}
