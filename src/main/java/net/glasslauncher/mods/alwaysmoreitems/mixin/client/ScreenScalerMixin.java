package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.ScreenScaler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenScaler.class)
public class ScreenScalerMixin {
    @ModifyExpressionValue(method = "<init>", at = @At(value = "CONSTANT", args = "intValue=240"))
    public int aa(int original) {
        if (AlwaysMoreItems.overlayEnabled && AMIConfig.centeredSearchBar() && Minecraft.INSTANCE.currentScreen instanceof HandledScreen) {
            return Math.max(original, 240 + 35);
        }
        
        return original;
    }
}
