package net.glasslauncher.mods.alwaysmoreitems.plugins.ami.description;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ItemDescriptionRecipe implements RecipeWrapper {
	private static final int lineSpacing = 2;
	@Nonnull
	private final List<String> description;
	@Nonnull
	private final List<List<ItemStack>> outputs;
	@Nonnull
	private final AMIDrawable slotDrawable;

	public static List<ItemDescriptionRecipe> create(@Nonnull List<ItemStack> itemStacks, String... descriptionKeys) {
		List<ItemDescriptionRecipe> recipes = new ArrayList<>();

		List<String> descriptionLines = translateDescriptionLines(descriptionKeys);
		descriptionLines = expandNewlines(descriptionLines);
		descriptionLines = wrapDescriptionLines(descriptionLines);
		final int lineCount = descriptionLines.size();

		final int maxLinesPerPage = (ItemDescriptionRecipeCategory.recipeHeight - 20) / (AMITextRenderer.FONT_HEIGHT + lineSpacing);
		final int pageCount = MathUtil.divideCeil(lineCount, maxLinesPerPage);
		for (int i = 0; i < pageCount; i++) {
			int startLine = i * maxLinesPerPage;
			int endLine = Math.min((i + 1) * maxLinesPerPage, lineCount);
			List<String> description = descriptionLines.subList(startLine, endLine);
			ItemDescriptionRecipe recipe = new ItemDescriptionRecipe(itemStacks, description);
			recipes.add(recipe);
		}

		return recipes;
	}

	@Nonnull
	private static List<String> translateDescriptionLines(String... descriptionKeys) {
		List<String> descriptionLines = new ArrayList<>();
		for (String descriptionKey : descriptionKeys) {
			String translatedLine = TranslationStorage.getInstance().get(descriptionKey);
			descriptionLines.add(translatedLine);
		}
		return descriptionLines;
	}

	@Nonnull
	private static List<String> expandNewlines(@Nonnull List<String> descriptionLines) {
		List<String> descriptionLinesExpanded = new ArrayList<>();
		for (String descriptionLine : descriptionLines) {
			String[] descriptionLineExpanded = descriptionLine.split("\\\\n");
			Collections.addAll(descriptionLinesExpanded, descriptionLineExpanded);
		}
		return descriptionLinesExpanded;
	}

	@Nonnull
	private static List<String> wrapDescriptionLines(@Nonnull List<String> descriptionLines) {
		if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
			return Collections.emptyList();
		}
		List<String> descriptionLinesWrapped = new ArrayList<>();
		for (String descriptionLine : descriptionLines) {
			List<String> textLines = AMITextRenderer.INSTANCE.listFormattedStringToWidth(descriptionLine, ItemDescriptionRecipeCategory.recipeWidth);
			descriptionLinesWrapped.addAll(textLines);
		}
		return descriptionLinesWrapped;
	}

	public ItemDescriptionRecipe(@Nonnull List<ItemStack> itemStacks, @Nonnull List<String> description) {
		this.description = description;
		this.outputs = Collections.singletonList(itemStacks);
		slotDrawable = DrawableHelper.createDrawable("/gui/furnace.png", 55, 16, 18, 18);
	}

	@Override
	public List<?> getInputs() {
		return Collections.emptyList();
	}

	@Nonnull
	@Override
	public List<List<ItemStack>> getOutputs() {
		return outputs;
	}

//	@Override
//	public List<FluidStack> getFluidInputs() {
//		return super.getFluidInputs();
//	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		int xPos = (recipeWidth - slotDrawable.getWidth()) / 2;
		int yPos = 0;
		slotDrawable.draw(minecraft, xPos, yPos);
		xPos = 0;
		yPos += slotDrawable.getHeight() + 4;

		for (String descriptionLine : description) {
			AMITextRenderer.INSTANCE.draw(descriptionLine, xPos, yPos, Color.black.getRGB());
			yPos += AMITextRenderer.FONT_HEIGHT + lineSpacing;
		}
	}

	@Override
	public void drawAnimations(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight) {

	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.emptyList();
	}

	@Override
	public boolean handleClick(@NotNull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	@Nonnull
	public List<String> getDescription() {
		return description;
	}

	// TODO: Post 1.0, implement our own serverside translationstorage so we can send these. Might want to make the client send it's current translation.
//	@Override
//	public NbtCompound exportRecipe() {
//		NbtCompound recipe = new NbtCompound();
//		NbtList items = new NbtList();
//		recipe.put("output", items);
//		for (ItemStack itemStack : outputs.get(0)) {
//			NbtCompound item = new NbtCompound();
//			itemStack.writeNbt(item);
//			items.add(item);
//		}
//		NbtList description = new NbtList();
//		this.description.forEach(string -> description.add(new NbtString(string)));
//		recipe.put("description", description);
//		return recipe;
//	}
//
//	@Override
//	public Identifier getPlugin() {
//		return AMIPlugin.ID;
//	}
}
