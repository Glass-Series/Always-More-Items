package net.glasslauncher.alwaysmoreitems;

import javax.annotation.Nonnull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.alwaysmoreitems.transfer.RecipeTransferHandlerHelper;
import net.glasslauncher.alwaysmoreitems.util.StackHelper;
import net.glasslauncher.alwaysmoreitems.api.IJeiHelpers;

public class JeiHelpers implements IJeiHelpers {
	private final StackHelper stackHelper;
	private final ItemBlacklist itemBlacklist;
	private final NbtIgnoreList nbtIgnoreList;
	private final RecipeTransferHandlerHelper recipeTransferHandlerHelper;

	public JeiHelpers() {
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
