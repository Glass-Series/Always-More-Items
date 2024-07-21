package net.glasslauncher.alwaysmoreitems.gui;

import net.glasslauncher.alwaysmoreitems.AMIConfig;
import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.Focus;
import net.glasslauncher.alwaysmoreitems.RenderHelper;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.gui.IRecipeLayout;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.gui.widget.RecipeTransferButton;
import net.glasslauncher.alwaysmoreitems.gui.widget.ingredients.GuiIngredient;
import net.glasslauncher.alwaysmoreitems.gui.widget.ingredients.GuiItemStackGroup;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import javax.annotation.*;
import java.util.*;

public class RecipeLayout implements IRecipeLayout {
	private static final int RECIPE_BUTTON_SIZE = 12;
	public static final int recipeTransferButtonIndex = 100;

	@Nonnull
	private final IRecipeCategory recipeCategory;
	@Nonnull
	private final GuiItemStackGroup guiItemStackGroup;
//	@Nonnull
//	private final GuiFluidStackGroup guiFluidStackGroup;
	@Nonnull
	private final RecipeTransferButton recipeTransferButton;
	@Nonnull
	private final IRecipeWrapper recipeWrapper;

	private final int posX;
	private final int posY;

	public RecipeLayout(int index, int posX, int posY, @Nonnull IRecipeCategory recipeCategory, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull Focus focus) {
		this.recipeCategory = recipeCategory;
		this.guiItemStackGroup = new GuiItemStackGroup();
//		this.guiFluidStackGroup = new GuiFluidStackGroup();
		int width = recipeCategory.getBackground().getWidth();
		int height = recipeCategory.getBackground().getHeight();
		this.recipeTransferButton = new RecipeTransferButton(recipeTransferButtonIndex + index, posX + width + 2, posY + height - RECIPE_BUTTON_SIZE, RECIPE_BUTTON_SIZE, RECIPE_BUTTON_SIZE, "+");
		this.posX = posX;
		this.posY = posY;

		this.recipeWrapper = recipeWrapper;
		this.guiItemStackGroup.setFocus(focus);
//		this.guiFluidStackGroup.setFocus(focus);
		this.recipeCategory.setRecipe(this, recipeWrapper);
	}

	public void draw(@Nonnull Minecraft minecraft, int mouseX, int mouseY) {
		GL11.glPushMatrix();
		GL11.glTranslatef(posX, posY, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		IDrawable background = recipeCategory.getBackground();
		background.draw(minecraft);
		recipeCategory.drawExtras(minecraft);

		if (AMIConfig.isRecipeAnimationsEnabled()) {
			recipeCategory.drawAnimations(minecraft);
			recipeWrapper.drawAnimations(minecraft, background.getWidth(), background.getHeight());
		}

		GL11.glTranslatef(-posX, -posY, 0.0F);
		recipeTransferButton.render(minecraft, mouseX, mouseY);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glTranslatef(posX, posY, 0.0F);
		GL11.glDisable(GL11.GL_LIGHTING);

		final int recipeMouseX = mouseX - posX;
		final int recipeMouseY = mouseY - posY;

		try {
			recipeWrapper.drawInfo(minecraft, background.getWidth(), background.getHeight(), recipeMouseX, recipeMouseY);
		} catch (AbstractMethodError ignored) {
			// older wrappers don't have this method
		}
		recipeWrapper.drawInfo(minecraft, background.getWidth(), background.getHeight(), 0, 0);

		RenderHelper.enableItemLighting();
		GuiIngredient hoveredItemStack = guiItemStackGroup.draw(minecraft, recipeMouseX, recipeMouseY);
		RenderHelper.disableItemLighting();
//		GuiIngredient hoveredFluidStack = guiFluidStackGroup.draw(minecraft, recipeMouseX, recipeMouseY);

		if (hoveredItemStack != null) {
			RenderHelper.enableItemLighting();
			hoveredItemStack.drawHovered(minecraft, recipeMouseX, recipeMouseY);
			RenderHelper.disableItemLighting();
//		} else if (hoveredFluidStack != null) {
//			hoveredFluidStack.drawHovered(minecraft, recipeMouseX, recipeMouseY);
		} else if (recipeMouseX >= 0 && recipeMouseX < background.getWidth() && recipeMouseY >= 0 && recipeMouseY < background.getHeight()) {
			List<String> tooltipStrings = null;
			try {
				tooltipStrings = recipeWrapper.getTooltipStrings(recipeMouseX, recipeMouseY);
			} catch (AbstractMethodError ignored) {
				// older wrappers don't have this method
			}
			if (tooltipStrings != null && !tooltipStrings.isEmpty()) {
				DrawableHelper.drawTooltip(tooltipStrings, recipeMouseX, recipeMouseY);
			}
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPopMatrix();
	}

	public Focus getFocusUnderMouse(int mouseX, int mouseY) {
		Focus focus = guiItemStackGroup.getFocusUnderMouse(mouseX - posX, mouseY - posY);
//		if (focus == null) {
//			focus = guiFluidStackGroup.getFocusUnderMouse(mouseX - posX, mouseY - posY);
//		}
		return focus;
	}

	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		try {
			return recipeWrapper.handleClick(minecraft, mouseX - posX, mouseY - posY, mouseButton);
		} catch (AbstractMethodError ignored) { // older wrappers don't have this method
			return false;
		}
	}

	@Override
	@Nonnull
	public GuiItemStackGroup getItemStacks() {
		return guiItemStackGroup;
	}

//	@Override
//	@Nonnull
//	public IGuiFluidStackGroup getFluidStacks() {
//		return guiFluidStackGroup;
//	}

	@Override
	public void setRecipeTransferButton(int posX, int posY) {
		recipeTransferButton.x = posX + this.posX;
		recipeTransferButton.y = posY + this.posY;
	}

	@Nonnull
	public RecipeTransferButton getRecipeTransferButton() {
		return recipeTransferButton;
	}

	@Nonnull
	public IRecipeWrapper getRecipeWrapper() {
		return recipeWrapper;
	}

	@Nonnull
	public IRecipeCategory getRecipeCategory() {
		return recipeCategory;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
}
