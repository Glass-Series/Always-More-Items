package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin implements SyncableRecipe {


    @Shadow @Final private List input;

    @Shadow @Final private ItemStack output;

    @Override
    public NbtCompound exportRecipe() {
        NbtCompound recipe = new NbtCompound();
        NbtList inputs = new NbtList();
        recipe.put("input", inputs);
        //noinspection unchecked
        for (ItemStack itemStack : (List<ItemStack>) input) {
            NbtCompound item = new NbtCompound();
            itemStack.writeNbt(item);
            inputs.add(item);
        }
        NbtCompound item = new NbtCompound();
        output.writeNbt(item);
        recipe.put("output", item);

        recipe.putByte("type", (byte) 1);
        return recipe;
    }

    @Override
    public Identifier getPlugin() {
        return VanillaPlugin.ID;
    }
}
