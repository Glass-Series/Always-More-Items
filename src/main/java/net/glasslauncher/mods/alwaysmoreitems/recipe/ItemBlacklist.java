package net.glasslauncher.mods.alwaysmoreitems.recipe;

import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class ItemBlacklist implements net.glasslauncher.mods.alwaysmoreitems.api.ItemBlacklist {
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
			if (itemBlacklist.contains(uid) || AMIConfig.INSTANCE.itemBlacklist.contains(uid)) {
				return true;
			}
		}
		return false;
	}
}
