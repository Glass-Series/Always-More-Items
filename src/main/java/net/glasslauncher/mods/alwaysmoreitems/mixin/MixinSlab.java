package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.SubProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SlabBlockItem;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

@Mixin(SlabBlockItem.class)
public class MixinSlab implements SubProvider {

    @Override
    public List<ItemStack> getSubItems() {
        return List.of(
                new ItemStack(Item.class.cast(this), 1, 0),
                new ItemStack(Item.class.cast(this), 1, 1),
                new ItemStack(Item.class.cast(this), 1, 2),
                new ItemStack(Item.class.cast(this), 1, 3)
        );
    }
}
