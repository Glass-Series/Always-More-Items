package net.glasslauncher.mods.alwaysmoreitems.api.recipe;

import net.minecraft.client.Minecraft;

import javax.annotation.*;
import java.util.*;

public abstract class BlankRecipeWrapper implements IRecipeWrapper {
    @Override
    public List getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List getOutputs() {
        return Collections.emptyList();
    }

// TODO: Implement once StationAPI gets a fluid API.
//	@Override
//	public List<FluidStack> getFluidInputs() {
//		return Collections.emptyList();
//	}
//
//	@Override
//	public List<FluidStack> getFluidOutputs() {
//		return Collections.emptyList();
//	}

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
