package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.IAMISyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.minecraft.ShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin implements IAMISyncableRecipe {

    @Shadow
    private ItemStack output;

    @Shadow private int width;

    @Shadow private int height;

    @Shadow private ItemStack[] input;

    @Override
    public NbtCompound exportRecipe() {
        NbtCompound recipe = new NbtCompound();
        NbtList inputs = new NbtList();
        recipe.put("input", inputs);
        for (ItemStack itemStack : input) {
            if (itemStack == null) {
                inputs.add(new NbtCompound());
                continue;
            }
            NbtCompound item = new NbtCompound();
            itemStack.writeNbt(item);
            inputs.add(item);
        }
        NbtCompound item = new NbtCompound();
        output.writeNbt(item);
        recipe.put("output", item);

        recipe.putInt("width", width);
        recipe.putInt("height", height);

        recipe.putByte("type", (byte) 2);
        return recipe;
    }

    @Override
    public Identifier getPlugin() {
        return VanillaPlugin.ID;
    }
}
