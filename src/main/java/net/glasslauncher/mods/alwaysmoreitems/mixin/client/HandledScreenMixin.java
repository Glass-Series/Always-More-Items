package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.init.KeybindListener;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    @Unique private int mouseX = 0;
    @Unique private int mouseY = 0;

    @Inject(method = "render", at = @At("HEAD"))
    private void mouseCatcher(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;hasStack()Z"))
    private boolean yourTooltipsAreMineAgain(Slot slot) {
        if (!slot.hasStack()) { // Nothing to do here.
            return false;
        }

        Tooltip.INSTANCE.setTooltip(slot.getStack(), mouseX, mouseY);
        return false;
    }
}
