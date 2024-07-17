package net.glasslauncher.alwaysmoreitems.gui.widget.ingredients;

import net.minecraft.client.Minecraft;

import javax.annotation.*;
import java.util.*;

public interface IIngredientRenderer<T> {
	void draw(@Nonnull Minecraft minecraft, int xPosition, int yPosition, @Nullable T value);

	@Nonnull
	List<String> getTooltip(@Nonnull Minecraft minecraft, @Nonnull T value);
}
