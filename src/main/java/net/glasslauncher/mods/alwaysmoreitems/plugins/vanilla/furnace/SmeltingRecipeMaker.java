package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.mods.alwaysmoreitems.api.AMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.api.SubItemHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.StackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;
import net.modificationstation.stationapi.api.tag.TagKey;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SmeltingRecipeMaker {

    @Nonnull
    public static List<SmeltingRecipe> getFurnaceRecipes(AMIHelpers helpers) {
        StackHelper stackHelper = helpers.getStackHelper();
        //noinspection unchecked
        Map<?, ItemStack> smeltingMap = SmeltingRecipeManager.getInstance().getRecipes();

        List<SmeltingRecipe> recipes = new ArrayList<>();

        for (Map.Entry<?, ItemStack> itemStackItemStackEntry : smeltingMap.entrySet()) {

            List<ItemStack> inputs;

            Object objItem = itemStackItemStackEntry.getKey();
            if (objItem instanceof TagKey<?> key) {
                //noinspection unchecked
                Optional<RegistryEntryList.Named<Item>> instanceEntryList = ItemRegistry.INSTANCE.getEntryList((TagKey<Item>) key);
                if (instanceEntryList.isEmpty()) {
                    continue;
                }
                inputs = new ArrayList<>();
                instanceEntryList.ifPresent(
                        registryEntries -> registryEntries.forEach(itemRegistryEntry -> {
                            List<ItemStack> items = SubItemHelper.getSubItems(itemRegistryEntry.value());
                            if (items != null) {
                                inputs.addAll(items);
                            }
                        })
                );
            }
            else if (objItem instanceof ItemStack itemStack) {
                inputs = stackHelper.getSubtypes(itemStack);
            }
            else {
                inputs = Collections.singletonList(new ItemStack((int) objItem, 1, 0));
            }

            ItemStack output = itemStackItemStackEntry.getValue();

            SmeltingRecipe recipe = new SmeltingRecipe(inputs, output);
            recipes.add(recipe);
        }

        return recipes;
    }

}
