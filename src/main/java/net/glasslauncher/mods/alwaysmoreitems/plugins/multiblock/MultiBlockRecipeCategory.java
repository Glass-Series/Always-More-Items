package net.glasslauncher.mods.alwaysmoreitems.plugins.multiblock;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.GuiIngredient;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.IGuiIngredient;
import net.glasslauncher.mods.gcapi3.api.CharacterUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class MultiBlockRecipeCategory implements RecipeCategory {

    private static final int[] COLORS = new int[] {
            CharacterUtils.getIntFromColour(new Color(11, 11, 11)),
            CharacterUtils.getIntFromColour(new Color(27, 27, 27)),
            CharacterUtils.getIntFromColour(new Color(43, 43, 43)),
            CharacterUtils.getIntFromColour(new Color(59, 59, 59)),
            CharacterUtils.getIntFromColour(new Color(75, 75, 75)),
            CharacterUtils.getIntFromColour(new Color(91, 91, 91)),
            CharacterUtils.getIntFromColour(new Color(107, 107, 107)),
            CharacterUtils.getIntFromColour(new Color(123, 123, 123)),
            CharacterUtils.getIntFromColour(new Color(139, 139, 139)),
    };

    private final AMIDrawable background = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 0, 0, 162, 130);

    private final AMIDrawable costTop = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 0, 25, 5);
    private final AMIDrawable costExtensionTop = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 0, 23, 5);
    private final AMIDrawable costMiddle = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 5, 25, 18);
    private final AMIDrawable costExtensionMiddle = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 5, 23, 18);
    private final AMIDrawable costBottom = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 23, 25, 5);
    private final AMIDrawable costExtensionBottom = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 28, 23, 5);

    MultiBlockRecipeWrapper recipeWrapper;
    GuiItemStackGroup itemStackGroup;
    Map<Integer, List<ItemStack>> costPerLayer;
    int descriptionFade;
    int descriptionFadeTick;

    @Override
    public @NotNull String getUid() {
        return "multi_block";
    }

    @Override
    public @NotNull String getTitle() {
        return "Multi Block Structures";
    }

    @Override
    public @NotNull AMIDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        int x = -28;
        int y = -33;

        int maxRows = 8;

        for(IGuiIngredient ingredient : this.itemStackGroup.getGuiIngredients().values()){
            ingredient.clear();
        }

        if(minecraft.currentScreen.height > 300) {
            y -= 45;
            maxRows =  13;
        }

        int columns = (int) Math.ceil((double) costPerLayer.get(recipeWrapper.currentLayer).size() / (double)maxRows);
        int rows = Math.min(costPerLayer.get(recipeWrapper.currentLayer).size(), maxRows);

        int currentCostIndex = 0;
        int startY = y;
        for(int currentColumn = 0; currentColumn < columns; currentColumn++) {
            y = startY;
            if(currentColumn == 0){
                costTop.draw(minecraft, x, y);
            } else {
                costExtensionTop.draw(minecraft, x, y);
            }
            y += 5;
            for(int currentRow = 0; currentRow < rows; currentRow++) {
                if(currentColumn == 0){
                    costMiddle.draw(minecraft, x, y);
                }
                else {
                    costExtensionMiddle.draw(minecraft, x, y);
                }
                if(currentCostIndex < costPerLayer.get(recipeWrapper.currentLayer).size()){
                    itemStackGroup.init(currentCostIndex, true, x + 5, y);
                    itemStackGroup.set(currentCostIndex, costPerLayer.get(recipeWrapper.currentLayer).get(currentCostIndex));
                }
                y += 18;
                currentCostIndex++;
            }
            if(currentColumn == 0) {
                costBottom.draw(minecraft, x, y);
            } else {
                costExtensionBottom.draw(minecraft, x, y);
            }
            x -= 18;
        }
        int color = COLORS[descriptionFade];
        minecraft.textRenderer.draw(TranslationStorage.getInstance().get("gui.alwaysmoreitems.multiblock.description_hint"), 2, 118, color);
        if (descriptionFadeTick >= 20) {
            descriptionFadeTick = 0;
            descriptionFade++;
        } else if (descriptionFade < 8) {
            descriptionFadeTick++;
        }
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@NotNull RecipeLayout recipeLayout, @NotNull RecipeWrapper recipeWrapper) {
        this.recipeWrapper = (MultiBlockRecipeWrapper)recipeWrapper;
        this.costPerLayer = ((MultiBlockRecipeWrapper)recipeWrapper).getCostPerLayer();
        this.itemStackGroup = recipeLayout.getItemStacks();
        descriptionFadeTick = 0;
        descriptionFade = 0;
    }
}
