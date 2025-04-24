package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla;

import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.api.AMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.api.ItemBlacklist;
import net.glasslauncher.mods.alwaysmoreitems.api.ItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.ModPluginProvider;
import net.glasslauncher.mods.alwaysmoreitems.api.ModRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferRegistry;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapedOreRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapedRecipesHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapelessOreRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapelessRecipesHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipeMaker;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FurnaceFuelCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FurnaceSmeltingCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipeMaker;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.ShapedRecipe;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.FurnaceScreenHandler;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VanillaPlugin implements ModPluginProvider {
    public static final Identifier ID = AlwaysMoreItems.NAMESPACE.id("vanilla");

    private ItemRegistry itemRegistry;
    private AMIHelpers amiHelpers;

    @Override
    public String getName() {
        return "Vanilla";
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void onAMIHelpersAvailable(AMIHelpers amiHelpers) {
        this.amiHelpers = amiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    @Override
    public void register(ModRegistry registry) {
        registry.addRecipeCategories(
                new CraftingRecipeCategory(),
                new FurnaceFuelCategory(),
                new FurnaceSmeltingCategory()
        );

        registry.addRecipeHandlers(
                new ShapedOreRecipeHandler(),
                new ShapedRecipesHandler(),
                new ShapelessOreRecipeHandler(),
                new ShapelessRecipesHandler(),
                new FuelRecipeHandler(),
                new SmeltingRecipeHandler()
        );

        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            registry.addRecipeClickArea(CraftingScreen.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
            registry.addRecipeClickArea(FurnaceScreen.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
        }

        RecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(CraftingScreenHandler.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
        recipeTransferRegistry.addRecipeTransferHandler(FurnaceScreenHandler.class, VanillaRecipeCategoryUid.SMELTING, 0, 1, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(FurnaceScreenHandler.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);

        registry.addRecipes(CraftingRecipeManager.getInstance().getRecipes());
        registry.addRecipes(SmeltingRecipeMaker.getFurnaceRecipes(amiHelpers));
        registry.addRecipes(FuelRecipeMaker.getFuelRecipes(itemRegistry, amiHelpers));
    }

    @Override
    public void onRecipeRegistryAvailable(RecipeRegistry recipeRegistry) {

    }

    @Override
    public SyncableRecipe deserializeRecipe(NbtCompound recipe) {
        return switch (recipe.getByte("type")) {
            case 1 -> // Vanilla shapeless
                    (SyncableRecipe) new ShapelessRecipe(new ItemStack(recipe.getCompound("output")), Arrays.asList(parseInputs(recipe.getList("input"))));
            case 2 -> // Vanilla shaped
                    (SyncableRecipe) new ShapedRecipe(recipe.getInt("width"), recipe.getInt("height"), parseInputs(recipe.getList("input")), new ItemStack(recipe.getCompound("output")));
            case 3 -> // StAPI shapeless
                    (SyncableRecipe) new StationShapelessRecipe(new ItemStack(recipe.getCompound("output")), parseStapiInputs(recipe.getList("input")));
            case 4 -> // StAPI shaped
                    (SyncableRecipe) new StationShapedRecipe(recipe.getInt("width"), recipe.getInt("height"), parseStapiInputs(recipe.getList("input")), new ItemStack(recipe.getCompound("output")));
            case 5 -> // Furnace
                    new SmeltingRecipe(Collections.singletonList(new ItemStack(recipe.getCompound("input"))), new ItemStack(recipe.getCompound("output")));
            case 6 -> // Furnace fuel
                    new FuelRecipe(List.of(parseInputs(recipe.getList("input"))), recipe.getInt("burnTime"));
            default -> null;
        };
    }

    public static ItemStack[] parseInputs(NbtList inputs) {
        ItemStack[] outputs = new ItemStack[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            if (((NbtCompound) inputs.get(i)).getByte("Count") == 0) {
                continue;
            }
            outputs[i] = new ItemStack((NbtCompound) inputs.get(i));
        }
        return outputs;
    }

    public static Either<TagKey<Item>, ItemStack>[] parseStapiInputs(NbtList inputs) {
        //noinspection unchecked
        Either<TagKey<Item>, ItemStack>[] outputs = new Either[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            NbtCompound input = (NbtCompound) inputs.get(i);
            if (!input.getString("identifier").isEmpty()) {
                outputs[i] = Either.left(TagKey.of(net.modificationstation.stationapi.api.registry.ItemRegistry.KEY, Identifier.of(input.getString("identifier"))));
                continue;
            }
            if (((NbtCompound) inputs.get(i)).getByte("Count") == 0) {
                continue;
            }
            outputs[i] = Either.right(new ItemStack(input));
        }
        return outputs;
    }

    @Override
    public void updateBlacklist(AMIHelpers amiHelpers) {
        if (AMIConfig.showRedundantItems()) {
            return;
        }

        ItemBlacklist itemBlacklist = amiHelpers.getItemBlacklist();
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.WATER));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.LAVA));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.GRASS));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.BED));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.PISTON_HEAD));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.MOVING_PISTON));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.DOUBLE_SLAB));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.FIRE));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.REDSTONE_WIRE));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.WHEAT));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.LIT_FURNACE));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.SIGN));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.DOOR));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.WALL_SIGN));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.IRON_DOOR));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.LIT_REDSTONE_ORE));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.REDSTONE_TORCH));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.SUGAR_CANE));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.REPEATER));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.POWERED_REPEATER));
        itemBlacklist.addItemToBlacklist(new ItemStack(Block.LOCKED_CHEST));
    }
}
