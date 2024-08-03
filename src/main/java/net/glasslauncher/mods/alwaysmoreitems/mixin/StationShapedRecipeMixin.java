package net.glasslauncher.mods.alwaysmoreitems.mixin;

import com.mojang.datafixers.util.Either;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMISyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StationShapedRecipe.class)
public class StationShapedRecipeMixin implements IAMISyncableRecipe {

    @Shadow @Final private ItemStack output;

    @Shadow @Final private Either<TagKey<Item>, ItemStack>[] grid;

    @Shadow @Final private int width;

    @Shadow @Final private int height;

    @Override
    public NbtCompound exportRecipe() {
        NbtCompound recipe = new NbtCompound();
        NbtList inputs = new NbtList();
        recipe.put("input", inputs);
        for (Either<TagKey<Item>, ItemStack> either : grid) {
            if (either == null) {
                inputs.add(new NbtCompound());
            }
            else if (either.right().isPresent()) {
                NbtCompound item = new NbtCompound();
                either.right().get().writeNbt(item);
                inputs.add(item);
            }
            else {
                NbtCompound id = new NbtCompound();
                id.putString("identifier", either.left().get().id().toString());
                inputs.add(id);
            }
        }
        NbtCompound item = new NbtCompound();
        output.writeNbt(item);
        recipe.put("output", item);

        recipe.putInt("width", width);
        recipe.putInt("height", height);

        recipe.putByte("type", (byte) 4);
        return recipe;
    }

    @Override
    public Identifier getPlugin() {
        return VanillaPlugin.ID;
    }
}
