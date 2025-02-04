package net.glasslauncher.mods.alwaysmoreitems.recipe;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class CraftingGridHelper implements net.glasslauncher.mods.alwaysmoreitems.api.gui.CraftingGridHelper {

    private final int craftInputSlot1;
    private final int craftOutputSlot;

    public CraftingGridHelper(int craftInputSlot1, int craftOutputSlot) {
        this.craftInputSlot1 = craftInputSlot1;
        this.craftOutputSlot = craftOutputSlot;
    }

    @Override
    public void setInput(@Nonnull GuiItemStackGroup guiItemStacks, @Nonnull List input) {
        int width, height;
        if (input.size() > 4) {
            width = height = 3;
        } else if (input.size() > 1) {
            width = height = 2;
        } else {
            width = height = 1;
        }

        setInput(guiItemStacks, input, width, height);
    }

    @Override
    public void setInput(@Nonnull GuiItemStackGroup guiItemStacks, @Nonnull List input, int width, int height) {
        for (int i = 0; i < input.size(); i++) {
            Object recipeItem = input.get(i);

            int index = i;
            if(input.size() != 9){
                index = getCraftingIndex(i, width, height);
            }

            List<ItemStack> itemStacks = AlwaysMoreItems.getStackHelper().toItemStackList(recipeItem);
            setInput(guiItemStacks, index, itemStacks);
        }
    }

    private int getCraftingIndex(int i, int width, int height) {
        int index;
        if (width == 1) {
            if (height == 3) {
                index = (i * 3) + 1;
            } else if (height == 2) {
                index = (i * 3) + 4;
            } else {
                index = 4;
            }
        } else if (height == 1) {
            index = i;// + 6;
        } else if (width == 2) {
            index = i;
            if (i > 1) {
                index++;
                if (i > 3) {
                    index++;
                }
            }
        } else if (height == 2) {
            index = i;// + 3;
        } else {
            index = i;
        }
        return index;
    }

    @Override
    public void setOutput(@Nonnull GuiItemStackGroup guiItemStacks, @Nonnull List<ItemStack> output) {
        guiItemStacks.set(craftOutputSlot, output);
    }

    private void setInput(@Nonnull GuiItemStackGroup guiItemStacks, int inputIndex, @Nonnull Collection<ItemStack> input) {
        guiItemStacks.set(craftInputSlot1 + inputIndex, input);
    }

}
