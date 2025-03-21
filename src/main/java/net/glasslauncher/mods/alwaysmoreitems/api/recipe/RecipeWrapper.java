package net.glasslauncher.mods.alwaysmoreitems.api.recipe;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper around a normal recipe with methods that allow AMI can make sense of it.
 * Implementers will have to create a wrapper for each type of recipe they have.
 */
public interface RecipeWrapper {

    /**
     * Return a list of recipe inputs.
     * Each element can be an ItemStack, null, or a List of ItemStacks.
     */
    List<?> getInputs();

    /**
     * Return a list of recipe inputs.
     * Each element can be an ItemStack, null, or a List of ItemStacks.
     */
    List<?> getOutputs();

// TODO: Implement once StationAPI gets a fluid API.
//    /** Return a list of recipe fluid inputs. */
//    List<FluidStack> getFluidInputs();
//
//    /** Return a list of recipe fluid outputs. */
//    List<FluidStack> getFluidOutputs();

    /**
     * Draw additional info about the recipe.
     * Use the mouse position for things like button highlights.
     * Tooltips are handled by IRecipeWrapper.getTooltipStrings()
     *
     * @param mouseX the X position of the mouse, relative to the recipe.
     * @param mouseY the Y position of the mouse, relative to the recipe.
     * @since AMI 2.19.0
     */
    void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY);

    /**
     * Draw animations involving the recipe. Can be disabled in the config.
     */
    void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight);

    /**
     * Get the tooltip for whatever's under the mouse.
     * ItemStack and fluid tooltips are already handled by AMI, this is for anything else.
     *
     * @param mouseX the X position of the mouse, relative to the recipe.
     * @param mouseY the Y position of the mouse, relative to the recipe.
     * @return tooltip strings. If there is no tooltip at this position, return null or an empty list.
     */
    @Nullable
    ArrayList<Object> getTooltip(int mouseX, int mouseY);

    /**
     * Called when a player clicks the recipe.
     * Useful for implementing buttons, hyperlinks, and other interactions to your recipe.
     *
     * @param mouseX      the X position of the mouse, relative to the recipe.
     * @param mouseY      the Y position of the mouse, relative to the recipe.
     * @param mouseButton the current mouse event button.
     * @return true if the click was handled, false otherwise
     * @since AMI 2.19.0
     */
    boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton);
}
