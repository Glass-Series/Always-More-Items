package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public interface IIngredientRenderer<T> {
    void draw(@Nonnull Minecraft minecraft, int xPosition, int yPosition, @Nullable T value);

    @Nonnull
    ArrayList<Object> getTooltip(@Nonnull Minecraft minecraft, @Nonnull T value);
}
