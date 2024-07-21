package net.glasslauncher.alwaysmoreitems;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import net.glasslauncher.alwaysmoreitems.api.AMINbt;
import net.glasslauncher.alwaysmoreitems.api.INbtIgnoreList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import javax.annotation.*;
import java.util.*;

public class NbtIgnoreList implements INbtIgnoreList {
	private final Set<String> nbtTagNameBlacklist = new HashSet<>();
	private final HashMultimap<Item, String> itemNbtTagNameBlacklist = HashMultimap.create();

	@Deprecated
	@Override
	public void ignoreNbtTagNames(String... nbtTagNames) {
		Collections.addAll(nbtTagNameBlacklist, nbtTagNames);
	}

	@Override
	public void ignoreNbtTagNames(@Nullable Item item, String... nbtTagNames) {
		if (item == null) {
			AlwaysMoreItems.LOGGER.error("Null item", new NullPointerException());
			return;
		}
		Collection<String> ignoredNbtTagNames = itemNbtTagNameBlacklist.get(item);
		Collections.addAll(ignoredNbtTagNames, nbtTagNames);
	}

	@Override
	public boolean isNbtTagIgnored(@Nullable String nbtTagName) {
		if (nbtTagName == null) {
			AlwaysMoreItems.LOGGER.error("Null nbtTagName", new NullPointerException());
			return false;
		}
		return AMIConfig.nbtBlacklist.contains(nbtTagName) || nbtTagNameBlacklist.contains(nbtTagName);
	}

	@Override
	@Nonnull
	public Set<String> getIgnoredNbtTags(@Nullable Set<String> nbtTagNames) {
		if (nbtTagNames == null) {
			AlwaysMoreItems.LOGGER.error("Null nbtTagNames", new NullPointerException());
			return Collections.emptySet();
		}
		Set<String> ignoredKeysConfig = Sets.intersection(nbtTagNames, new HashSet<>(AMIConfig.nbtBlacklist));
		Set<String> ignoredKeysApi = Sets.intersection(nbtTagNames, nbtTagNameBlacklist);
		return Sets.union(ignoredKeysConfig, ignoredKeysApi);
	}

	@Nullable
	@Override
	public NbtCompound getNbt(@Nullable ItemStack itemStack) {
		if (itemStack == null) {
			AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
			return null;
		}

		NbtCompound nbtTagCompound = itemStack.getStationNbt();
		if (nbtTagCompound == null) {
			return null;
		}

		Set<String> keys = ((AMINbt) nbtTagCompound).always_More_Items$getKeySet();

		Set<String> allIgnoredKeysForItem = itemNbtTagNameBlacklist.get(itemStack.getItem());

		Set<String> ignoredKeysConfig = Sets.intersection(keys, new HashSet<>(AMIConfig.nbtBlacklist));
		Set<String> ignoredKeysApi = Sets.intersection(keys, nbtTagNameBlacklist);
		Set<String> ignoredKeysApiForItem = Sets.intersection(keys, allIgnoredKeysForItem);

		Set<String> ignoredKeys = Sets.union(ignoredKeysConfig, ignoredKeysApi);
		ignoredKeys = Sets.union(ignoredKeys, ignoredKeysApiForItem);

		if (ignoredKeys.isEmpty()) {
			return nbtTagCompound;
		}

		NbtCompound nbtTagCompoundCopy = nbtTagCompound.copy();
		for (String ignoredKey : ignoredKeys) {
			((AMINbt) nbtTagCompoundCopy).always_More_Items$removeTag(ignoredKey);
		}
		return nbtTagCompoundCopy;
	}
}
