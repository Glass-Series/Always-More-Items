package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;tickInput()V"))
    private void danyLMoment(Screen instance, Operation<Void> original) {
        if (OverlayScreen.INSTANCE.recipesGui.isActive() && instance instanceof HandledScreen) {
            OverlayScreen.INSTANCE.tickInput();
        }
        else {
            original.call(instance);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;onKeyboardEvent()V"))
    private void danyLMoment2(Screen instance, Operation<Void> original) {
        if (OverlayScreen.INSTANCE.recipesGui.isActive() && instance instanceof HandledScreen) {
            OverlayScreen.INSTANCE.onKeyboardEvent();
        }
        else {
            original.call(instance);
        }
    }
}
