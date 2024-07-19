package net.glasslauncher.alwaysmoreitems.mixin;

import net.glasslauncher.alwaysmoreitems.api.SubProvider;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

@Mixin(SlabBlock.class)
public class MixinSlab implements SubProvider {

    @Override
    public List<ItemStack> getSubItems() {
        return List.of(
                new ItemStack(Block.class.cast(this), 1, 0),
                new ItemStack(Block.class.cast(this), 1, 1),
                new ItemStack(Block.class.cast(this), 1, 2),
                new ItemStack(Block.class.cast(this), 1, 3)
        );
    }
}
