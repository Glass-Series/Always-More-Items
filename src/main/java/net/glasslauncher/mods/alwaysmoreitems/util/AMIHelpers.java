package net.glasslauncher.mods.alwaysmoreitems.util;

import net.glasslauncher.mods.alwaysmoreitems.recipe.ItemBlacklist;
import net.glasslauncher.mods.alwaysmoreitems.recipe.NbtIgnoreList;
import net.glasslauncher.mods.alwaysmoreitems.transfer.RecipeTransferHandlerHelper;

import javax.annotation.Nonnull;

public class AMIHelpers implements net.glasslauncher.mods.alwaysmoreitems.api.AMIHelpers {
	private final StackHelper stackHelper;
	private final ItemBlacklist itemBlacklist;
	private final NbtIgnoreList nbtIgnoreList;
	private final RecipeTransferHandlerHelper recipeTransferHandlerHelper;

	public AMIHelpers() {
		this.stackHelper = new StackHelper();
		this.itemBlacklist = new ItemBlacklist();
		this.nbtIgnoreList = new NbtIgnoreList();
		this.recipeTransferHandlerHelper = new RecipeTransferHandlerHelper();
	}

	@Nonnull
	@Override
	public StackHelper getStackHelper() {
		return stackHelper;
	}

	@Nonnull
	@Override
	public ItemBlacklist getItemBlacklist() {
		return itemBlacklist;
	}

	@Nonnull
	@Override
	public NbtIgnoreList getNbtIgnoreList() {
		return nbtIgnoreList;
	}

	@Nonnull
	@Override
	public RecipeTransferHandlerHelper recipeTransferHandlerHelper() {
		return recipeTransferHandlerHelper;
	}

	@Override
	public void reload() {

	}
}
