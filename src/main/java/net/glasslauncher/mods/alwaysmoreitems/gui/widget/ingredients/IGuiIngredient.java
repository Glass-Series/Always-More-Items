package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.minecraft.client.Minecraft;

import javax.annotation.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public interface IGuiIngredient<T> {
    void set(@Nonnull T contained, @Nonnull Focus focus);

    void set(@Nonnull Collection<T> contained, @Nonnull Focus focus);

    void clear();

    @Nullable
    T get();

    @Nonnull
    List<T> getAll();

    boolean isInput();

    boolean isMouseOver(int mouseX, int mouseY);

    void draw(@Nonnull Minecraft minecraft);

    void drawHovered(@Nonnull Minecraft minecraft, int mouseX, int mouseY);

    /**
     * Offset is in screen coordinates, unlike the other draw methods
     */
    void drawHighlight(@Nonnull Minecraft minecraft, Color color, int xOffset, int yOffset);
}
