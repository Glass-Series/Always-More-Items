package net.glasslauncher.alwaysmoreitems.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

/**
 * Should this be part of a bugfix mod? Probably, but this would cause so many issues in SP that it just makes more sense to do it myself.
 */
@Mixin(CraftingScreenHandler.class)
public abstract class FixCraftingTableGhostItemsMixin extends ScreenHandler {

    @Shadow public CraftingInventory input;

    @Shadow private World world;

    @Inject(method = "onClosed", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, require = 0)
    public void removedFix(PlayerEntity player, CallbackInfo ci) {
        if (!world.isRemote) {
            Arrays.stream(input.stacks).forEach(player::dropItem);
        }
        input.stacks = new ItemStack[input.stacks.length];
        ci.cancel();
    }
}
