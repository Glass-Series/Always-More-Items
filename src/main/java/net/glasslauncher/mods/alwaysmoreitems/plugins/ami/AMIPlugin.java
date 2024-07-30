package net.glasslauncher.mods.alwaysmoreitems.plugins.ami;

import net.glasslauncher.mods.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.mods.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipeHandler;

public class AMIPlugin implements IModPlugin {
	private IAMIHelpers amiHelpers;

	@Override
	public String getName() {
		return "AMI";
	}

	@Override
	public void onAMIHelpersAvailable(IAMIHelpers amiHelpers) {
		this.amiHelpers = amiHelpers;
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
}
