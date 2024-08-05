package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.*;
import java.util.*;

public class SmeltingRecipe extends VanillaRecipeWrapper implements SyncableRecipe {
	@Nonnull
	private final List<List<ItemStack>> input;
	@Nonnull
	private final List<ItemStack> outputs;

	public SmeltingRecipe(@Nonnull List<ItemStack> input, @Nonnull ItemStack output) {
		this.input = Collections.singletonList(input);
		this.outputs = Collections.singletonList(output);
	}

	@Nonnull
	public List<List<ItemStack>> getInputs() {
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
		input.get(0).get(0).writeNbt(itemNbt);
		nbtCompound.put("input", itemNbt);

		itemNbt = new NbtCompound();
		outputs.get(0).writeNbt(itemNbt);
		nbtCompound.put("output", itemNbt);

		nbtCompound.putByte("type", (byte) 5);

		return nbtCompound;
	}

	@Override
	public Identifier getPlugin() {
		return VanillaPlugin.ID;
	}
}
