package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.class_555;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_555.class)
public class class_555Mixin {

    @Redirect(method = "method_1844", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V"))
    private void iAmNoLongerSuggesting(Screen instance, int mouseX, int mouseY, float delta) {
        if (OverlayScreen.INSTANCE.recipesGui.isActive()) {
            OverlayScreen.INSTANCE.render(mouseX, mouseY, delta);
            return;
        }
        instance.render(mouseX, mouseY, delta);
    }
}
