package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.gui.screen.AMIStatusScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen implements AMIStatusScreen {
    @Unique
    private String message;

    @Override
    public void setStatus(String status) {
        message = status;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V"))
    private void renderStatus(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        drawCenteredTextWithShadow(Minecraft.INSTANCE.textRenderer, message, width / 2, height / 2 + 20, 16777215);
    }
}
