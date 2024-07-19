package net.glasslauncher.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.alwaysmoreitems.AMITextRenderer;
import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableAnimated;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawableStatic;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class FuelRecipe extends VanillaRecipeWrapper {
	@Nonnull
	private final List<List<ItemStack>> inputs;
	@Nonnull
	private final String burnTimeString;
	@Nonnull
	private final IDrawableAnimated flame;

	public FuelRecipe(@Nonnull Collection<ItemStack> input, int burnTime) {
		List<ItemStack> inputList = new ArrayList<>(input);
		this.inputs = Collections.singletonList(inputList);
		this.burnTimeString = TranslationStorage.getInstance().get("gui.ami.category.fuel.burnTime", burnTime);

		IDrawableStatic flameDrawable = DrawableHelper.createDrawable("/gui/furnace.png", 176, 0, 14, 14);
		this.flame = DrawableHelper.createAnimatedDrawable(flameDrawable, burnTime, IDrawableAnimated.StartDirection.TOP, true);
	}

	@Nonnull
	@Override
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		AMITextRenderer.INSTANCE.draw(burnTimeString, 24, 12, Color.gray.getRGB());
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		flame.draw(minecraft, 2, 0);
	}
}
