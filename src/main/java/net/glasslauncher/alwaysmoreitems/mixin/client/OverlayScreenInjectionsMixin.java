package net.glasslauncher.alwaysmoreitems.mixin.client;

import net.glasslauncher.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class OverlayScreenInjectionsMixin extends Screen {

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        OverlayScreen.INSTANCE.init(this, width, height);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V", shift = At.Shift.AFTER))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        OverlayScreen.INSTANCE.render(mouseX, mouseY, delta);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci) {
        OverlayScreen.INSTANCE.tick();
    }

    @Inject(method = "keyPressed", at = @At(value = "HEAD"), cancellable = true)
    public void keyPressed(char character, int keyCode, CallbackInfo ci) {
        if (OverlayScreen.INSTANCE.overlayKeyPressed(character, keyCode)) {
            ci.cancel();
        }
    }

    @Override
    public void onMouseEvent() {
        OverlayScreen.INSTANCE.onMouseEvent();
        super.onMouseEvent();
    }
}
