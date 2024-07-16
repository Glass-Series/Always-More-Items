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
public class ScreenMixinHandler extends Screen {

    @Unique
    OverlayScreen overlay;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void constructor(CallbackInfo ci) {
        overlay = new OverlayScreen((HandledScreen) (Object) this);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        overlay.init(minecraft, width, height);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V", shift = At.Shift.AFTER))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        overlay.render(mouseX, mouseY, delta);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci){
        overlay.tick();
    }

    @Inject(method = "mouseClicked", at = @At(value = "HEAD"))
    public void mouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci){
        overlay.mouseClicked(mouseX, mouseY, button);
    }

    @Inject(method = "keyPressed", at = @At(value = "TAIL"))
    public void keyPressed(char character, int keyCode, CallbackInfo ci){
        overlay.keyPressed(character, keyCode);
    }

    @Override
    public void onMouseEvent(){
        super.onMouseEvent();
        overlay.onMouseEvent();
    }
}
