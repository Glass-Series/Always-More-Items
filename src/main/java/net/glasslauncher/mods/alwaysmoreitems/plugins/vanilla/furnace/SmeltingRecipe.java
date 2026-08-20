package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import com.mojang.datafixers.util.Either;
import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class SmeltingRecipe extends VanillaRecipeWrapper implements SyncableRecipe {
    @Nonnull
    private final List<List<Either<TagKey<Item>, ItemStack>>> input;
    @Nonnull
    private final List<ItemStack> outputs;

    public SmeltingRecipe(@Nonnull List<Either<TagKey<Item>, ItemStack>> input, @Nonnull ItemStack output) {
        this.input = Collections.singletonList(input);
        this.outputs = Collections.singletonList(output);
    }

    @Nonnull
    public List<List<Either<TagKey<Item>, ItemStack>>> getInputs() {
        return input;
    }

    @Nonnull
    public List<ItemStack> getOutputs() {
        return outputs;
    }

    @Override
    public NbtCompound exportRecipe() {
        NbtCompound nbtCompound = new NbtCompound();

        NbtCompound itemNbt = new NbtCompound();
        Either<TagKey<Item>, ItemStack> obj = input.get(0).get(0);

        obj.map(l -> {
            itemNbt.putString("tag", l.id().toString());
            return null;
        }, r -> r.writeNbt(itemNbt));
        nbtCompound.put("input", itemNbt);

        NbtCompound outputNbt = new NbtCompound();
        outputs.get(0).writeNbt(outputNbt);
        nbtCompound.put("output", outputNbt);

        nbtCompound.putByte("type", (byte) 5);

        return nbtCompound;
    }

    @Override
    public Identifier getPlugin() {
        return VanillaPlugin.ID;
    }
}
