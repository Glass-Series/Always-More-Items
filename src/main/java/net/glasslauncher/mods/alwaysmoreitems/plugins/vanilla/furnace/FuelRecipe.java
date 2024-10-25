package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.AnimatedDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.StaticDrawable;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.util.HoverChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FuelRecipe extends VanillaRecipeWrapper implements SyncableRecipe {
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.##");
	@Nonnull
	private final List<List<ItemStack>> inputs;
	@Nonnull
	private final String burnTimeStringTicks;
	@Nonnull
	private final String burnTimeStringItems;
	@Nonnull
	private final String burnTimeStringSeconds;
	private final String burnTimeStringItemsFull;
	private final String burnTimeStringSecondsFull;
	private final int burnTimeStringItemsWidth;
	private final HoverChecker burnTimeStringItemsTooltipChecker;
	private final HoverChecker burnTimeStringSecondsTooltipChecker;
	@Nonnull
	private final AnimatedDrawable flame;
	private final int burnTime;

	public FuelRecipe(@Nonnull Collection<ItemStack> input, int burnTime) {
		List<ItemStack> inputList = new ArrayList<>(input);
		inputs = Collections.singletonList(inputList);
		this.burnTime = burnTime;
		if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
			burnTimeStringTicks = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime", burnTime);
			burnTimeStringItems = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.items", NUMBER_FORMAT.format(burnTime / 200f));
			burnTimeStringSeconds = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.seconds", NUMBER_FORMAT.format(burnTime / 20f));
			burnTimeStringItemsFull = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.items.full");
			burnTimeStringSecondsFull = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.seconds.full");
			burnTimeStringItemsWidth = AMITextRenderer.INSTANCE.getWidth(burnTimeStringItems);
			burnTimeStringItemsTooltipChecker = new HoverChecker(26, 35, 24, 24 + burnTimeStringItemsWidth);
			burnTimeStringSecondsTooltipChecker = new HoverChecker(26, 35, 24 + burnTimeStringItemsWidth + 8, 24 + burnTimeStringItemsWidth + AMITextRenderer.INSTANCE.getWidth(burnTimeStringSeconds) + 8);

			StaticDrawable flameDrawable = DrawableHelper.createDrawable("/gui/furnace.png", 176, 0, 14, 14);
			flame = DrawableHelper.createAnimatedDrawable(flameDrawable, burnTime, AnimatedDrawable.StartDirection.TOP, true);
		}
		else {
			flame = null;
			burnTimeStringTicks = null;
			burnTimeStringItems = null;
			burnTimeStringSeconds = null;
			burnTimeStringItemsFull = null;
			burnTimeStringSecondsFull = null;
			burnTimeStringItemsWidth = 0;
			burnTimeStringItemsTooltipChecker = null;
			burnTimeStringSecondsTooltipChecker = null;
		}
	}

	@Nonnull
	@Override
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		AMITextRenderer.INSTANCE.draw(burnTimeStringTicks, 24, 12, Color.DARK_GRAY.getRGB());
		AMITextRenderer.INSTANCE.draw(burnTimeStringItems, 24, 26, Color.GRAY.getRGB());
		AMITextRenderer.INSTANCE.draw(burnTimeStringSeconds, 24 + burnTimeStringItemsWidth + 8, 26, Color.GRAY.getRGB());
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		flame.draw(minecraft, 2, 0);
	}

	@Override
	public ArrayList<Object> getTooltip(int mouseX, int mouseY) {
		if(burnTimeStringItemsTooltipChecker.isOver(mouseX, mouseY)) {
			return new ArrayList<>(){{add(burnTimeStringItemsFull);}};
		}

		if (burnTimeStringSecondsTooltipChecker.isOver(mouseX, mouseY)) {
			return new ArrayList<>(){{add(burnTimeStringSecondsFull);}};
		}

		return null;
	}

	@Override
	public NbtCompound exportRecipe() {
		NbtCompound recipe = new NbtCompound();
		NbtList items = new NbtList();
		recipe.put("input", items);
		for (ItemStack itemStack : inputs.get(0)) {
			NbtCompound item = new NbtCompound();
			itemStack.writeNbt(item);
			items.add(item);
		}
		recipe.putInt("burnTime", burnTime);
		recipe.putByte("type", (byte) 6);
		return recipe;
	}

	@Override
	public Identifier getPlugin() {
		return VanillaPlugin.ID;
	}
}
