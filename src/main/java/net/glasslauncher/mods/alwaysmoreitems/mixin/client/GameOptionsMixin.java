package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "setInt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;guiScale:I", ordinal = 0, shift = At.Shift.AFTER))
    private void updateOurOwnScale(Option value, int par2, CallbackInfo ci) {
        OverlayScreen.INSTANCE.rescale();
    }
}
