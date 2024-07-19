package net.glasslauncher.alwaysmoreitems.util;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.RecipeRegistry;
import net.glasslauncher.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferRegistry;
import net.glasslauncher.alwaysmoreitems.gui.RecipeClickableArea;
import net.glasslauncher.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipe;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class ModRegistry implements IModRegistry {
	private final List<IRecipeCategory> recipeCategories = new ArrayList<>();
	private final List<IRecipeHandler> recipeHandlers = new ArrayList<>();
	private final List<Object> recipes = new ArrayList<>();
	private final RecipeTransferRegistry recipeTransferRegistry = new RecipeTransferRegistry();
	private final Map<Class<? extends HandledScreen>, RecipeClickableArea> recipeClickableAreas = new HashMap<>();

	@Override
	public void addRecipeCategories(IRecipeCategory... recipeCategories) {
		Collections.addAll(this.recipeCategories, recipeCategories);
	}

	@Override
	public void addRecipeHandlers(IRecipeHandler... recipeHandlers) {
		Collections.addAll(this.recipeHandlers, recipeHandlers);
	}

	@Override
	public void addRecipes(List recipes) {
		if (recipes != null) {
			this.recipes.addAll(recipes);
		}
	}

	@Override
	public void addRecipeClickArea(@Nonnull Class<? extends HandledScreen> guiClass, int xPos, int yPos, int width, int height, @Nonnull String... recipeCategoryUids) {
		RecipeClickableArea recipeClickableArea = new RecipeClickableArea(yPos, yPos + height, xPos, xPos + width, recipeCategoryUids);
		this.recipeClickableAreas.put(guiClass, recipeClickableArea);
	}

	@Override
	public void addDescription(List<ItemStack> itemStacks, String... descriptionKeys) {
		if (itemStacks == null || itemStacks.size() == 0) {
			IllegalArgumentException e = new IllegalArgumentException();
			AlwaysMoreItems.LOGGER.error("Tried to add description with no itemStacks.", e);
			return;
		}
		if (descriptionKeys.length == 0) {
			IllegalArgumentException e = new IllegalArgumentException();
			AlwaysMoreItems.LOGGER.error("Tried to add an empty list of descriptionKeys for itemStacks {}.", itemStacks, e);
			return;
		}
		List<ItemDescriptionRecipe> recipes = ItemDescriptionRecipe.create(itemStacks, descriptionKeys);
		this.recipes.addAll(recipes);
	}

	@Override
	public void addDescription(ItemStack itemStack, String... descriptionKeys) {
		addDescription(Collections.singletonList(itemStack), descriptionKeys);
	}

	@Override
	public IRecipeTransferRegistry getRecipeTransferRegistry() {
		return recipeTransferRegistry;
	}

	@Nonnull
	public RecipeRegistry createRecipeRegistry() {
		List<IRecipeTransferHandler> recipeTransferHandlers = recipeTransferRegistry.getRecipeTransferHandlers();
		return new RecipeRegistry(recipeCategories, recipeHandlers, recipeTransferHandlers, recipes, recipeClickableAreas);
	}
}
