package net.glasslauncher.alwaysmoreitems.mixin.client;

import net.glasslauncher.alwaysmoreitems.AMITextField;
import net.glasslauncher.mods.gcapi.impl.screen.widget.ExtensibleTextFieldWidget;
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
    AMITextField textFieldWidget;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void test(CallbackInfo ci) {
//        textFieldWidget = new AMITextField((HandledScreen) (Object) this, textRenderer);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground()V", shift = At.Shift.AFTER))
    public void render(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        textFieldWidget.draw(mouseX, mouseY);
    }
}
