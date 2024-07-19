package net.glasslauncher.alwaysmoreitems.mixin;

import net.glasslauncher.alwaysmoreitems.api.SubProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

@Mixin(Block.class)
public class BlockMixin implements SubProvider {
    
    @Override
    public List<ItemStack> getSubItems() {
        return Collections.singletonList(new ItemStack(Item.class.cast(this)));
    }
}