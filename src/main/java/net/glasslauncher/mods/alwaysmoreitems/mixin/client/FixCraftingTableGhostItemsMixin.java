package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Should this be part of a bugfix mod? Probably, but this would cause so many issues in SP that it just makes more sense to do it myself.
 */
@Mixin(CraftingScreenHandler.class)
public abstract class FixCraftingTableGhostItemsMixin extends ScreenHandler {

    @Shadow public CraftingInventory input;

    @Inject(method = "onClosed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, require = 0)
    public void removedFix(PlayerEntity player, CallbackInfo ci, int index, ItemStack itemStack) {
        input.setStack(index, null);
    }
}
