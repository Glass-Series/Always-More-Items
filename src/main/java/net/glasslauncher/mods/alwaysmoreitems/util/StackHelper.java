package net.glasslauncher.mods.alwaysmoreitems.util;

import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.api.AMINbt;
import net.glasslauncher.mods.alwaysmoreitems.api.SubItemHelper;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.IGuiIngredient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.block.MetaNamedBlockItemProvider;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public class StackHelper implements net.glasslauncher.mods.alwaysmoreitems.api.recipe.StackHelper {
    /**
     * Returns a list of items in slots that complete the recipe defined by requiredStacksList.
     * Returns a result that contains missingItems if there are not enough items in availableItemStacks.
     */
    @Nonnull
    public MatchingItemsResult getMatchingItems(@Nonnull List<ItemStack> availableItemStacks, @Nonnull Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredientsMap) {
        MatchingItemsResult matchingItemResult = new MatchingItemsResult();

        int recipeSlotNumber = -1;
        SortedSet<Integer> keys = new TreeSet<>(ingredientsMap.keySet());
        for (Integer key : keys) {
            IGuiIngredient<ItemStack> ingredient = ingredientsMap.get(key);
            if (!ingredient.isInput()) {
                continue;
            }
            recipeSlotNumber++;

            List<ItemStack> requiredStacks = ingredient.getAll();
            if (requiredStacks.isEmpty()) {
                continue;
            }

            ItemStack matching = containsStack(availableItemStacks, requiredStacks);
            if (matching == null) {
                matchingItemResult.missingItems.add(key);
            } else {
                ItemStack matchingSplit = matching.split(1);
                if (matching.count == 0) {
                    availableItemStacks.remove(matching);
                }
                matchingItemResult.matchingItems.put(recipeSlotNumber, matchingSplit);
            }
        }

        return matchingItemResult;
    }

    @Nullable
    public Slot getSlotWithStack(@Nonnull ScreenHandler container, @Nonnull Iterable<Integer> slotNumbers, @Nonnull ItemStack stack) {
        StackHelper stackHelper = AlwaysMoreItems.getStackHelper();

        for (Integer slotNumber : slotNumbers) {
            Slot slot = container.getSlot(slotNumber);
            if (slot != null) {
                ItemStack slotStack = slot.getStack();
                if (stackHelper.isIdentical(stack, slotStack)) {
                    return slot;
                }
            }
        }
        return null;
    }

    @Nonnull
    public List<ItemStack> removeDuplicateItemStacks(@Nonnull Iterable<ItemStack> stacks) {
        List<ItemStack> newStacks = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null && containsStack(newStacks, stack) == null) {
                newStacks.add(stack);
            }
        }
        return newStacks;
    }

    /* Returns an ItemStack from "stacks" if it isIdentical to an ItemStack from "contains" */
    @Nullable
    public ItemStack containsStack(@Nullable Iterable<ItemStack> stacks, @Nullable Iterable<ItemStack> contains) {
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
    @Nullable
    public ItemStack containsStack(@Nullable Iterable<ItemStack> stacks, @Nullable ItemStack contains) {
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

    public boolean isIdentical(@Nullable ItemStack lhs, @Nullable ItemStack rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        if (lhs.getItem() != rhs.getItem()) {
            return false;
        }

        if (lhs.getDamage() != -1/*OreDictionary.WILDCARD_VALUE*/) {
            if (lhs.getDamage() != rhs.getDamage()) {
                return false;
            }
        }

        return lhs.isItemEqual(rhs);
    }

    @Override
    @Nonnull
    public List<ItemStack> getSubtypes(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
            return Collections.emptyList();
        }

        Item item = itemStack.getItem();
        if (item == null) {
            AlwaysMoreItems.LOGGER.error("Null item in itemStack", new NullPointerException());
            return Collections.emptyList();
        }

        if (itemStack.getDamage() != -1/*OreDictionary.WILDCARD_VALUE*/) {
            return new ArrayList<>() {
                {
                    add(itemStack);
                }
            };
        }

        return getSubtypes(item, itemStack.count);
    }

    @Nonnull
    public List<ItemStack> getSubtypes(@Nonnull Item item, int stackSize) {
//        List<ItemStack> itemStacks = new ArrayList<>();

//        for (CreativeTabs itemTab : item.getCreativeTabs()) {
//            List<ItemStack> subItems = new ArrayList<>();
//            item.getSubItems(item, itemTab, subItems);
//            for (ItemStack subItem : subItems) {
//                if (subItem.stackSize != stackSize) {
//                    ItemStack subItemCopy = subItem.copy();
//                    subItemCopy.stackSize = stackSize;
//                    itemStacks.add(subItemCopy);
//                } else {
//                    itemStacks.add(subItem);
//                }
//            }
//        }

        // Try to get the mod defined sub items
        List<ItemStack> subItems = SubItemHelper.getSubItems(item);

        // If mod has defined sub items, use those
        if (subItems != null && !subItems.isEmpty()) {
            subItems = subItems.stream().peek(itemStack -> itemStack.count = stackSize).toList();
        }

        // If mod hasnt defined sub items, look for them ourselves
        else {
            // Create a list for sub items
            subItems = new ArrayList<>();
            List<String> keyCache = new ArrayList<>();

            // If on server, we dont care, generate first item and return
            if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
                subItems.add(new ItemStack(item, stackSize, 0));
                return subItems;
            }

            // If item is a BlockItem and that BlockItem has MetaNamedBlockItemProvider, use that and return
            if (item instanceof BlockItem blockItem) {
                // StationAPI interface
                if (blockItem.getBlock() instanceof MetaNamedBlockItemProvider metaBlockItemProvider) {
                    for (int i = 0; i < metaBlockItemProvider.getValidMetas().length; i++) {
                        subItems.add(new ItemStack(item, stackSize, metaBlockItemProvider.getValidMetas()[i]));
                    }
                    return subItems;
                }

                // StationAPI annotation
                if (blockItem.getBlock() instanceof HasMetaNamedBlockItem blockItemWithMeta) {
                    for (int i = 0; i < blockItemWithMeta.validMetas().length; i++) {
                        subItems.add(new ItemStack(item, stackSize, blockItemWithMeta.validMetas()[i]));
                    }
                    return subItems;
                }
            }

            // As a last resort try to scan all the 16 possible meta values
            for (int meta = 0; meta < 16; meta++) {
                try { // Shitcoders go brrr
                    ItemStack itemStack = new ItemStack(item, stackSize, meta);
                    String translationKey = itemStack.getTranslationKey();

                    // If this Translation Key has already been observed, ignore it
                    if (keyCache.contains(translationKey + "@" + itemStack.getTextureId())) {
                        continue;
                    }

                    // Check if the name and ends with the meta (like the aether dart shooter)
                    if (translationKey.endsWith(String.valueOf(meta))) {
                        // If meta is present, query for the translation with that meta
                        String translatedNameWithMeta = I18n.getTranslation(translationKey + ".name");

                        // If translatedName is not translated and is the raw translation key, 
                        // then removing the last 5 characters will remove ".name" allowing the comparison with the translationKey 
                        if (translationKey.contains(translatedNameWithMeta.substring(0, translatedNameWithMeta.length() - 5))) {
                            // This meta is not translated, avoid
                            AlwaysMoreItems.LOGGER.debug("Untranslated meta value {} hidden, translation key is {}", meta, translationKey);
                            keyCache.add(translationKey + "@" + itemStack.getTextureId());
                            continue;
                        }
                    }

                    // Check if the item does not have a translation key
                    if (itemStack.getItem().getTranslatedName().equals(translationKey + ".name")) {
                        AlwaysMoreItems.LOGGER.debug("Item {} is not translated", translationKey);

                        // Check if ignoring untranslated names is enabled
                        if (AMIConfig.ignoreUntranslatedNames()) {
                            // Add only the item with meta 0 and return
                            subItems.add(new ItemStack(item, stackSize, meta));
                            return subItems;
                        }
                    }

                    keyCache.add(translationKey + "@" + itemStack.getTextureId());
                    subItems.add(itemStack);
                } catch (Exception e) {
                    //noinspection OptionalGetWithoutIsPresent if we throw an exception on this get, then someone fucked up big time.
                    AlwaysMoreItems.LOGGER.error("An item being autoregistered threw an exception, yell at the creator of " + ItemRegistry.INSTANCE.getId(item.id).get(), e);
                }
            }
        }

        return subItems;
    }

    @Override
    @Nonnull
    public List<ItemStack> getAllSubtypes(@Nullable Iterable stacks) {
        if (stacks == null) {
            AlwaysMoreItems.LOGGER.error("Null stacks", new NullPointerException());
            return Collections.emptyList();
        }

        List<ItemStack> allSubtypes = new ArrayList<>();
        getAllSubtypes(allSubtypes, stacks);
        return allSubtypes;
    }

    private void getAllSubtypes(@Nonnull List<ItemStack> subtypesList, @Nonnull Iterable stacks) {
        for (Object obj : stacks) {
            if (obj instanceof ItemStack stack) {
                List<ItemStack> subtypes = getSubtypes(stack);
                subtypesList.addAll(subtypes);
            } else if (obj instanceof Iterable iterable) {
                getAllSubtypes(subtypesList, iterable);
            } else if (obj != null) {
                AlwaysMoreItems.LOGGER.error("Unknown object found: {}", obj);
            }
        }
    }

    @Override
    @Nonnull
    public List<ItemStack> toItemStackList(@Nullable Object stacks) {
        if (stacks == null) {
            return Collections.emptyList();
        }

        List<ItemStack> itemStacksList = new ArrayList<>();
        toItemStackList(itemStacksList, stacks);
        return removeDuplicateItemStacks(itemStacksList);
    }

    private void toItemStackList(@Nonnull List<ItemStack> itemStackList, @Nullable Object input) {
        if (input instanceof ItemStack stack) {
            itemStackList.add(stack);
        } else if (input instanceof String) {
//            List<ItemStack> stacks = ItemRegistry.INSTANCE.getEntryList(TagKey.of(ItemRegistry.KEY, Identifier.of(string)));
//            itemStackList.addAll(stacks);
        } else if (input instanceof Iterable iterable) {
            for (Object obj : iterable) {
                toItemStackList(itemStackList, obj);
            }
        } else if (input instanceof Either<?, ?> either) {
            if (either.left().isPresent()) { // LEFT = TagKey
                //System.out.println("LEFT " + either.left().get().getClass().getSimpleName());
                if (either.left().get() instanceof TagKey<?> iTagKey) {
                    Optional<TagKey<Item>> tagKey = iTagKey.tryCast(ItemRegistry.INSTANCE.getKey());
                    if (tagKey.isPresent()) {
                        for (RegistryEntry<Item> entry : ItemRegistry.INSTANCE.getOrCreateEntryList(tagKey.get())) {
                            itemStackList.add(new ItemStack(entry.value(), 1));
                        }
                    }
                }
            } else if (either.right().isPresent()) { // Right = ItemStack
                //System.out.println("RIGHT " + either.right().get().getClass().getSimpleName());
                if (either.right().get() instanceof ItemStack stack) {
                    itemStackList.add(stack);
                }
            }

        } else if (input != null) {
            AlwaysMoreItems.LOGGER.error("Unknown object found: {}", input);
        }
    }

    @Nonnull
    public String getUniqueIdentifierForStack(@Nonnull ItemStack stack) {
        return getUniqueIdentifierForStack(stack, false);
    }

    @Nonnull
    public String getUniqueIdentifierForStack(@Nonnull ItemStack stack, boolean wildcard) {
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
            if (!((AMINbt) stack.getStationNbt()).always_More_Items$hasNoTags()) {
                NbtCompound nbtTagCompound = AlwaysMoreItems.getHelpers().getNbtIgnoreList().getNbt(stack);
                if (nbtTagCompound != null && !((AMINbt) nbtTagCompound).always_More_Items$hasNoTags()) {
                    itemKey.append(':').append(nbtTagCompound);
                }
            }
        }

        return itemKey.toString();
    }

    @Nonnull
    public List<String> getUniqueIdentifiersWithWildcard(@Nonnull ItemStack itemStack) {
        String uid = getUniqueIdentifierForStack(itemStack, false);
        String uidWild = getUniqueIdentifierForStack(itemStack, true);

        if (uid.equals(uidWild)) {
            return Collections.singletonList(uid);
        } else {
            return Arrays.asList(uid, uidWild);
        }
    }

    public int addStack(@Nonnull ScreenHandler container, @Nonnull Collection<Integer> slotIndexes, @Nonnull ItemStack stack, boolean doAdd) {
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

    public static class MatchingItemsResult {
        @Nonnull
        public final Map<Integer, ItemStack> matchingItems = new HashMap<>();
        @Nonnull
        public final List<Integer> missingItems = new ArrayList<>();
    }
}
