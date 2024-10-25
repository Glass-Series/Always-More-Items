package net.glasslauncher.mods.alwaysmoreitems.api.gui;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Helps set crafting-grid-style GuiItemStackGroup.
 * Get an instance from IGuiHelper.
 */
public interface CraftingGridHelper {

    void setInput(@Nonnull GuiItemStackGroup guiItemStacks, @Nonnull List input);

    void setInput(@Nonnull GuiItemStackGroup guiItemStacks, @Nonnull List input, int width, int height);

    void setOutput(@Nonnull GuiItemStackGroup guiItemStacks, @Nonnull List<ItemStack> output);

}
