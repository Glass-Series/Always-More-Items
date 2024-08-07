package net.glasslauncher.mods.alwaysmoreitems.util;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.*;
import java.util.*;

/**
 * For getting properties of ItemStacks efficiently
 */
public class ItemStackElement {
	@Nonnull
	private final ItemStack itemStack;
	@Nonnull
	private final String searchString;
	@Nonnull
	private final String modName;

	@Nullable
	public static ItemStackElement create(@Nonnull ItemStack itemStack) {
		try {
			return new ItemStackElement(itemStack);
		} catch (RuntimeException e) {
			AlwaysMoreItems.LOGGER.warn("Found broken itemStack.", e);
			return null;
		}
	}

	private ItemStackElement(@Nonnull ItemStack itemStack) {
		this.itemStack = itemStack;

		Identifier itemResourceLocation = ItemRegistry.INSTANCE.getId(itemStack.getItem());
		if (itemResourceLocation == null) {
			throw new NullPointerException("Null item id for #" + itemStack.getItem().id);
		}
		String modId = itemResourceLocation.getNamespace().toString().toLowerCase(Locale.ENGLISH);

		String modName = itemResourceLocation.getNamespace().getName().toLowerCase(Locale.ENGLISH);

		String displayName = TranslationStorage.getInstance().getClientTranslation(itemStack.getTranslationKey());
		if (displayName == null) {
			throw new NullPointerException("No display name for item. " + itemResourceLocation + ' ' + itemStack.getItem().getClass());
		}

		String searchString = displayName.toLowerCase();

		this.modName = modId + ' ' + modName;

//		if (Config.isAtPrefixRequiredForModName()) {
			this.searchString = searchString;
//		} else {
//			this.searchString = searchString + ' ' + this.modName;
//		}
	}

	@Nonnull
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Nonnull
	public String getSearchString() {
		return searchString;
	}

	@Nonnull
	public String getModName() {
		return modName;
	}
}
