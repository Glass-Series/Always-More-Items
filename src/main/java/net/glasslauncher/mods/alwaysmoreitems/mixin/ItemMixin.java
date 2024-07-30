package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.SubProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

@Mixin(Item.class)
public class ItemMixin implements SubProvider {

    @Override
    public List<ItemStack> getSubItems() {
        return Collections.emptyList();
    }
}
