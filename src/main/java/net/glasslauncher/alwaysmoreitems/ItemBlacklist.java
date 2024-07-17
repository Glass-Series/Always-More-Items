package net.glasslauncher.alwaysmoreitems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.glasslauncher.alwaysmoreitems.api.IItemBlacklist;
import net.minecraft.item.ItemStack;

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
			if (itemBlacklist.contains(uid) || AMIConfig.INSTANCE.itemBlacklist.contains(uid)) {
				return true;
			}
		}
		return false;
	}
}
