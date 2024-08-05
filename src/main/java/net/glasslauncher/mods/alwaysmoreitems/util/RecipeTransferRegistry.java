package net.glasslauncher.mods.alwaysmoreitems.util;

import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferInfo;
import net.glasslauncher.mods.alwaysmoreitems.transfer.BasicRecipeTransferHandler;
import net.glasslauncher.mods.alwaysmoreitems.transfer.BasicRecipeTransferInfo;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.*;
import java.util.*;

public class RecipeTransferRegistry implements net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferRegistry {
	private final List<RecipeTransferHandler> recipeTransferHandlers = new ArrayList<>();

	@Override
	public void addRecipeTransferHandler(@Nullable Class<? extends ScreenHandler> containerClass, @Nullable String recipeCategoryUid, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
		if (containerClass == null) {
			AlwaysMoreItems.LOGGER.error("Null containerClass", new NullPointerException());
			return;
		}
		if (recipeCategoryUid == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeCategoryUid", new NullPointerException());
			return;
		}

		RecipeTransferInfo recipeTransferHelper = new BasicRecipeTransferInfo(containerClass, recipeCategoryUid, recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount);
		addRecipeTransferHandler(recipeTransferHelper);
	}

	@Override
	public void addRecipeTransferHandler(@Nullable RecipeTransferInfo recipeTransferInfo) {
		if (recipeTransferInfo == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeTransferInfo", new NullPointerException());
			return;
		}
		RecipeTransferHandler recipeTransferHandler = new BasicRecipeTransferHandler(recipeTransferInfo);
		addRecipeTransferHandler(recipeTransferHandler);
	}

	@Override
	public void addRecipeTransferHandler(@Nullable RecipeTransferHandler recipeTransferHandler) {
		if (recipeTransferHandler == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeTransferHandler", new NullPointerException());
			return;
		}
		this.recipeTransferHandlers.add(recipeTransferHandler);
	}

	public List<RecipeTransferHandler> getRecipeTransferHandlers() {
		return recipeTransferHandlers;
	}
}
