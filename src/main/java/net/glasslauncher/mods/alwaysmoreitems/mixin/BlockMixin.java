package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.SubProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.*;

@Mixin(Block.class)
public class BlockMixin implements SubProvider {

    @Override
    public List<ItemStack> getSubItems() {
        return Collections.emptyList();
    }
}
