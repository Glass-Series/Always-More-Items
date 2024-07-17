package net.glasslauncher.alwaysmoreitems.plugins.jei.debug;

import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.gui.IGuiItemStackGroup;
import net.glasslauncher.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.*;

public class DebugRecipeCategory implements IRecipeCategory {
	public static final int recipeWidth = 160;
	public static final int recipeHeight = 60;
	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;
	@Nonnull
	private final IDrawable tankBackground;
//	@Nonnull
//	private final IDrawable tankOverlay;

	public DebugRecipeCategory() {
		background = DrawableHelper.createBlankDrawable(recipeWidth, recipeHeight);
		localizedName = "debug";

		tankBackground = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/recipeBackground.png", 176, 0, 20, 55);
//		tankOverlay = guiHelper.createDrawable(backgroundTexture, 176, 55, 12, 47);
	}

	@Nonnull
	@Override
	public String getUid() {
		return "debug";
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		tankBackground.draw(minecraft);
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (input) {
                tooltip.add(slotIndex + " Input itemStack");
            } else {
                tooltip.add(slotIndex + " Output itemStack");
            }
        });

		guiItemStacks.init(0, false, 70, 0);
		guiItemStacks.init(1, true, 110, 0);
		guiItemStacks.set(0, new ItemStack(Item.WATER_BUCKET));
		guiItemStacks.set(1, new ItemStack(Item.LAVA_BUCKET));

//		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
//		guiFluidStacks.addTooltipCallback(new ITooltipCallback<FluidStack>() {
//			@Override
//			public void onTooltip(int slotIndex, boolean input, FluidStack ingredient, List<String> tooltip) {
//				if (input) {
//					tooltip.add(slotIndex + " Input fluidStack");
//				} else {
//					tooltip.add(slotIndex + " Output fluidStack");
//				}
//			}
//		});
//
//		guiFluidStacks.init(0, true, 4, 4, 12, 47, 2000, true, tankOverlay);
//		guiFluidStacks.init(1, true, 24, 0, 12, 47, 16000, true, null);
//		guiFluidStacks.init(2, false, 50, 0, 24, 24, 2000, true, tankOverlay);
//		guiFluidStacks.init(3, false, 90, 0, 12, 47, 100, false, tankOverlay);
//
//		List<FluidStack> fluidInputs = recipeWrapper.getFluidInputs();
//		guiFluidStacks.set(0, fluidInputs.get(0));
//		guiFluidStacks.set(1, fluidInputs.get(1));
//		guiFluidStacks.set(3, fluidInputs.get(0));
	}
}
