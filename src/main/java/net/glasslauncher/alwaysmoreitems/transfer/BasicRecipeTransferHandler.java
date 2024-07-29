package net.glasslauncher.alwaysmoreitems.transfer;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.gui.IGuiItemStackGroup;
import net.glasslauncher.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferInfo;
import net.glasslauncher.alwaysmoreitems.gui.widget.ingredients.IGuiIngredient;
import net.glasslauncher.alwaysmoreitems.util.StackHelper;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import javax.annotation.*;
import java.util.*;

public class BasicRecipeTransferHandler implements IRecipeTransferHandler {
	@Nonnull
	private final IRecipeTransferInfo transferHelper;

	public BasicRecipeTransferHandler(@Nonnull IRecipeTransferInfo transferHelper) {
		this.transferHelper = transferHelper;
	}

	@Override
	public Class<? extends ScreenHandler> getContainerClass() {
		return transferHelper.getContainerClass();
	}

	@Override
	public String getRecipeCategoryUid() {
		return transferHelper.getRecipeCategoryUid();
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(@Nonnull ScreenHandler container, @Nonnull IRecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
		IRecipeTransferHandlerHelper handlerHelper = AlwaysMoreItems.getHelpers().recipeTransferHandlerHelper();
		StackHelper stackHelper = AlwaysMoreItems.getStackHelper();

		if (!AlwaysMoreItems.isAMIOnServer()) {
			return handlerHelper.createInternalError();
		}

		Map<Integer, Slot> inventorySlots = new HashMap<>();
		for (Slot slot : transferHelper.getInventorySlots(container)) {
			inventorySlots.put(slot.id, slot);
		}

		Map<Integer, Slot> craftingSlots = new HashMap<>();
		for (Slot slot : transferHelper.getRecipeSlots(container)) {
			craftingSlots.put(slot.id, slot);
		}

		int inputCount = 0;
		IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
		for (IGuiIngredient<ItemStack> ingredient : itemStackGroup.getGuiIngredients().values()) {
			if (ingredient.isInput() && !ingredient.getAll().isEmpty()) {
				inputCount++;
			}
		}

		if (inputCount > craftingSlots.size()) {
			AlwaysMoreItems.LOGGER.error("Recipe Transfer helper {} does not work for container {}", transferHelper.getClass(), container.getClass());
			return handlerHelper.createInternalError();
		}

		List<ItemStack> availableItemStacks = new ArrayList<>();
		int filledCraftSlotCount = 0;
		int emptySlotCount = 0;

		for (Slot slot : craftingSlots.values()) {
			if (slot.hasStack()) {
				if (slot.takeStack(0) == null) { // TODO: Do this better.
					AlwaysMoreItems.LOGGER.error("Recipe Transfer helper {} does not work for container {}. Player can't move item out of Crafting Slot number {}", transferHelper.getClass(), container.getClass(), slot.id);
					return handlerHelper.createInternalError();
				}
				filledCraftSlotCount++;
				availableItemStacks.add(slot.getStack().copy());
			}
		}

		for (Slot slot : inventorySlots.values()) {
			if (slot.hasStack()) {
				availableItemStacks.add(slot.getStack().copy());
			} else {
				emptySlotCount++;
			}
		}

		// check if we have enough inventory space to shuffle items around to their final locations
		if (filledCraftSlotCount - inputCount > emptySlotCount) {
			String message = TranslationStorage.getInstance().get("alwaysmoreitems.tooltip.error.recipe.transfer.inventory.full");
			return handlerHelper.createUserErrorWithTooltip(message);
		}

		StackHelper.MatchingItemsResult matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks, itemStackGroup.getGuiIngredients());

		if (!matchingItemsResult.missingItems.isEmpty()) {
			String message = TranslationStorage.getInstance().get("alwaysmoreitems.tooltip.error.recipe.transfer.missing");
			return handlerHelper.createUserErrorForSlots(message, matchingItemsResult.missingItems);
		}

		List<Integer> craftingSlotIndexes = new ArrayList<>(craftingSlots.keySet());
		Collections.sort(craftingSlotIndexes);

		List<Integer> inventorySlotIndexes = new ArrayList<>(inventorySlots.keySet());
		Collections.sort(inventorySlotIndexes);

		// check that the slots exist and can be altered
		for (Map.Entry<Integer, ItemStack> entry : matchingItemsResult.matchingItems.entrySet()) {
			int craftNumber = entry.getKey();
			int slotNumber = craftingSlotIndexes.get(craftNumber);
			if (slotNumber >= container.slots.size()) {
				AlwaysMoreItems.LOGGER.error("Recipes Transfer Helper {} references slot {} outside of the inventory's size {}", transferHelper.getClass(), slotNumber, container.slots.size());
				return handlerHelper.createInternalError();
			}
			Slot slot = container.getSlot(slotNumber);
			ItemStack stack = entry.getValue();
			if (slot == null) {
				AlwaysMoreItems.LOGGER.error("The slot number {} does not exist in the container.", slotNumber);
				return handlerHelper.createInternalError();
			}
			if (!slot.canInsert(stack)) {
				AlwaysMoreItems.LOGGER.error("The ItemStack {} is not valid for the slot number {}", stack, slotNumber);
				return handlerHelper.createInternalError();
			}
		}

//		if (doTransfer) {
//			PacketRecipeTransfer packet = new PacketRecipeTransfer(matchingItemsResult.matchingItems, craftingSlotIndexes, inventorySlotIndexes, maxTransfer);
//			PacketHelper.send(packet);
//		}

		return null;
	}

	public static void setItems(@Nonnull PlayerEntity player, @Nonnull Map<Integer, ItemStack> slotMap, @Nonnull List<Integer> craftingSlots, @Nonnull List<Integer> inventorySlots, boolean maxTransfer) {
		ScreenHandler container = player.container;
		StackHelper stackHelper = AlwaysMoreItems.getStackHelper();

		// remove required recipe items
		int removedSets = removeSetsFromInventory(container, slotMap.values(), craftingSlots, inventorySlots, maxTransfer);
		if (removedSets == 0) {
			return;
		}

		// clear the crafting grid
		List<ItemStack> clearedCraftingItems = new ArrayList<>();
		for (Integer craftingSlotNumber : craftingSlots) {
			Slot craftingSlot = container.getSlot(craftingSlotNumber);
			if (craftingSlot != null && craftingSlot.hasStack()) {
				ItemStack craftingItem = craftingSlot.takeStack(Integer.MAX_VALUE);
				clearedCraftingItems.add(craftingItem);
			}
		}

		// put items into the crafting grid
		for (Map.Entry<Integer, ItemStack> entry : slotMap.entrySet()) {
			ItemStack stack = entry.getValue();
			if (stack.isStackable()) {
				int maxSets = stack.getMaxCount() / stack.count;
				stack.count *= Math.min(maxSets, removedSets);
			}
			Integer craftNumber = entry.getKey();
			Integer slotNumber = craftingSlots.get(craftNumber);
			Slot slot = container.getSlot(slotNumber);
			slot.setStack(stack);
		}

		// put cleared items back into the player's inventory
		for (ItemStack oldCraftingItem : clearedCraftingItems) {
			stackHelper.addStack(container, inventorySlots, oldCraftingItem, true);
		}

		container.sendContentUpdates();
	}

	private static int removeSetsFromInventory(@Nonnull ScreenHandler container, @Nonnull Collection<ItemStack> required, @Nonnull List<Integer> craftingSlots, @Nonnull List<Integer> inventorySlots, boolean maxTransfer) {
		if (maxTransfer) {
            List<ItemStack> requiredCopy = new ArrayList<>(required);

			int removedSets = 0;
			while (!requiredCopy.isEmpty() && removeSetsFromInventory(container, requiredCopy, craftingSlots, inventorySlots)) {
				removedSets++;
				Iterator<ItemStack> iterator = requiredCopy.iterator();
				while (iterator.hasNext()) {
					ItemStack stack = iterator.next();
					if (!stack.isStackable() || (stack.count * (removedSets + 1) > stack.getMaxCount())) {
						iterator.remove();
					}
				}
			}
			return removedSets;
		} else {
			boolean success = removeSetsFromInventory(container, required, craftingSlots, inventorySlots);
			return success ? 1 : 0;
		}
	}

	private static boolean removeSetsFromInventory(@Nonnull ScreenHandler container, @Nonnull Iterable<ItemStack> required, @Nonnull List<Integer> craftingSlots, @Nonnull List<Integer> inventorySlots) {
		final Map<Slot, ItemStack> originalSlotContents = new HashMap<>();

		for (ItemStack matchingStack : required) {
			final ItemStack requiredStack = matchingStack.copy();
			while (requiredStack.count > 0) {
				final Slot slot = getSlotWithStack(container, requiredStack, craftingSlots, inventorySlots);
				if (slot == null) {
					// abort! put removed items back where the came from
					for (Map.Entry<Slot, ItemStack> slotEntry : originalSlotContents.entrySet()) {
						ItemStack stack = slotEntry.getValue();
						slotEntry.getKey().setStack(stack);
					}
					return false;
				}

				if (!originalSlotContents.containsKey(slot)) {
					originalSlotContents.put(slot, slot.getStack().copy());
				}

				ItemStack removed = slot.takeStack(requiredStack.count);
				requiredStack.count -= removed.count;
			}
		}

		return true;
	}

	private static Slot getSlotWithStack(@Nonnull ScreenHandler container, @Nonnull ItemStack stack, @Nonnull List<Integer> craftingSlots, @Nonnull List<Integer> inventorySlots) {
		StackHelper stackHelper = AlwaysMoreItems.getStackHelper();

		Slot slot = stackHelper.getSlotWithStack(container, craftingSlots, stack);
		if (slot == null) {
			slot = stackHelper.getSlotWithStack(container, inventorySlots, stack);
		}

		return slot;
	}
}
