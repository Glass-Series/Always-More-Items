package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.CraftingGridHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.CraftingRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.wrapper.ShapedCraftingRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;

import javax.annotation.Nonnull;

public class CraftingRecipeCategory implements RecipeCategory {

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;

	@Nonnull
	private final AMIDrawable background;
	@Nonnull
	private final String localizedName;
	@Nonnull
	private final CraftingGridHelper craftingGridHelper;

	public CraftingRecipeCategory() {
		background = DrawableHelper.createDrawable("/gui/crafting.png", 29, 16, 116, 54);
		localizedName = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.craftingTable");
		craftingGridHelper = new net.glasslauncher.mods.alwaysmoreitems.recipe.CraftingGridHelper(craftInputSlot1, craftOutputSlot);
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
	public AMIDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull RecipeWrapper recipeWrapper) {
		GuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(craftOutputSlot, false, 94, 18);

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = craftInputSlot1 + x + (y * 3);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		}

		if (recipeWrapper instanceof ShapedCraftingRecipeWrapper) {
			ShapedCraftingRecipeWrapper wrapper = (ShapedCraftingRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, wrapper.getInputs(), wrapper.getWidth(), wrapper.getHeight());
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs());
		} else if (recipeWrapper instanceof CraftingRecipeWrapper) {
			CraftingRecipeWrapper wrapper = (CraftingRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, wrapper.getInputs());
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs());
		} else {
			AlwaysMoreItems.LOGGER.error("RecipeWrapper is not a known crafting wrapper type: {}", recipeWrapper);
		}
	}

}
