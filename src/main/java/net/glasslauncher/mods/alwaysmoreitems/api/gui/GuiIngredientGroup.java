package net.glasslauncher.mods.alwaysmoreitems.api.gui;

import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients.IGuiIngredient;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * IGuiIngredientGroup displays recipe ingredients in a gui.
 * <p>
 * If multiple ingredients are set for one index, they will be displayed in rotation.
 *
 * @see GuiItemStackGroup and IGuiFluidStackGroup
 */
public interface GuiIngredientGroup<T> {
    /**
     * Set the ingredient at slotIndex to a rotating collection of ingredients.
     */
    void set(int slotIndex, @Nonnull Collection<T> ingredients);

    /**
     * Set the ingredient at slotIndex to a specific ingredient.
     */
    void set(int slotIndex, @Nonnull T ingredient);

    /**
     * Add a callback to alter the tooltip for these ingredients.
     */
    void addTooltipCallback(@Nonnull TooltipCallback<T> tooltipCallback);

    /**
     * Get the ingredients after they have been set.
     * Used by recipe transfer handlers.
     */
    Map<Integer, ? extends IGuiIngredient<T>> getGuiIngredients();
}
