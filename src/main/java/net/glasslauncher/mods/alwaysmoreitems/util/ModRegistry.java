package net.glasslauncher.mods.alwaysmoreitems.util;

import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferRegistry;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeClickableArea;
import net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description.ItemDescriptionRecipe;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class ModRegistry implements IModRegistry {
	private final List<IRecipeCategory> recipeCategories;
	private final List<IRecipeHandler> recipeHandlers;
	private final List<Object> recipes = new ArrayList<>();
	private final RecipeTransferRegistry recipeTransferRegistry;
	private final Map<Class<? extends HandledScreen>, RecipeClickableArea> recipeClickableAreas;

	public ModRegistry() {
        recipeCategories = new ArrayList<>();
        recipeHandlers = new ArrayList<>();
        recipeTransferRegistry = new RecipeTransferRegistry();
		recipeClickableAreas = new HashMap<>();
    }

	public ModRegistry(ModRegistry oldRegistry) {
		recipeCategories = oldRegistry.recipeCategories;
		recipeHandlers = oldRegistry.recipeHandlers;
        recipeTransferRegistry = oldRegistry.recipeTransferRegistry;
		recipeClickableAreas = oldRegistry.recipeClickableAreas;
    }

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
		if (itemStacks == null || itemStacks.isEmpty()) {
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