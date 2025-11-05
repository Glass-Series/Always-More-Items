package net.glasslauncher.mods.alwaysmoreitems.testmod.init;

import net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock.BlockPatternEntry;
import net.glasslauncher.mods.alwaysmoreitems.registry.multiblock.MultiBlockRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.testmod.TestMod;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RecipeListener {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        Identifier type = event.recipeId;

        SignBlockEntity signBlockEntity = new SignBlockEntity();
        signBlockEntity.texts = new String[]{"Bielefeld", "is", "fake", "~Altilist"};

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            List<String[]> testMultiblockLayers = List.of(
                    new String[]{"  xxx", "  xgx", "  xxx", "x---x", ".   .", ".   .", ".   .", "x---x"},
                    new String[]{"  xyx", "  xxx", "  xxx", "i   i", "sssss", "     ", "     ", "i   i"},
                    new String[]{"  xxx", "  pxf", "  xxx", "i   i", "     ", "     ", "     ", "i   i"},
                    new String[]{"sssss", "     ", "     ", "i   i", "     ", "     ", "     ", "i   i"},
                    new String[]{"     ", "     ", "     ", "x---x", ".   .", ".   .", ".   .", "x---x"}
            );
            List<BlockPatternEntry> testMultiblockPatterns = List.of(
                    new BlockPatternEntry('x', Block.LOG.getDefaultState(), 0, new ItemStack(Block.LOG.asItem()), null),
                    new BlockPatternEntry('y', Block.FURNACE.getDefaultState(), 2, new ItemStack(Block.FURNACE.asItem()), null),
                    new BlockPatternEntry('g', Block.CHEST.getDefaultState(), 0, new ItemStack(Block.CHEST.asItem()), null),
                    new BlockPatternEntry('p', Block.GRASS_BLOCK.getDefaultState(), 0, new ItemStack(Block.GRASS_BLOCK.asItem()), null),
                    new BlockPatternEntry('f', Block.CRAFTING_TABLE.getDefaultState(), 0, new ItemStack(Block.CRAFTING_TABLE.asItem()), null),
                    new BlockPatternEntry('i', Block.WOOL.getDefaultState(), 0, new ItemStack(Block.WOOL.asItem()), null),
                    new BlockPatternEntry('-', Block.WOOL.getDefaultState(), 2, new ItemStack(Block.WOOL.asItem(), 1, 2), null),
//                    new BlockPatternEntry('.', Block.WOOL.getDefaultState(), 1, new ItemStack(Block.WOOL.asItem(), 1, 1), null),
                    new BlockPatternEntry('s', Block.SIGN.getDefaultState(), 0, new ItemStack(Block.SIGN.asItem(), 1, 0), signBlockEntity)

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

            List<String[]> diverseTestMultiblockLayers = List.of(
                    new String[]{"abcde", "fghij", "klmno"},
                    new String[]{"pqrst", "uvwxy", "zäöüß"},
                    new String[]{"ABCDE", "FGHIJ", "KLMNO"},
                    new String[]{"PQRST", "UVWXY", "ZÄÖÜẞ"}
            );
            List<BlockPatternEntry> diverseTestMultiblockPatterns = List.of(
                    new BlockPatternEntry('a', Block.STONE.getDefaultState(), 0, new ItemStack(Block.STONE.asItem())),
                    new BlockPatternEntry('b', Block.GRASS_BLOCK.getDefaultState(), 0, new ItemStack(Block.GRASS_BLOCK.asItem())),
                    new BlockPatternEntry('c', Block.DIRT.getDefaultState(), 0, new ItemStack(Block.DIRT.asItem())),
                    new BlockPatternEntry('d', Block.COBBLESTONE.getDefaultState(), 0, new ItemStack(Block.COBBLESTONE.asItem())),
                    new BlockPatternEntry('e', Block.PLANKS.getDefaultState(), 0, new ItemStack(Block.PLANKS.asItem())),
                    new BlockPatternEntry('f', Block.SAPLING.getDefaultState(), 0, new ItemStack(Block.SAPLING.asItem())),
                    new BlockPatternEntry('g', Block.BEDROCK.getDefaultState(), 0, new ItemStack(Block.BEDROCK.asItem())),
                    new BlockPatternEntry('h', Block.WATER.getDefaultState(), 0, new ItemStack(Block.WATER.asItem())),
                    new BlockPatternEntry('i', Block.LAVA.getDefaultState(), 0, new ItemStack(Block.LAVA.asItem())),
                    new BlockPatternEntry('j', Block.SAND.getDefaultState(), 0, new ItemStack(Block.SAND.asItem())),
                    new BlockPatternEntry('k', Block.GRAVEL.getDefaultState(), 0, new ItemStack(Block.GRAVEL.asItem())),
                    new BlockPatternEntry('l', Block.GOLD_ORE.getDefaultState(), 0, new ItemStack(Block.GOLD_ORE.asItem())),
                    new BlockPatternEntry('m', Block.IRON_ORE.getDefaultState(), 0, new ItemStack(Block.IRON_ORE.asItem())),
                    new BlockPatternEntry('n', Block.COAL_ORE.getDefaultState(), 0, new ItemStack(Block.COAL_ORE.asItem())),
                    new BlockPatternEntry('o', Block.LOG.getDefaultState(), 0, new ItemStack(Block.LOG.asItem())),
                    new BlockPatternEntry('p', Block.LEAVES.getDefaultState(), 0, new ItemStack(Block.LEAVES.asItem())),
                    new BlockPatternEntry('q', Block.SPONGE.getDefaultState(), 0, new ItemStack(Block.SPONGE.asItem())),
                    new BlockPatternEntry('r', Block.GLASS.getDefaultState(), 0, new ItemStack(Block.GLASS.asItem())),
                    new BlockPatternEntry('s', Block.LAPIS_ORE.getDefaultState(), 0, new ItemStack(Block.LAPIS_ORE.asItem())),
                    new BlockPatternEntry('t', Block.LAPIS_BLOCK.getDefaultState(), 0, new ItemStack(Block.LAPIS_BLOCK.asItem())),
                    new BlockPatternEntry('u', Block.DISPENSER.getDefaultState(), 0, new ItemStack(Block.DISPENSER.asItem())),
                    new BlockPatternEntry('v', Block.SANDSTONE.getDefaultState(), 0, new ItemStack(Block.SANDSTONE.asItem())),
                    new BlockPatternEntry('w', Block.NOTE_BLOCK.getDefaultState(), 0, new ItemStack(Block.NOTE_BLOCK.asItem())),
                    new BlockPatternEntry('x', Block.POWERED_RAIL.getDefaultState(), 0, new ItemStack(Block.POWERED_RAIL.asItem())),
                    new BlockPatternEntry('y', Block.DETECTOR_RAIL.getDefaultState(), 0, new ItemStack(Block.DETECTOR_RAIL.asItem())),
                    new BlockPatternEntry('z', Block.STICKY_PISTON.getDefaultState(), 0, new ItemStack(Block.STICKY_PISTON.asItem())),
                    new BlockPatternEntry('ä', Block.COBWEB.getDefaultState(), 0, new ItemStack(Block.COBWEB.asItem())),
                    new BlockPatternEntry('ö', Block.PISTON.getDefaultState(), 0, new ItemStack(Block.PISTON.asItem())),
                    new BlockPatternEntry('ü', Block.WOOL.getDefaultState(), 0, new ItemStack(Block.WOOL.asItem())),
                    new BlockPatternEntry('ß', Block.DANDELION.getDefaultState(), 0, new ItemStack(Block.DANDELION.asItem())),

                    new BlockPatternEntry('A', Block.ROSE.getDefaultState(), 0, new ItemStack(Block.ROSE.asItem())),
                    new BlockPatternEntry('B', Block.BROWN_MUSHROOM.getDefaultState(), 0, new ItemStack(Block.BROWN_MUSHROOM.asItem())),
                    new BlockPatternEntry('C', Block.RED_MUSHROOM.getDefaultState(), 0, new ItemStack(Block.RED_MUSHROOM.asItem())),
                    new BlockPatternEntry('D', Block.GOLD_BLOCK.getDefaultState(), 0, new ItemStack(Block.GOLD_BLOCK.asItem())),
                    new BlockPatternEntry('E', Block.IRON_BLOCK.getDefaultState(), 0, new ItemStack(Block.IRON_BLOCK.asItem())),
                    new BlockPatternEntry('F', Block.SLAB.getDefaultState(), 0, new ItemStack(Block.SLAB.asItem())),
                    new BlockPatternEntry('G', Block.BRICKS.getDefaultState(), 0, new ItemStack(Block.BRICKS.asItem())),
                    new BlockPatternEntry('H', Block.TNT.getDefaultState(), 0, new ItemStack(Block.TNT.asItem())),
                    new BlockPatternEntry('I', Block.BOOKSHELF.getDefaultState(), 0, new ItemStack(Block.BOOKSHELF.asItem())),
                    new BlockPatternEntry('J', Block.MOSSY_COBBLESTONE.getDefaultState(), 0, new ItemStack(Block.MOSSY_COBBLESTONE.asItem())),
                    new BlockPatternEntry('K', Block.OBSIDIAN.getDefaultState(), 0, new ItemStack(Block.OBSIDIAN.asItem())),
                    new BlockPatternEntry('L', Block.TORCH.getDefaultState(), 0, new ItemStack(Block.TORCH.asItem())),
                    new BlockPatternEntry('M', Block.SPAWNER.getDefaultState(), 0, new ItemStack(Block.SPAWNER.asItem())),
                    new BlockPatternEntry('N', Block.WOODEN_STAIRS.getDefaultState(), 0, new ItemStack(Block.WOODEN_STAIRS.asItem())),
                    new BlockPatternEntry('O', Block.CHEST.getDefaultState(), 0, new ItemStack(Block.CHEST.asItem())),
                    new BlockPatternEntry('P', Block.REDSTONE_WIRE.getDefaultState(), 0, new ItemStack(Block.REDSTONE_WIRE.asItem())),
                    new BlockPatternEntry('Q', Block.DIAMOND_ORE.getDefaultState(), 0, new ItemStack(Block.DIAMOND_ORE.asItem())),
                    new BlockPatternEntry('R', Block.DIAMOND_BLOCK.getDefaultState(), 0, new ItemStack(Block.DIAMOND_BLOCK.asItem())),
                    new BlockPatternEntry('S', Block.CRAFTING_TABLE.getDefaultState(), 0, new ItemStack(Block.CRAFTING_TABLE.asItem())),
                    new BlockPatternEntry('T', Block.FARMLAND.getDefaultState(), 0, new ItemStack(Block.FARMLAND.asItem())),
                    new BlockPatternEntry('U', Block.FURNACE.getDefaultState(), 0, new ItemStack(Block.FURNACE.asItem())),
                    new BlockPatternEntry('V', Block.LADDER.getDefaultState(), 0, new ItemStack(Block.LADDER.asItem())),
                    new BlockPatternEntry('W', Block.RAIL.getDefaultState(), 0, new ItemStack(Block.RAIL.asItem())),
                    new BlockPatternEntry('X', Block.COBBLESTONE_STAIRS.getDefaultState(), 0, new ItemStack(Block.COBBLESTONE_STAIRS.asItem())),
                    new BlockPatternEntry('Y', Block.LEVER.getDefaultState(), 0, new ItemStack(Block.LEVER.asItem())),
                    new BlockPatternEntry('Z', Block.STONE_PRESSURE_PLATE.getDefaultState(), 0, new ItemStack(Block.STONE_PRESSURE_PLATE.asItem())),
                    new BlockPatternEntry('Ä', Block.WOODEN_PRESSURE_PLATE.getDefaultState(), 0, new ItemStack(Block.WOODEN_PRESSURE_PLATE.asItem())),
                    new BlockPatternEntry('Ö', Block.REDSTONE_ORE.getDefaultState(), 0, new ItemStack(Block.REDSTONE_ORE.asItem())),
                    new BlockPatternEntry('Ü', Block.LIT_REDSTONE_ORE.getDefaultState(), 0, new ItemStack(Block.LIT_REDSTONE_ORE.asItem())),
                    new BlockPatternEntry('ẞ', Block.REDSTONE_TORCH.getDefaultState(), 0, new ItemStack(Block.REDSTONE_TORCH.asItem()))
            );
            List<Object> diverseTestDescription = new ArrayList<>() {
                {
                    this.add("Diverse Test Block");
                    this.add("A test block for testing how well a lot of");
                    this.add("blocks are handled by different parts of the");
                    this.add("tab, such as rendering and the block list.");
                }
            };
            MultiBlockRecipeRegistry.INSTANCE.addMultiblockRecipe(Identifier.of(TestMod.NAMESPACE, "diverse_test"), diverseTestDescription, diverseTestMultiblockLayers, diverseTestMultiblockPatterns);

            List<String[]> giantMultiblockLayers = List.of(
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"},
                    new String[]{"xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxx"}
            );
            List<BlockPatternEntry> giantMultiblockPatterns = List.of(
                    new BlockPatternEntry('x', Block.LOG.getDefaultState(), 0, new ItemStack(Block.LOG.asItem()))

            );
            List<Object> giantDescription = new ArrayList<>() {
                {
                    this.add("Gigantic Block");
                    this.add("A test block for testing huge structures.");
                    this.add("This is it, you can stop reading now.");
                    this.add("This sentence is FALSE!");
                }
            };
            MultiBlockRecipeRegistry.INSTANCE.addMultiblockRecipe(Identifier.of(TestMod.NAMESPACE, "giant"), giantDescription, giantMultiblockLayers, giantMultiblockPatterns);
        }
    }
}
