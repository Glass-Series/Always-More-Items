package net.glasslauncher.alwaysmoreitems.util;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferInfo;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferRegistry;
import net.glasslauncher.alwaysmoreitems.transfer.BasicRecipeTransferHandler;
import net.glasslauncher.alwaysmoreitems.transfer.BasicRecipeTransferInfo;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.*;
import java.util.*;

public class RecipeTransferRegistry implements IRecipeTransferRegistry {
	private final List<IRecipeTransferHandler> recipeTransferHandlers = new ArrayList<>();

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

		IRecipeTransferInfo recipeTransferHelper = new BasicRecipeTransferInfo(containerClass, recipeCategoryUid, recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount);
		addRecipeTransferHandler(recipeTransferHelper);
	}

	@Override
	public void addRecipeTransferHandler(@Nullable IRecipeTransferInfo recipeTransferInfo) {
		if (recipeTransferInfo == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeTransferInfo", new NullPointerException());
			return;
		}
		IRecipeTransferHandler recipeTransferHandler = new BasicRecipeTransferHandler(recipeTransferInfo);
		addRecipeTransferHandler(recipeTransferHandler);
	}

	@Override
	public void addRecipeTransferHandler(@Nullable IRecipeTransferHandler recipeTransferHandler) {
		if (recipeTransferHandler == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeTransferHandler", new NullPointerException());
			return;
		}
		this.recipeTransferHandlers.add(recipeTransferHandler);
	}

	public List<IRecipeTransferHandler> getRecipeTransferHandlers() {
		return recipeTransferHandlers;
	}
}
