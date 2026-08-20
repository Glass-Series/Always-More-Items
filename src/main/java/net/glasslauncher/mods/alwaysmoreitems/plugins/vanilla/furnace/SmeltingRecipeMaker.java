package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import com.mojang.datafixers.util.Either;
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

            List<Either<TagKey<Item>, ItemStack>> inputs;

            Object objItem = itemStackItemStackEntry.getKey();
            if (objItem instanceof TagKey<?> key) {
                //noinspection unchecked Thanks java type erasure, very cool
                inputs = Collections.singletonList(Either.left((TagKey<Item>) key));
            }
            else if (objItem instanceof ItemStack itemStack) {
                inputs = new ArrayList<>();
                stackHelper.getSubtypes(itemStack).forEach(input -> inputs.add(Either.right(input)));
            }
            else {
                inputs = Collections.singletonList(Either.right(new ItemStack((int) objItem, 1, 0)));
            }

            ItemStack output = itemStackItemStackEntry.getValue();

            SmeltingRecipe recipe = new SmeltingRecipe(inputs, output);
            recipes.add(recipe);
        }

        return recipes;
    }

}
