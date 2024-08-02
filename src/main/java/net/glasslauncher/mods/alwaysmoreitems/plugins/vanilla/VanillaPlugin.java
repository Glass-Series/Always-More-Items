package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla;

import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMISyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.api.IItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.mods.alwaysmoreitems.api.IModRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferRegistry;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapedOreRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapedRecipesHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapelessOreRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapelessRecipesHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FuelRecipeMaker;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FurnaceFuelCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.FurnaceSmeltingCategory;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace.SmeltingRecipeMaker;
import net.minecraft.ShapedRecipe;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.FurnaceScreenHandler;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import java.util.*;

public class VanillaPlugin implements IModPlugin {
	public static final Identifier ID = AlwaysMoreItems.NAMESPACE.id("vanilla");

	private IItemRegistry itemRegistry;
	private IAMIHelpers amiHelpers;

	@Override
	public String getName() {
		return "Vanilla";
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public void onAMIHelpersAvailable(IAMIHelpers amiHelpers) {
		this.amiHelpers = amiHelpers;
	}

	@Override
	public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	@Override
	public void register(IModRegistry registry) {
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

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		recipeTransferRegistry.addRecipeTransferHandler(CraftingScreenHandler.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		recipeTransferRegistry.addRecipeTransferHandler(FurnaceScreenHandler.class, VanillaRecipeCategoryUid.SMELTING, 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(FurnaceScreenHandler.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);

		registry.addRecipes(CraftingRecipeManager.getInstance().getRecipes());
		registry.addRecipes(SmeltingRecipeMaker.getFurnaceRecipes(amiHelpers));
		registry.addRecipes(FuelRecipeMaker.getFuelRecipes(itemRegistry, amiHelpers));
	}

	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

	}

	@Override
	public IAMISyncableRecipe deserializeRecipe(NbtCompound recipe) {
        return switch (recipe.getByte("type")) {
            case 1 -> // Vanilla shapeless
                    (IAMISyncableRecipe) new ShapelessRecipe(new ItemStack(recipe.getCompound("output")), Arrays.asList(parseInputs(recipe.getList("input"))));
            case 2 -> // Vanilla shaped
                    (IAMISyncableRecipe) new ShapedRecipe(recipe.getInt("width"), recipe.getInt("height"), parseInputs(recipe.getList("input")), new ItemStack(recipe.getCompound("output")));
            case 3 -> // StAPI shapeless
                    (IAMISyncableRecipe) new StationShapelessRecipe(new ItemStack(recipe.getCompound("output")), parseStapiInputs(recipe.getList("input")));
            case 4 -> // StAPI shaped
                    (IAMISyncableRecipe) new StationShapedRecipe(recipe.getInt("width"), recipe.getInt("height"), parseStapiInputs(recipe.getList("input")), new ItemStack(recipe.getCompound("output")));
            case 5 -> // Furnace
                    new SmeltingRecipe(Collections.singletonList(new ItemStack(recipe.getCompound("input"))), new ItemStack(recipe.getCompound("output")));
            default -> null;
        };
    }

	private Either<Integer, Either<TagKey<Item>, ItemStack>> parseStapiFurnaceItem(NbtCompound input) {
		if (input.getInt("itemInt") != 0) {
			return Either.left(input.getInt("itemInt"));
		}
		NbtList imTooLazyForThis = new NbtList();
		imTooLazyForThis.add(input.getCompound("itemNbt"));
		return Either.right(parseStapiInputs(imTooLazyForThis)[0]);
	}

	public static ItemStack[] parseInputs(NbtList inputs) {
		ItemStack[] outputs = new ItemStack[inputs.size() - 1];
		for (int i = 0; i < inputs.size(); i++) {
			outputs[i] = new ItemStack((NbtCompound) inputs.get(i));
		}
		return outputs;
	}

	public static Either<TagKey<Item>, ItemStack>[] parseStapiInputs(NbtList inputs) {
        //noinspection unchecked
        Either<TagKey<Item>, ItemStack>[] outputs = new Either[inputs.size() - 1];
		for (int i = 0; i < inputs.size(); i++) {
			NbtElement input = inputs.get(i);
            if (input instanceof NbtString string && string.value.contains(":")) {
				outputs[i] = Either.left(TagKey.of(ItemRegistry.KEY, Identifier.of(string.value)));
				continue;
			}
            //noinspection DataFlowIssue
            outputs[i] = Either.right(new ItemStack((NbtCompound) input));
		}
		return outputs;
	}

	public static class FurnaceRecipe implements IAMISyncableRecipe {
		public Either<Integer, Either<TagKey<Item>, ItemStack>> input;
		public Either<Integer, Either<TagKey<Item>, ItemStack>> output;

		public FurnaceRecipe(Either<Integer, Either<TagKey<Item>, ItemStack>> input, Either<Integer, Either<TagKey<Item>, ItemStack>> output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public NbtCompound exportRecipe() {
			NbtCompound recipe = new NbtCompound();
			recipe.put("input", exportItemToNbt(input));
			recipe.put("output", exportItemToNbt(output));
			return recipe;
		}

		@Override
		public Identifier getPlugin() {
			return AlwaysMoreItems.NAMESPACE.id("always_more_items");
		}

		public NbtElement exportItemToNbt(Either<Integer, Either<TagKey<Item>, ItemStack>> item) {
			if (item.left().isPresent()) {
				return new NbtInt(item.left().get());
			}
			if (item.right().isEmpty()) {
				throw new NullPointerException();
			}
			Either<TagKey<Item>, ItemStack> stapiItem = item.right().get();
			if (stapiItem.right().isPresent()) {
				NbtCompound itemNbt = new NbtCompound();
				stapiItem.right().get().writeNbt(itemNbt);
				return itemNbt;
			}
			if (stapiItem.right().isEmpty()) {
				throw new NullPointerException();
			}
			return new NbtString(stapiItem.right().get().toString());
		}
	}
}
