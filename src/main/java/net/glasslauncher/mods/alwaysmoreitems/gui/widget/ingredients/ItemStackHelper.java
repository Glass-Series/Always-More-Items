package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class ItemStackHelper implements IIngredientHelper<ItemStack> {
    @Override
    public Collection<ItemStack> expandSubtypes(Collection<ItemStack> contained) {
        return AlwaysMoreItems.getStackHelper().getAllSubtypes(contained);
    }

    @Override
    public ItemStack getMatch(Iterable<ItemStack> contained, @Nonnull Focus toMatch) {
        return AlwaysMoreItems.getStackHelper().containsStack(contained, toMatch.getStack());
    }
}
