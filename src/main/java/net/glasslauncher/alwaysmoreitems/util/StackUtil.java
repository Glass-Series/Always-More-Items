package net.glasslauncher.alwaysmoreitems.util;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.AMINbt;
import net.glasslauncher.alwaysmoreitems.api.SubProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.*;
import java.util.*;

/**
 * @deprecated Use IAMIHelpers.getStackHelper()
 */
@Deprecated
public class StackUtil {
	private StackUtil() {

	}

	@Deprecated
	@Nonnull
	public static List<ItemStack> removeDuplicateItemStacks(@Nonnull Iterable<ItemStack> stacks) {
		List<ItemStack> newStacks = new ArrayList<>();
		for (ItemStack stack : stacks) {
			if (stack != null && containsStack(newStacks, stack) == null) {
				newStacks.add(stack);
			}
		}
		return newStacks;
	}

	/* Returns an ItemStack from "stacks" if it isIdentical to an ItemStack from "contains" */
	@Deprecated
	@Nullable
	public static ItemStack containsStack(@Nullable Iterable<ItemStack> stacks, @Nullable Iterable<ItemStack> contains) {
		if (stacks == null || contains == null) {
			return null;
		}

		for (ItemStack containStack : contains) {
			ItemStack matchingStack = containsStack(stacks, containStack);
			if (matchingStack != null) {
				return matchingStack;
			}
		}

		return null;
	}

	/* Returns an ItemStack from "stacks" if it isIdentical to "contains" */
	@Deprecated
	@Nullable
	public static ItemStack containsStack(@Nullable Iterable<ItemStack> stacks, @Nullable ItemStack contains) {
		if (stacks == null || contains == null) {
			return null;
		}

		for (ItemStack stack : stacks) {
			if (isIdentical(contains, stack)) {
				return stack;
			}
		}
		return null;
	}

	@Deprecated
	@Nonnull
	public static List<ItemStack> condenseStacks(Collection<ItemStack> stacks) {
		List<ItemStack> condensed = new ArrayList<>();

		for (ItemStack stack : stacks) {
			if (stack == null || stack.count <= 0) {
				continue;
			}

			boolean matched = false;
			for (ItemStack cached : condensed) {
				if (cached.isItemEqual(stack) && ItemStack.areEqual(cached, stack)) {
					cached.count += stack.count;
					matched = true;
				}
			}

			if (!matched) {
				ItemStack cached = stack.copy();
				condensed.add(cached);
			}
		}

		return condensed;
	}

	/**
	 * Counts how many full sets are contained in the passed stock.
	 * Returns a list of matching stacks from set, or null if there aren't enough for a complete match.
	 */
	@Deprecated
	@Nullable
	public static List<ItemStack> containsSets(Collection<ItemStack> required, Collection<ItemStack> offered) {
		int totalSets = 0;

		List<ItemStack> matching = new ArrayList<>();
		List<ItemStack> condensedRequired = condenseStacks(required);
		List<ItemStack> condensedOffered = condenseStacks(offered);

		for (ItemStack req : condensedRequired) {
			int reqCount = 0;
			for (ItemStack offer : condensedOffered) {
				if (isIdentical(req, offer)) {
					int stackCount = offer.count / req.count;
					reqCount = Math.max(reqCount, stackCount);
				}
			}

			if (reqCount == 0) {
				return null;
			} else {
				matching.add(req);

				if (totalSets == 0 || totalSets > reqCount) {
					totalSets = reqCount;
				}
			}
		}

		return matching;
	}

	@Deprecated
	public static boolean isIdentical(@Nullable ItemStack lhs, @Nullable ItemStack rhs) {
		if (lhs == rhs) {
			return true;
		}

		if (lhs == null || rhs == null) {
			return false;
		}

		if (lhs.getItem() != rhs.getItem()) {
			return false;
		}

		if (lhs.getDamage() != -1 /*OreDictionary.WILDCARD_VALUE*/) {
			if (lhs.getDamage() != rhs.getDamage()) {
				return false;
			}
		}

		return ItemStack.areEqual(lhs, rhs);
	}

	/**
	 * Returns all the subtypes of itemStack if it has a wildcard meta value.
	 */
	@Deprecated
	@Nonnull
	public static List<ItemStack> getSubtypes(@Nonnull ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item == null) {
			return Collections.emptyList();
		}

		if (itemStack.getDamage() != -1 /*OreDictionary.WILDCARD_VALUE*/) {
			return Collections.singletonList(itemStack);
		}

		return getSubtypes(item, itemStack.count);
	}

	@Deprecated
	@Nonnull
	public static List<ItemStack> getSubtypes(@Nonnull Item item, int stackSize) {
//		List<ItemStack> itemStacks = new ArrayList<>();

//		for (CreativeTabs itemTab : item.getCreativeTabs()) {
//			List<ItemStack> subItems = new ArrayList<>();
//			item.getSubItems(item, itemTab, subItems);
//			for (ItemStack subItem : subItems) {
//				if (subItem.count != stackSize) {
//					ItemStack subItemCopy = subItem.copy();
//					subItemCopy.count = stackSize;
//					itemStacks.add(subItemCopy);
//				} else {
//					itemStacks.add(subItem);
//				}
//			}
//		}

		return ((SubProvider) item).getSubItems().stream().map(itemStack -> {
			ItemStack newStack = itemStack.copy();
			newStack.count = stackSize;
			return newStack;
		}).toList();
	}

	@Deprecated
	public static List<ItemStack> getAllSubtypes(Iterable stacks) {
		List<ItemStack> allSubtypes = new ArrayList<>();
		getAllSubtypes(allSubtypes, stacks);
		return allSubtypes;
	}

	@Deprecated
	private static void getAllSubtypes(List<ItemStack> subtypesList, Iterable stacks) {
		for (Object obj : stacks) {
			if (obj instanceof ItemStack) {
				ItemStack itemStack = (ItemStack) obj;
				List<ItemStack> subtypes = getSubtypes(itemStack);
				subtypesList.addAll(subtypes);
			} else if (obj instanceof Iterable) {
				getAllSubtypes(subtypesList, (Iterable) obj);
			} else if (obj != null) {
				AlwaysMoreItems.LOGGER.error("Unknown object found: {}", obj);
			}
		}
	}

	@Deprecated
	@Nonnull
	public static List<ItemStack> toItemStackList(@Nullable Object stacks) {
		List<ItemStack> itemStacksList = new ArrayList<>();
		toItemStackList(itemStacksList, stacks);
		return removeDuplicateItemStacks(itemStacksList);
	}

	@Deprecated
	private static void toItemStackList(@Nonnull List<ItemStack> itemStackList, @Nullable Object input) {
		if (input instanceof ItemStack) {
			itemStackList.add((ItemStack) input);
		} else if (input instanceof String) {
//			List<ItemStack> stacks = OreDictionary.getOres((String) input);
//			itemStackList.addAll(stacks);
		} else if (input instanceof Iterable) {
			for (Object obj : (Iterable) input) {
				toItemStackList(itemStackList, obj);
			}
		} else if (input != null) {
			AlwaysMoreItems.LOGGER.error("Unknown object found: {}", input);
		}
	}

	@Deprecated
	@Nonnull
	public static String getUniqueIdentifierForStack(@Nonnull ItemStack stack) {
		return getUniqueIdentifierForStack(stack, false);
	}

	@Deprecated
	@Nonnull
	public static String getUniqueIdentifierForStack(@Nonnull ItemStack stack, boolean wildcard) {
		Item item = stack.getItem();
		if (item == null) {
			throw new ItemUidException("Found an itemStack with a null item. This is an error from another mod.");
		}

		Identifier itemName = ItemRegistry.INSTANCE.getId(item);
		if (itemName == null) {
			throw new ItemUidException("No name for item in GameData itemRegistry: " + item.getClass());
		}

		String itemNameString = itemName.toString();
		int metadata = stack.getDamage();
		if (wildcard || metadata == -1 /*OreDictionary.WILDCARD_VALUE*/) {
			return itemNameString;
		}

		StringBuilder itemKey = new StringBuilder(itemNameString);
		if (stack.hasSubtypes()) {
			itemKey.append(':').append(metadata);
		}

		if (!((AMINbt) stack.getStationNbt()).always_More_Items$hasNoTags()) {
			NbtCompound nbtTagCompound = AlwaysMoreItems.getHelpers().getNbtIgnoreList().getNbt(stack);
			if (nbtTagCompound != null && !((AMINbt) nbtTagCompound).always_More_Items$hasNoTags()) {
				itemKey.append(':').append(nbtTagCompound);
			}
		}

		return itemKey.toString();
	}

	@Deprecated
	@Nonnull
	public static List<String> getUniqueIdentifiersWithWildcard(@Nonnull ItemStack itemStack) {
		String uid = getUniqueIdentifierForStack(itemStack, false);
		String uidWild = getUniqueIdentifierForStack(itemStack, true);

		if (uid.equals(uidWild)) {
			return Collections.singletonList(uid);
		} else {
			return Arrays.asList(uid, uidWild);
		}
	}

	@Deprecated
	public static int addStack(ScreenHandler container, Collection<Integer> slotIndexes, ItemStack stack, boolean doAdd) {
		int added = 0;
		// Add to existing stacks first
		for (Integer slotIndex : slotIndexes) {
			Slot slot = container.getSlot(slotIndex);
			if (slot == null) {
				continue;
			}

			ItemStack inventoryStack = slot.getStack();
			if (inventoryStack == null || inventoryStack.getItem() == null) {
				continue;
			}

			// Already occupied by different item, skip this slot.
			if (!inventoryStack.isStackable() || !inventoryStack.isItemEqual(stack) || !ItemStack.areEqual(inventoryStack, stack)) {
				continue;
			}

			int remain = stack.count - added;
			int space = inventoryStack.getMaxCount() - inventoryStack.count;
			if (space <= 0) {
				continue;
			}

			// Enough space
			if (space >= remain) {
				if (doAdd) {
					inventoryStack.count += remain;
				}
				return stack.count;
			}

			// Not enough space
			if (doAdd) {
				inventoryStack.count = inventoryStack.getMaxCount();
			}

			added += space;
		}

		if (added >= stack.count) {
			return added;
		}

		for (Integer slotIndex : slotIndexes) {
			Slot slot = container.getSlot(slotIndex);
			if (slot == null) {
				continue;
			}

			ItemStack inventoryStack = slot.getStack();
			if (inventoryStack != null) {
				continue;
			}

			if (doAdd) {
				ItemStack stackToAdd = stack.copy();
				stackToAdd.count = stack.count - added;
				slot.setStack(stackToAdd);
			}
			return stack.count;
		}

		return added;
	}
}
