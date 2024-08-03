package net.glasslauncher.mods.alwaysmoreitems.plugins.ami;

import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMISyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.mods.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

public class AMIPlugin implements IModPlugin {
	public static final Identifier ID = AlwaysMoreItems.NAMESPACE.id("always_more_items");

    @Override
	public String getName() {
		return "AMI";
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public void onAMIHelpersAvailable(IAMIHelpers amiHelpers) {
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
	}

	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

	}

	@Override
	public IAMISyncableRecipe deserializeRecipe(NbtCompound recipe) {
		return new ItemDescriptionRecipe(Arrays.asList(VanillaPlugin.parseInputs(recipe.getList("output"))), nbtListToArrayList(recipe.getList("description")));
	}

	public static <T> ArrayList<T> nbtListToArrayList(NbtList nbtList) {
		ArrayList<T> arrayList = new ArrayList<>(nbtList.size());
		for (int i = 0; i < nbtList.size(); i++) {
            //noinspection unchecked Good. Crash.
            arrayList.add((T) nbtList.get(i));
		}
		return arrayList;
	}
}
