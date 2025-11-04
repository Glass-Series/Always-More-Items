package net.glasslauncher.mods.alwaysmoreitems.testmod.init;

import net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock.BlockPatternEntry;
import net.glasslauncher.mods.alwaysmoreitems.registry.multiblock.MultiBlockRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.testmod.TestMod;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RecipeListener {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        Identifier type = event.recipeId;
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            List<String[]> testMultiblockLayers = List.of(
                    new String[]{"  xxx", "  xgx", "  xxx", "x---x", ".   .", ".   .", ".   .", "x---x"},
                    new String[]{"  xyx", "  xxx", "  xxx", "i   i", "     ", "     ", "     ", "i   i"},
                    new String[]{"  xxx", "  pxf", "  xxx", "i   i", "     ", "     ", "     ", "i   i"},
                    new String[]{"     ", "     ", "     ", "i   i", "     ", "     ", "     ", "i   i"},
                    new String[]{"     ", "     ", "     ", "x---x", ".   .", ".   .", ".   .", "x---x"}
            );
            List<BlockPatternEntry> testMultiblockPatterns = List.of(
                    new BlockPatternEntry('x', Block.LOG.getDefaultState(), 0, new ItemStack(Block.LOG.asItem())),
                    new BlockPatternEntry('y', Block.FURNACE.getDefaultState(), 2, new ItemStack(Block.FURNACE.asItem())),
                    new BlockPatternEntry('g', Block.CHEST.getDefaultState(), 0, new ItemStack(Block.CHEST.asItem())),
                    new BlockPatternEntry('p', Block.GRASS_BLOCK.getDefaultState(), 0, new ItemStack(Block.GRASS_BLOCK.asItem())),
                    new BlockPatternEntry('f', Block.CRAFTING_TABLE.getDefaultState(), 0, new ItemStack(Block.CRAFTING_TABLE.asItem())),
                    new BlockPatternEntry('i', Block.WOOL.getDefaultState(), 0, new ItemStack(Block.WOOL.asItem())),
                    new BlockPatternEntry('-', Block.WOOL.getDefaultState(), 2, new ItemStack(Block.WOOL.asItem(), 1, 2)),
                    new BlockPatternEntry('.', Block.WOOL.getDefaultState(), 1, new ItemStack(Block.WOOL.asItem(), 1, 1))

            );
            List<Object> testDescription = new ArrayList<>() {
                {
                    this.add("Test Block");
                    this.add("A test block for testing tests!");
                    this.add("This is it, you can stop reading now.");
                    this.add("Bielefeld does exist.");
                }
            };
            MultiBlockRecipeRegistry.INSTANCE.addMultiblockRecipe(Identifier.of(TestMod.NAMESPACE, "test"), testDescription, testMultiblockLayers, testMultiblockPatterns);
        }
    }
}
