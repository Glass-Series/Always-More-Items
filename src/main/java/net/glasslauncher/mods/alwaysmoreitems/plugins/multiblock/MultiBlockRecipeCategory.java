package net.glasslauncher.mods.alwaysmoreitems.plugins.multiblock;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.glasslauncher.mods.gcapi3.api.CharacterUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class MultiBlockRecipeCategory implements RecipeCategory {
    private final AMIDrawable background = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 0, 0, 162, 130);

    private final AMIDrawable costTop = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 0, 25, 5);
    private final AMIDrawable costExtensionTop = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 0, 23, 5);
    private final AMIDrawable costMiddle = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 5, 25, 18);
    private final AMIDrawable costExtensionMiddle = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 5, 23, 18);
    private final AMIDrawable costBottom = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 23, 25, 5);
    private final AMIDrawable costExtensionBottom = DrawableHelper.createDrawable("/assets/alwaysmoreitems/stationapi/textures/gui/multiblock.png", 162, 28, 23, 5);

    GuiItemStackGroup itemStackGroup;
    List<ItemStack> cost;
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

        if(minecraft.currentScreen.height > 300) {
            y -= 45;
            maxRows =  13;
        }

        int columns = (int) Math.ceil((double) cost.size() / (double)maxRows);
        int rows = Math.min(cost.size(), maxRows);

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
                if(currentCostIndex < cost.size()){
                    itemStackGroup.init(currentCostIndex, true, x + 5, y);
                    itemStackGroup.setFromRecipe(currentCostIndex, cost.get(currentCostIndex));
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
        Color color = switch (descriptionFade) {
            case 0:
                yield new Color(0x0B, 0x0B, 0x0B);
            case 1:
                yield new Color(0x1B, 0x1B, 0x1B);
            case 2:
                yield new Color(0x2B, 0x2B, 0x2B);
            case 3:
                yield new Color(0x3B, 0x3B, 0x3B);
            case 4:
                yield new Color(0x4B, 0x4B, 0x4B);
            case 5:
                yield new Color(0x5B, 0x5B, 0x5B);
            case 6:
                yield new Color(0x6B, 0x6B, 0x6B);
            case 7:
                yield new Color(0x7B, 0x7B, 0x7B);
            case 8:
                yield new Color(0x8B, 0x8B, 0x8B);
            default:
                yield Color.BLACK;
        };
        minecraft.textRenderer.draw(TranslationStorage.getInstance().get("gui.alwaysmoreitems.multiblock.description_hint"), 2, 118, CharacterUtils.getIntFromColour(color));
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
        this.cost = ((MultiBlockRecipeWrapper)recipeWrapper).getCost();
        this.itemStackGroup = recipeLayout.getItemStacks();
        descriptionFadeTick = 0;
        descriptionFade = 0;
    }
}
