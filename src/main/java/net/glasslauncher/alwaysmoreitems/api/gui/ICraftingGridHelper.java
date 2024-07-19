package net.glasslauncher.alwaysmoreitems.api.gui;

import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

/**
 * Helps set crafting-grid-style GuiItemStackGroup.
 * Get an instance from IGuiHelper.
 */
public interface ICraftingGridHelper {

	void setInput(@Nonnull IGuiItemStackGroup guiItemStacks, @Nonnull List input);

	void setInput(@Nonnull IGuiItemStackGroup guiItemStacks, @Nonnull List input, int width, int height);

	void setOutput(@Nonnull IGuiItemStackGroup guiItemStacks, @Nonnull List<ItemStack> output);

}
