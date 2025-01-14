package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.api.Rarity;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @Inject(method = "draw(Ljava/lang/String;IIIZ)V", at = @At(value = "HEAD"), cancellable = true)
    public void hahaIHopeOtherModsDontTryToDoThis(String string, int x, int y, int color, boolean shadow, CallbackInfo ci) {
        if (string == null) {
            ci.cancel();
            return;
        }
        if (!string.isEmpty() && string.charAt(0) == Rarity.HeaderCode.PREFIX_CHARACTER) {
            string = string.substring(2); // Rarities didn't process on this string for some reason, skip it.
        }
        if (shadow) {
            int shadowOffset = color & -16777216;
            color = (color & 16579836) >> 2;
            color += shadowOffset;
        }

        float red = (float)(color >> 16 & 255) / 255;
        float green = (float)(color >> 8 & 255) / 255;
        float blue = (float)(color & 255) / 255;
        float alpha = (float)(color >> 24 & 255) / 255;
        if (alpha == 0) {
            alpha = 1;
        }

        AMITextRenderer.INSTANCE.renderStringAtPos(string, x, y, new Color(red, green, blue, alpha), shadow);
        ci.cancel();
    }
}
