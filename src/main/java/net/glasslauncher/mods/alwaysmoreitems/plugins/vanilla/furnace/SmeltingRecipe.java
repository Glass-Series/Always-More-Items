package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

public class SmeltingRecipe extends VanillaRecipeWrapper {
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
}
