package net.glasslauncher.alwaysmoreitems.plugins.ami;
import net.glasslauncher.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipeCategory;
import net.glasslauncher.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipeHandler;

public class AMIPlugin implements IModPlugin {
	private IAMIHelpers amiHelpers;

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
