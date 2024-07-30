package net.glasslauncher.mods.alwaysmoreitems;

import net.glasslauncher.mods.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.transfer.RecipeTransferHandlerHelper;
import net.glasslauncher.mods.alwaysmoreitems.util.StackHelper;

import javax.annotation.*;

public class AMIHelpers implements IAMIHelpers {
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
