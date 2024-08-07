package net.glasslauncher.mods.alwaysmoreitems.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.glasslauncher.mods.alwaysmoreitems.api.ItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.*;
import java.util.*;

public class AMIItemRegistry implements ItemRegistry {

    @Nonnull
    private final Set<String> itemNameSet = new HashSet<>();
    @Nonnull
    private final ImmutableList<ItemStack> itemList;
    @Nonnull
    private final ImmutableListMultimap<String, ItemStack> itemsByModId;
    @Nonnull
    private final ImmutableList<ItemStack> fuels;

    public AMIItemRegistry() {
        List<ItemStack> itemListMutable = new ArrayList<>();
        List<ItemStack> fuelsMutable = new ArrayList<>();

        Item[] items = net.modificationstation.stationapi.api.registry.ItemRegistry.INSTANCE.stream().toArray(Item[]::new);
        for (Item item : items) {
            addItemAndSubItems(item, itemListMutable, fuelsMutable);
        }

        this.itemList = ImmutableList.copyOf(itemListMutable);
        this.fuels = ImmutableList.copyOf(fuelsMutable);

        ImmutableListMultimap.Builder<String, ItemStack> itemsByModIdBuilder = ImmutableListMultimap.builder();
        for (ItemStack itemStack : itemListMutable) {
            Item item = itemStack.getItem();
            if (item != null) {
                Identifier itemResourceLocation = net.modificationstation.stationapi.api.registry.ItemRegistry.INSTANCE.getId(itemStack.getItem());
                if (itemResourceLocation == null) {
                    AlwaysMoreItems.LOGGER.warn("Item has no associated mod id", new NullPointerException());
                    continue;
                }
                String modId = itemResourceLocation.namespace.toString().toLowerCase(Locale.ENGLISH);
                itemsByModIdBuilder.put(modId, itemStack);
            }
        }
        this.itemsByModId = itemsByModIdBuilder.build();
    }

    @Override
    @Nonnull
    public ImmutableList<ItemStack> getItemList() {
        return itemList;
    }

    @Override
    @Nonnull
    public ImmutableList<ItemStack> getFuels() {
        return fuels;
    }

    @Nonnull
    @Override
    public String getModNameForItem(@Nullable Item item) {
        Identifier identifier = net.modificationstation.stationapi.api.registry.ItemRegistry.INSTANCE.getId(item);
        if (identifier == null) {
            AlwaysMoreItems.LOGGER.error("Item has no identifier?", new NullPointerException());
            return "";
        }
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(identifier.namespace.toString());
        if (modContainer.isEmpty()) {
            AlwaysMoreItems.LOGGER.error("Mod namespace has no container", new NullPointerException());
            return "";
        }
        return modContainer.get().getMetadata().getName();
    }

    @Nonnull
    @Override
    public ImmutableList<ItemStack> getItemListForModId(@Nullable String modId) {
        if (modId == null) {
            AlwaysMoreItems.LOGGER.error("Null modId", new NullPointerException());
            return ImmutableList.of();
        }
        String lowerCaseModId = modId.toLowerCase(Locale.ENGLISH);
        return itemsByModId.get(lowerCaseModId);
    }

    private void addItemAndSubItems(@Nullable Item item, @Nonnull List<ItemStack> itemList, @Nonnull List<ItemStack> fuels) {
        if (item == null) {
            return;
        }

        List<ItemStack> items = AlwaysMoreItems.getStackHelper().getSubtypes(item, 1);
        addItemStacks(items, itemList, fuels);
    }

    private void addItemStacks(@Nonnull Iterable<ItemStack> stacks, @Nonnull List<ItemStack> itemList, @Nonnull List<ItemStack> fuels) {
        for (ItemStack stack : stacks) {
            if (stack != null) {
                addItemStack(stack, itemList, fuels);
            }
        }
    }

    private void addItemStack(@Nonnull ItemStack stack, @Nonnull List<ItemStack> itemList, @Nonnull List<ItemStack> fuels) {
        try {
            String itemKey = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(stack);

            if (itemNameSet.contains(itemKey)) {
                return;
            }
            itemNameSet.add(itemKey);
            itemList.add(stack);

            if (FuelRegistry.getFuelTime(stack) > 0) {
                fuels.add(stack);
            }
        } catch (RuntimeException e) {
            AlwaysMoreItems.LOGGER.error("Couldn't create unique name for itemStack {}.", stack.getClass(), e);
        }
    }
}