package net.glasslauncher.mods.alwaysmoreitems.gui;

import lombok.Getter;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.RecipeTransferButton;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.GuiIngredient;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeLayout implements net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout {
    private static final int RECIPE_BUTTON_SIZE = 12;
    public static final int recipeTransferButtonIndex = 100;

    @Nonnull
    private final RecipeCategory recipeCategory;
    @Nonnull
    private final GuiItemStackGroup guiItemStackGroup;
//    @Nonnull
//    private final GuiFluidStackGroup guiFluidStackGroup;
    @Nonnull
    private final RecipeTransferButton recipeTransferButton;
    @Nonnull
    private final RecipeWrapper recipeWrapper;

    @Getter
    private final int posX;
    @Getter
    private final int posY;

    public RecipeLayout(int index, int posX, int posY, @Nonnull RecipeCategory recipeCategory, @Nonnull RecipeWrapper recipeWrapper, @Nonnull Focus focus) {
        this.recipeCategory = recipeCategory;
        this.guiItemStackGroup = new GuiItemStackGroup();
//        this.guiFluidStackGroup = new GuiFluidStackGroup();
        int width = recipeCategory.getBackground().getWidth();
        int height = recipeCategory.getBackground().getHeight();
        this.recipeTransferButton = new RecipeTransferButton(recipeTransferButtonIndex + index, posX + width + 2, posY + height - RECIPE_BUTTON_SIZE, RECIPE_BUTTON_SIZE, RECIPE_BUTTON_SIZE, "gui.alwaysmoreitems.transferRecipesCharacter");
        this.posX = posX;
        this.posY = posY;

        this.recipeWrapper = recipeWrapper;
        this.guiItemStackGroup.setFocus(focus);
//        this.guiFluidStackGroup.setFocus(focus);
        this.recipeCategory.setRecipe(this, recipeWrapper);
    }

    public void draw(@Nonnull Minecraft minecraft, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        AMIDrawable background = recipeCategory.getBackground();
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
//        GuiIngredient hoveredFluidStack = guiFluidStackGroup.draw(minecraft, recipeMouseX, recipeMouseY);

        if (hoveredItemStack != null) {
            RenderHelper.enableItemLighting();
            hoveredItemStack.drawHovered(minecraft, mouseX, mouseY);
            RenderHelper.disableItemLighting();
//        } else if (hoveredFluidStack != null) {
//            hoveredFluidStack.drawHovered(minecraft, recipeMouseX, recipeMouseY);
        } else if (recipeMouseX >= 0 && recipeMouseX < background.getWidth() && recipeMouseY >= 0 && recipeMouseY < background.getHeight()) {
            List<Object> tooltipStrings = null;
            try {
                tooltipStrings = recipeWrapper.getTooltip(recipeMouseX, recipeMouseY);
            } catch (AbstractMethodError ignored) {
                // older wrappers don't have this method
            }
            if (tooltipStrings != null && !tooltipStrings.isEmpty()) {
                Tooltip.INSTANCE.setTooltip(tooltipStrings, mouseX, mouseY);
            }
        }

        GL11.glPopMatrix();
    }

    public Focus getFocusUnderMouse(int mouseX, int mouseY) {
        Focus focus = guiItemStackGroup.getFocusUnderMouse(mouseX - posX, mouseY - posY);
//        if (focus == null) {
//            focus = guiFluidStackGroup.getFocusUnderMouse(mouseX - posX, mouseY - posY);
//        }
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

//    @Override
//    @Nonnull
//    public IGuiFluidStackGroup getFluidStacks() {
//        return guiFluidStackGroup;
//    }

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
    public RecipeWrapper getRecipeWrapper() {
        return recipeWrapper;
    }

    @Nonnull
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }
}
