package net.glasslauncher.mods.alwaysmoreitems.testmod.recipe;

import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

import javax.annotation.*;
import java.util.*;

public class DebugRecipe extends BlankRecipeWrapper {
    private final ButtonWidget button;

    public DebugRecipe() {
        this.button = new ButtonWidget(0, 110, 30, 40, 20, "test");
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        button.render(minecraft, mouseX, mouseY);
    }

//    @Override
//    public List<FluidStack> getFluidInputs() {
//        return Arrays.asList(
//                new FluidStack(FluidRegistry.WATER, 1000 + (int) (Math.random() * 1000)),
//                new FluidStack(FluidRegistry.LAVA, 1000 + (int) (Math.random() * 1000))
//        );
//    }

    @Nullable
    @Override
    public ArrayList<Object> getTooltip(int mouseX, int mouseY) {
        ArrayList<Object> tooltipStrings = new ArrayList<>();
        if (button.isMouseOver(Minecraft.INSTANCE, mouseX, mouseY)) {
            tooltipStrings.add("button tooltip!");
        } else {
            tooltipStrings.add(AMITextRenderer.BOLD + "tooltip debug");
        }
        tooltipStrings.add(mouseX + ", " + mouseY);
        return tooltipStrings;
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && button.isMouseOver(minecraft, mouseX, mouseY)) {
            Screen screen = new InventoryScreen(minecraft.player);
            minecraft.setScreen(screen);
            return true;
        }
        return false;
    }
}
