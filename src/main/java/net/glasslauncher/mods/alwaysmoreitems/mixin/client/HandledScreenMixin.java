package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.alwaysmoreitems.AMITooltipSystem;
import net.glasslauncher.mods.alwaysmoreitems.Focus;
import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.init.KeybindListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.benjiweber.expressions.tuple.TriTuple;

import java.util.*;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    @Shadow public int backgroundWidth;

    @Shadow public int backgroundHeight;

    @Shadow protected abstract Slot getSlotAt(int x, int y);

    @Unique private int mouseX = 0;
    @Unique private int mouseY = 0;

    @SuppressWarnings("SameReturnValue") // Fuck off, this is a mixin that replaces vanilla behaviour.
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;hasStack()Z"))
    private boolean yourTooltipsAreNowMine(Slot instance, @Local(ordinal = 0) Slot slot, @Local(ordinal = 0, argsOnly = true) int mouseX, @Local(ordinal = 1, argsOnly = true) int mouseY) {
        if (!slot.hasStack()) { // Nothing to do here.
            return false;
        }

        int offsetX = (width - backgroundWidth) / 2;
        int offsetY = (height - backgroundHeight) / 2;


        String itemName = (TranslationStorage.getInstance().get(slot.getStack().getTranslationKey() + ".name"));
        List<String> tooltip;
        if(slot.getStack().getItem() instanceof CustomTooltipProvider tooltipProvider) {
            tooltip = new ArrayList<>(List.of(tooltipProvider.getTooltip(slot.getStack(), itemName)));
        }
        else {
            tooltip = new ArrayList<>(){{add(itemName);}};
        }
        if (slot.getStack().getItem() instanceof RarityProvider rarity) {
            tooltip.set(0, rarity.getRarity(slot.getStack()) + tooltip.get(0));
        }
        int tooltipX = mouseX - offsetX;
        int tooltipY = mouseY - offsetY;

        TriTuple<Integer, Integer, Boolean> result = AMITooltipSystem.getTooltipOffsets(mouseX, mouseY, tooltip, width, height);

        AMITooltipSystem.drawTooltip(tooltip, result.one() + tooltipX, result.two() + tooltipY, result.three());

        return false;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void mouseCatcher(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void nowSoAreYourInputs(char chr, int keyCode, CallbackInfo ci) {
        Slot hoveredItem = getSlotAt(mouseX, mouseY);
        // Item Actions
        if (hoveredItem != null && hoveredItem.hasStack()) {
            // Show Recipes
            if (keyCode == KeybindListener.showRecipe.code) {
                OverlayScreen.INSTANCE.recipesGui.showRecipes(new Focus(hoveredItem.getStack()));
                ci.cancel();
            }

            // Show Uses
            else if (keyCode == KeybindListener.showUses.code) {
                OverlayScreen.INSTANCE.recipesGui.showUses(new Focus(hoveredItem.getStack()));
                ci.cancel();
            }
        }
    }
}
