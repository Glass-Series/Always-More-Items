package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.CraftingGridHelper;
import net.glasslauncher.mods.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.ICraftingGridHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IGuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;

import javax.annotation.*;

public class CraftingRecipeCategory implements IRecipeCategory {

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;
	@Nonnull
	private final ICraftingGridHelper craftingGridHelper;

	public CraftingRecipeCategory() {
		background = DrawableHelper.createDrawable("/gui/crafting.png", 29, 16, 116, 54);
		localizedName = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.craftingTable");
		craftingGridHelper = new CraftingGridHelper(craftInputSlot1, craftOutputSlot);
	}

	@Override
	@Nonnull
	public String getUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	@Nonnull
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(craftOutputSlot, false, 94, 18);

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = craftInputSlot1 + x + (y * 3);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		}

		if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
			IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, wrapper.getInputs(), wrapper.getWidth(), wrapper.getHeight());
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs());
		} else if (recipeWrapper instanceof ICraftingRecipeWrapper) {
			ICraftingRecipeWrapper wrapper = (ICraftingRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, wrapper.getInputs());
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs());
		} else {
			AlwaysMoreItems.LOGGER.error("RecipeWrapper is not a known crafting wrapper type: {}", recipeWrapper);
		}
	}

}
