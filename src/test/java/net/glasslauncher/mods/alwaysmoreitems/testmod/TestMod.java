package net.glasslauncher.mods.alwaysmoreitems.testmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class TestMod {
    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();

    Item amiTortureTester;

    @EventListener
    public void init(ItemRegistryEvent event) {
        amiTortureTester = new AMIItem(NAMESPACE.id("amiitem"));
    }

    @Environment(EnvType.SERVER)
    @EventListener
    public void initRecipes(RecipeRegisterEvent event) {
        if (event.recipeId.equals(RecipeRegisterEvent.Vanilla.SMELTING.type())) {
            SmeltingRegistry.addSmeltingRecipe(TagKey.of(ItemRegistry.KEY, Identifier.of("c:cobblestones")), new ItemStack(amiTortureTester, 1, 2));
        }
        if (event.recipeId.equals(RecipeRegisterEvent.Vanilla.SMELTING.type())) {
            SmeltingRegistry.addSmeltingRecipe(new ItemStack(Block.DIRT), new ItemStack(amiTortureTester, 1, 4));
        }
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void initRecipesC(RecipeRegisterEvent event) {
        if (event.recipeId.equals(RecipeRegisterEvent.Vanilla.SMELTING.type())) {
            SmeltingRegistry.addSmeltingRecipe(new ItemStack(Block.DIRT), new ItemStack(amiTortureTester, 1, 0));
        }
        if (event.recipeId.equals(RecipeRegisterEvent.Vanilla.SMELTING.type())) {
            SmeltingRegistry.addSmeltingRecipe(TagKey.of(ItemRegistry.KEY, Identifier.of("c:cobblestones")), new ItemStack(amiTortureTester, 1, 8));
        }
    }
}
