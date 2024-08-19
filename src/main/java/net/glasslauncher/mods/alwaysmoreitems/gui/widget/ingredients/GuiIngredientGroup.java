package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.TooltipCallback;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.minecraft.client.Minecraft;

import javax.annotation.*;
import java.util.*;

public abstract class GuiIngredientGroup<V, T extends GuiIngredient<V>> implements net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiIngredientGroup<V> {
    protected final int itemCycleOffset = (int) (Math.random() * 1000);
    @Nonnull
    protected final Map<Integer, T> guiIngredients = new HashMap<>();
    @Nonnull
    protected Focus focus = new Focus();
    @Nullable
    private TooltipCallback<V> tooltipCallback;

    /**
     * If focus is set and any of the guiIngredients contains focus
     * they will only display focus instead of rotating through all their values.
     */
    public void setFocus(@Nonnull Focus focus) {
        this.focus = focus;
    }

    @Override
    public void set(int slotIndex, @Nonnull Collection<V> values) {
        guiIngredients.get(slotIndex).set(values, focus);
    }

    @Override
    public void set(int slotIndex, @Nonnull V value) {
        guiIngredients.get(slotIndex).set(value, focus);
    }

    @Override
    public void addTooltipCallback(@Nonnull TooltipCallback<V> tooltipCallback) {
        this.tooltipCallback = tooltipCallback;
    }

    @Override
    @Nonnull
    public Map<Integer, T> getGuiIngredients() {
        return guiIngredients;
    }

    @Nullable
    public Focus getFocusUnderMouse(int mouseX, int mouseY) {
        for (T widget : guiIngredients.values()) {
            if (widget != null && widget.isMouseOver(mouseX, mouseY)) {
                return Focus.create(widget.get());
            }
        }
        return null;
    }

    @Nullable
    public T draw(@Nonnull Minecraft minecraft, int mouseX, int mouseY) {
        T hovered = null;
        for (T ingredient : guiIngredients.values()) {
            if (hovered == null && ingredient.isMouseOver(mouseX, mouseY)) {
                hovered = ingredient;
                hovered.setTooltipCallback(tooltipCallback);
            } else {
                ingredient.draw(minecraft);
            }
        }
        return hovered;
    }
}
