package net.glasslauncher.mods.alwaysmoreitems;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMISyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.api.IRecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeClickableArea;
import net.glasslauncher.mods.alwaysmoreitems.util.ItemUidException;
import net.glasslauncher.mods.alwaysmoreitems.util.RecipeCategoryComparator;
import net.glasslauncher.mods.alwaysmoreitems.util.RecipeMap;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.*;
import java.util.*;

public class RecipeRegistry implements IRecipeRegistry {
	private final ImmutableMap<Class, IRecipeHandler> recipeHandlers;
	private final ImmutableTable<Class, String, IRecipeTransferHandler> recipeTransferHandlers;
	private final ImmutableMap<Class<? extends HandledScreen>, RecipeClickableArea> recipeClickableAreas;
	private final ImmutableMap<String, IRecipeCategory> recipeCategoriesMap;
	private final ListMultimap<IRecipeCategory, Object> recipesForCategories;
	private final RecipeMap recipeInputMap;
	private final RecipeMap recipeOutputMap;
	private final Set<Class> unhandledRecipeClasses;
	@Getter
	private final ArrayList<IAMISyncableRecipe> syncableRecipes;
	@Getter
	private final ArrayList<Object> unsyncableRecipes;

	public RecipeRegistry(@Nonnull List<IRecipeCategory> recipeCategories, @Nonnull List<IRecipeHandler> recipeHandlers, @Nonnull List<IRecipeTransferHandler> recipeTransferHandlers, @Nonnull List<Object> recipes, @Nonnull Map<Class<? extends HandledScreen>, RecipeClickableArea> recipeClickableAreas) {
		this.recipeCategoriesMap = buildRecipeCategoriesMap(recipeCategories);
		this.recipeTransferHandlers = buildRecipeTransferHandlerTable(recipeTransferHandlers);
		this.recipeHandlers = buildRecipeHandlersMap(recipeHandlers);
		this.recipeClickableAreas = ImmutableMap.copyOf(recipeClickableAreas);

		RecipeCategoryComparator recipeCategoryComparator = new RecipeCategoryComparator(recipeCategories);
		this.recipeInputMap = new RecipeMap(recipeCategoryComparator);
		this.recipeOutputMap = new RecipeMap(recipeCategoryComparator);

		this.unhandledRecipeClasses = new HashSet<>();

		this.recipesForCategories = ArrayListMultimap.create();
		this.syncableRecipes = new ArrayList<>();
		this.unsyncableRecipes = new ArrayList<>();
		addRecipes(recipes);
	}

	private static ImmutableMap<String, IRecipeCategory> buildRecipeCategoriesMap(@Nonnull List<IRecipeCategory> recipeCategories) {
		ImmutableMap.Builder<String, IRecipeCategory> mapBuilder = ImmutableMap.builder();
		for (IRecipeCategory recipeCategory : recipeCategories) {
			mapBuilder.put(recipeCategory.getUid(), recipeCategory);
		}
		return mapBuilder.build();
	}

	private static ImmutableMap<Class, IRecipeHandler> buildRecipeHandlersMap(@Nonnull List<IRecipeHandler> recipeHandlers) {
		ImmutableMap.Builder<Class, IRecipeHandler> mapBuilder = ImmutableMap.builder();
		Set<Class> recipeHandlerClasses = new HashSet<>();
		for (IRecipeHandler recipeHandler : recipeHandlers) {
			if (recipeHandler == null) {
				continue;
			}

			Class recipeClass = recipeHandler.getRecipeClass();

			if (recipeHandlerClasses.contains(recipeClass)) {
				throw new IllegalArgumentException("A Recipe Handler has already been registered for this recipe class: " + recipeClass.getName());
			}

			recipeHandlerClasses.add(recipeClass);
			mapBuilder.put(recipeClass, recipeHandler);
		}
		return mapBuilder.build();
	}

	private static ImmutableTable<Class, String, IRecipeTransferHandler> buildRecipeTransferHandlerTable(@Nonnull List<IRecipeTransferHandler> recipeTransferHandlers) {
		ImmutableTable.Builder<Class, String, IRecipeTransferHandler> builder = ImmutableTable.builder();
		for (IRecipeTransferHandler recipeTransferHelper : recipeTransferHandlers) {
			builder.put(recipeTransferHelper.getContainerClass(), recipeTransferHelper.getRecipeCategoryUid(), recipeTransferHelper);
		}
		return builder.build();
	}

	private void addRecipes(@Nullable List<Object> recipes) {
		if (recipes == null) {
			return;
		}

		for (Object recipe : recipes) {
			addRecipe(recipe);
		}
	}

	@Override
	public void addRecipe(@Nullable Object recipe) {
		if (recipe == null) {
			AlwaysMoreItems.LOGGER.error("Null recipe", new NullPointerException());
			return;
		}

		Class recipeClass = recipe.getClass();
		IRecipeHandler recipeHandler = getRecipeHandler(recipeClass);
		if (recipeHandler == null) {
			if (!unhandledRecipeClasses.contains(recipeClass)) {
				unhandledRecipeClasses.add(recipeClass);
				if (AMIConfig.isDebugModeEnabled()) {
					AlwaysMoreItems.LOGGER.debug("Can't handle recipe: {}", recipeClass);
				}
			}
			return;
		}

		String recipeCategoryUid = recipeHandler.getRecipeCategoryUid();
		IRecipeCategory recipeCategory = recipeCategoriesMap.get(recipeCategoryUid);
		if (recipeCategory == null) {
			AlwaysMoreItems.LOGGER.error("No recipe category registered for recipeCategoryUid: {}", recipeCategoryUid);
			return;
		}

		//noinspection unchecked
		if (!recipeHandler.isRecipeValid(recipe)) {
			return;
		}

		try {
			addRecipeUnchecked(recipe, recipeCategory, recipeHandler);
		} catch (RuntimeException e) {
			String recipeInfo = getInfoFromBrokenRecipe(recipe, recipeHandler);
			if (e instanceof ItemUidException) {
				AlwaysMoreItems.LOGGER.error("Found broken recipe: {}\n{}\n", e.getMessage(), recipeInfo);
			} else {
				AlwaysMoreItems.LOGGER.error("Found broken recipe: {}\n", recipeInfo, e);
			}
		}
	}

	@Nonnull
	private String getInfoFromBrokenRecipe(@Nonnull Object recipe, @Nonnull IRecipeHandler recipeHandler) {
		StringBuilder recipeInfoBuilder = new StringBuilder();
		try {
			recipeInfoBuilder.append(recipe);
		} catch (RuntimeException e) {
			AlwaysMoreItems.LOGGER.error("Failed recipe.toString", e);
			recipeInfoBuilder.append(recipe.getClass());
		}

		IRecipeWrapper recipeWrapper;

		try {
			//noinspection unchecked
			recipeWrapper = recipeHandler.getRecipeWrapper(recipe);
		} catch (RuntimeException ignored) {
			recipeInfoBuilder.append("\nFailed to create recipe wrapper");
			return recipeInfoBuilder.toString();
		}

		recipeInfoBuilder.append("\nOutput ItemStacks: ");
		try {
			recipeInfoBuilder.append(recipeWrapper.getOutputs());
		} catch (RuntimeException e) {
			recipeInfoBuilder.append(e.getMessage());
		}

//		recipeInfoBuilder.append("\nOutput Fluids: ");
//		try {
//			recipeInfoBuilder.append(recipeWrapper.getFluidOutputs());
//		} catch (RuntimeException e) {
//			recipeInfoBuilder.append(e.getMessage());
//		}

		recipeInfoBuilder.append("\nInput ItemStacks: ");
		try {
			recipeInfoBuilder.append(recipeWrapper.getInputs());
		} catch (RuntimeException e) {
			recipeInfoBuilder.append(e.getMessage());
		}

//		recipeInfoBuilder.append("\nInput Fluids: ");
//		try {
//			recipeInfoBuilder.append(recipeWrapper.getFluidInputs());
//		} catch (RuntimeException e) {
//			recipeInfoBuilder.append(e.getMessage());
//		}

		return recipeInfoBuilder.toString();
	}

	// TODO: Implement fluids when API exists
	private void addRecipeUnchecked(@Nonnull Object recipe, IRecipeCategory recipeCategory, IRecipeHandler recipeHandler) {
		//noinspection unchecked
		IRecipeWrapper recipeWrapper = recipeHandler.getRecipeWrapper(recipe);

		List<?> inputs = recipeWrapper.getInputs();
		if (inputs != null) {
			List<ItemStack> inputStacks = AlwaysMoreItems.getStackHelper().toItemStackList(inputs);
			recipeInputMap.addRecipe(recipe, recipeCategory, inputStacks);
		}

		List<?> outputs = recipeWrapper.getOutputs();
		if (outputs != null) {
			List<ItemStack> outputStacks = AlwaysMoreItems.getStackHelper().toItemStackList(outputs);
			recipeOutputMap.addRecipe(recipe, recipeCategory, outputStacks);
		}

		if (recipe instanceof IAMISyncableRecipe syncableRecipe) {
			syncableRecipes.add(syncableRecipe);
		}
		else {
			unsyncableRecipes.add(recipe);
		}

		recipesForCategories.put(recipeCategory, recipe);
	}

	@Nonnull
	@Override
	public ImmutableList<IRecipeCategory> getRecipeCategories() {
		ImmutableList.Builder<IRecipeCategory> builder = ImmutableList.builder();
		for (IRecipeCategory recipeCategory : recipeCategoriesMap.values()) {
			if (!getRecipes(recipeCategory).isEmpty()) {
				builder.add(recipeCategory);
			}
		}
		return builder.build();
	}

	@Nonnull
	@Override
	public ImmutableList<IRecipeCategory> getRecipeCategories(@Nullable List<String> recipeCategoryUids) {
		if (recipeCategoryUids == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeCategoryUids", new NullPointerException());
			return ImmutableList.of();
		}

		ImmutableList.Builder<IRecipeCategory> builder = ImmutableList.builder();
		for (String recipeCategoryUid : recipeCategoryUids) {
			IRecipeCategory recipeCategory = recipeCategoriesMap.get(recipeCategoryUid);
			if (recipeCategory != null && !getRecipes(recipeCategory).isEmpty()) {
				builder.add(recipeCategory);
			}
		}
		return builder.build();
	}

	@Nullable
	@Override
	public IRecipeHandler getRecipeHandler(@Nullable Class recipeClass) {
		if (recipeClass == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeClass", new NullPointerException());
			return null;
		}

		IRecipeHandler recipeHandler;
		while ((recipeHandler = recipeHandlers.get(recipeClass)) == null && (recipeClass != Object.class)) {
			recipeClass = recipeClass.getSuperclass();
		}

		return recipeHandler;
	}

	@Nullable
	public RecipeClickableArea getRecipeClickableArea(@Nonnull HandledScreen gui) {
		return recipeClickableAreas.get(gui.getClass());
	}

	@Nonnull
	@Override
	public ImmutableList<IRecipeCategory> getRecipeCategoriesWithInput(@Nullable ItemStack input) {
		if (input == null) {
			AlwaysMoreItems.LOGGER.error("Null ItemStack input", new NullPointerException());
			return ImmutableList.of();
		}
		return recipeInputMap.getRecipeCategories(input);
	}

//	@Nonnull
//	@Override
//	public ImmutableList<IRecipeCategory> getRecipeCategoriesWithInput(@Nullable Fluid input) {
//		if (input == null) {
//			AlwaysMoreItems.LOGGER.error("Null Fluid input", new NullPointerException());
//			return ImmutableList.of();
//		}
//		return recipeInputMap.getRecipeCategories(input);
//	}

	@Nonnull
	@Override
	public ImmutableList<IRecipeCategory> getRecipeCategoriesWithOutput(@Nullable ItemStack output) {
		if (output == null) {
			AlwaysMoreItems.LOGGER.error("Null ItemStack output", new NullPointerException());
			return ImmutableList.of();
		}
		return recipeOutputMap.getRecipeCategories(output);
	}

//	@Nonnull
//	@Override
//	public ImmutableList<IRecipeCategory> getRecipeCategoriesWithOutput(@Nullable Fluid output) {
//		if (output == null) {
//			AlwaysMoreItems.LOGGER.error("Null Fluid output", new NullPointerException());
//			return ImmutableList.of();
//		}
//		return recipeOutputMap.getRecipeCategories(output);
//	}

	@Nonnull
	@Override
	public ImmutableList<Object> getRecipesWithInput(@Nullable IRecipeCategory recipeCategory, @Nullable ItemStack input) {
		if (recipeCategory == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeCategory", new NullPointerException());
			return ImmutableList.of();
		} else if (input == null) {
			AlwaysMoreItems.LOGGER.error("Null ItemStack input", new NullPointerException());
			return ImmutableList.of();
		}
		return recipeInputMap.getRecipes(recipeCategory, input);
	}

//	@Nonnull
//	@Override
//	public List<Object> getRecipesWithInput(@Nullable IRecipeCategory recipeCategory, @Nullable Fluid input) {
//		if (recipeCategory == null) {
//			AlwaysMoreItems.LOGGER.error("Null recipeCategory", new NullPointerException());
//			return ImmutableList.of();
//		} else if (input == null) {
//			AlwaysMoreItems.LOGGER.error("Null Fluid input", new NullPointerException());
//			return ImmutableList.of();
//		}
//		return recipeInputMap.getRecipes(recipeCategory, input);
//	}

	@Nonnull
	@Override
	public ImmutableList<Object> getRecipesWithOutput(@Nullable IRecipeCategory recipeCategory, @Nullable ItemStack output) {
		if (recipeCategory == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeCategory", new NullPointerException());
			return ImmutableList.of();
		} else if (output == null) {
			AlwaysMoreItems.LOGGER.error("Null ItemStack output", new NullPointerException());
			return ImmutableList.of();
		}
		return recipeOutputMap.getRecipes(recipeCategory, output);
	}

//	@Nonnull
//	@Override
//	public List<Object> getRecipesWithOutput(@Nullable IRecipeCategory recipeCategory, @Nullable Fluid output) {
//		if (recipeCategory == null) {
//			return ImmutableList.of();
//		} else if (output == null) {
//			AlwaysMoreItems.LOGGER.error("Null Fluid output", new NullPointerException());
//			return ImmutableList.of();
//		}
//		return recipeOutputMap.getRecipes(recipeCategory, output);
//	}

	@Nonnull
	@Override
	public List<Object> getRecipes(@Nullable IRecipeCategory recipeCategory) {
		if (recipeCategory == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeCategory", new NullPointerException());
			return ImmutableList.of();
		}
		return Collections.unmodifiableList(recipesForCategories.get(recipeCategory));
	}

	@Nullable
	public IRecipeTransferHandler getRecipeTransferHandler(@Nullable ScreenHandler container, @Nullable IRecipeCategory recipeCategory) {
		if (container == null) {
			AlwaysMoreItems.LOGGER.error("Null container", new NullPointerException());
			return null;
		} else if (recipeCategory == null) {
			AlwaysMoreItems.LOGGER.error("Null recipeCategory", new NullPointerException());
			return null;
		}

		return recipeTransferHandlers.get(container.getClass(), recipeCategory.getUid());
	}
}
