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
            SmeltingRegistry.addSmeltingRecipe(new ItemStack(Block.DIRT), new ItemStack(amiTortureTester, 1, 4));
        }
    }
}
