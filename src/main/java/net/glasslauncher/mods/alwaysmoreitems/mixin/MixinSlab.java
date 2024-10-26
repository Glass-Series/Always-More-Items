package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.SubItemProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SlabBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(SlabBlockItem.class)
public class MixinSlab {

    @Unique
    @SubItemProvider
    public List<ItemStack> getSubItems() {
        return List.of(
                new ItemStack(Item.class.cast(this), 1, 0),
                new ItemStack(Item.class.cast(this), 1, 1),
                new ItemStack(Item.class.cast(this), 1, 2),
                new ItemStack(Item.class.cast(this), 1, 3)
        );
    }
}
