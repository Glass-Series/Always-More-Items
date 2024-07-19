package net.glasslauncher.alwaysmoreitems.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.alwaysmoreitems.AMITextRenderer;
import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {

    @Shadow public int backgroundWidth;

    @Shadow public int backgroundHeight;

    @SuppressWarnings("SameReturnValue") // Fuck off, this is a mixin that replaces vanilla behaviour.
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;hasStack()Z"))
    private boolean yourTooltipsAreNowMine(Slot instance, @Local(ordinal = 0) Slot slot, @Local(ordinal = 0, argsOnly = true) int mouseX, @Local(ordinal = 1, argsOnly = true) int mouseY) {
        if (!slot.hasStack()) { // Nothing to do here.
            return false;
        }

        int offsetX = (width - backgroundWidth) / 2;
        int offsetY = (height - backgroundHeight) / 2;


        String itemName = (TranslationStorage.getInstance().get(slot.getStack().getTranslationKey() + ".name"));
        String[] tooltip;
        if(slot.getStack().getItem() instanceof CustomTooltipProvider tooltipProvider) {
            tooltip = tooltipProvider.getTooltip(slot.getStack(), itemName);
        }
        else {
            tooltip = new String[]{itemName};
        }
        int tooltipX = mouseX - offsetX;
        int tooltipY = mouseY - offsetY;

        DrawableHelper.drawTooltip(List.of(tooltip), tooltipX, tooltipY);

        return false;
    }
}
