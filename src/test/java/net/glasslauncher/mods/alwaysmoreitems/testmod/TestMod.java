package net.glasslauncher.mods.alwaysmoreitems.testmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class TestMod {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

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
