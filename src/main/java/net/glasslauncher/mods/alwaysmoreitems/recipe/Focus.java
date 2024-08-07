package net.glasslauncher.mods.alwaysmoreitems.recipe;

import lombok.Getter;
import net.glasslauncher.mods.alwaysmoreitems.api.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class Focus {
	public enum Mode {
		INPUT, OUTPUT, NONE
	}

	@Getter
    private final ItemStack stack;
//	private final Fluid fluid;
	@Nonnull
	private Mode mode = Mode.NONE;

	public static Focus create(Object focus) {
		if (focus instanceof ItemStack) {
			return new Focus((ItemStack) focus);
//		} else if (focus instanceof Fluid) {
//			return new Focus((Fluid) focus);
//		} else if (focus instanceof FluidStack) {
//			return new Focus(((FluidStack) focus).getFluid());
		} else {
			return new Focus();
		}
	}

	public Focus() {
		this.stack = null;
//		this.fluid = null;
	}

	public Focus(ItemStack stack) {
		this.stack = stack;
//		this.fluid = getFluidFromItemStack(stack);
	}

//	public Focus(Fluid fluid) {
//		this.stack = null;
//		this.fluid = fluid;
//	}

//	@Nullable
//	private static Fluid getFluidFromItemStack(ItemStack stack) {
//		Item item = stack.getItem();
//		if (item instanceof IFluidContainerItem) {
//			IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
//			FluidStack fluidStack = fluidContainerItem.getFluid(stack);
//			if (fluidStack == null) {
//				return null;
//			}
//			return fluidStack.getFluid();
//		} else if (FluidContainerRegistry.isFilledContainer(stack)) {
//			FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
//			if (fluidStack == null) {
//				return null;
//			}
//			return fluidStack.getFluid();
//		} else if (item instanceof ItemBlock) {
//			ItemBlock itemBlock = (ItemBlock) item;
//			Block block = itemBlock.getBlock();
//			return FluidRegistry.lookupFluidForBlock(block);
//		}
//
//		return null;
//	}

//	public Fluid getFluid() {
//		return fluid;
//	}

    public boolean isBlank() {
		return stack == null; // && fluid == null;
	}

	public void setMode(@Nonnull Mode mode) {
		this.mode = mode;
	}

	@Nonnull
	public Mode getMode() {
		return mode;
	}

	public boolean equalsFocus(Focus other) {
		return ItemStack.areEqual(this.stack, other.getStack()) /* && fluid == other.fluid */ && mode == other.mode;
	}

	@Nonnull
	public List<RecipeCategory> getCategories() {
		RecipeRegistry recipeRegistry = AlwaysMoreItems.getRecipeRegistry();
		if (mode == Mode.INPUT) {
			return getInputCategories(recipeRegistry);
		} else if (mode == Mode.OUTPUT) {
			return getOutputCategories(recipeRegistry);
		} else {
			return recipeRegistry.getRecipeCategories();
		}
	}

	private List<RecipeCategory> getInputCategories(RecipeRegistry recipeRegistry) {
//		if (stack != null && fluid != null) {
//			List<IRecipeCategory> categories = new ArrayList<>(recipeRegistry.getRecipeCategoriesWithInput(stack));
//			categories.addAll(recipeRegistry.getRecipeCategoriesWithInput(fluid));
//			return ImmutableSet.copyOf(categories).asList();
//		}
		if (stack != null) {
			return recipeRegistry.getRecipeCategoriesWithInput(stack);
		}
//		else {
//			return recipeRegistry.getRecipeCategoriesWithInput(fluid);
//		}
		return null;
	}

	private List<RecipeCategory> getOutputCategories(RecipeRegistry recipeRegistry) {
//		if (stack != null && fluid != null) {
//			List<IRecipeCategory> categories = new ArrayList<>(recipeRegistry.getRecipeCategoriesWithOutput(stack));
//			categories.addAll(recipeRegistry.getRecipeCategoriesWithOutput(fluid));
//			return ImmutableSet.copyOf(categories).asList();
//		}
		if (stack != null) {
			return recipeRegistry.getRecipeCategoriesWithOutput(stack);
		}
//		else {
//			return recipeRegistry.getRecipeCategoriesWithOutput(fluid);
//		}
		return null;
	}

	@Nonnull
	public List<Object> getRecipes(RecipeCategory recipeCategory) {
		RecipeRegistry recipeRegistry = AlwaysMoreItems.getRecipeRegistry();
		if (mode == Mode.INPUT) {
			return getInputRecipes(recipeRegistry, recipeCategory);
		} else if (mode == Mode.OUTPUT) {
			return getOutputRecipes(recipeRegistry, recipeCategory);
		} else {
			return recipeRegistry.getRecipes(recipeCategory);
		}
	}

	private List<Object> getInputRecipes(RecipeRegistry recipeRegistry, RecipeCategory recipeCategory) {
//		if (stack != null && fluid != null) {
//			List<Object> recipes = new ArrayList<>(recipeRegistry.getRecipesWithInput(recipeCategory, stack));
//			recipes.addAll(recipeRegistry.getRecipesWithInput(recipeCategory, fluid));
//			return ImmutableSet.copyOf(recipes).asList();
//		}
		if (stack != null) {
			return recipeRegistry.getRecipesWithInput(recipeCategory, stack);
		}
//		else {
//			return recipeRegistry.getRecipesWithInput(recipeCategory, fluid);
//		}
		return null;
	}

	private List<Object> getOutputRecipes(RecipeRegistry recipeRegistry, RecipeCategory recipeCategory) {
//		if (stack != null && fluid != null) {
//			List<Object> recipes = new ArrayList<>(recipeRegistry.getRecipesWithOutput(recipeCategory, stack));
//			recipes.addAll(recipeRegistry.getRecipesWithOutput(recipeCategory, fluid));
//			return ImmutableSet.copyOf(recipes).asList();
//		}
		if (stack != null) {
			return recipeRegistry.getRecipesWithOutput(recipeCategory, stack);
		}
//		else {
//			return recipeRegistry.getRecipesWithOutput(recipeCategory, fluid);
//		}
		return null;
	}
}
