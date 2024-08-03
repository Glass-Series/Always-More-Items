package net.glasslauncher.mods.alwaysmoreitems;

import net.glasslauncher.mods.alwaysmoreitems.api.IItemBlacklist;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class ItemBlacklist implements IItemBlacklist {
	@Nonnull
	private final Set<String> itemBlacklist = new HashSet<>();

	@Override
	public void addItemToBlacklist(@Nullable ItemStack itemStack) {
		if (itemStack == null) {
			AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
			return;
		}
		String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack);
		itemBlacklist.add(uid);

		AlwaysMoreItems.resetItemFilter();
	}

	@Override
	public void removeItemFromBlacklist(@Nullable ItemStack itemStack) {
		if (itemStack == null) {
			AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
			return;
		}
		String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack);
		itemBlacklist.remove(uid);

		AlwaysMoreItems.resetItemFilter();
	}

	@Override
	public boolean isItemBlacklisted(@Nullable ItemStack itemStack) {
		if (itemStack == null) {
			AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
			return false;
		}
		List<String> uids = AlwaysMoreItems.getStackHelper().getUniqueIdentifiersWithWildcard(itemStack);
		for (String uid : uids) {
			if (itemBlacklist.contains(uid) || AMIConfig.itemBlacklist.contains(uid)) {
				return true;
			}
		}
		return false;
	}
}