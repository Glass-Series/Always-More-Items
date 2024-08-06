package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.Focus;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.init.KeybindListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    @Shadow public abstract Slot getSlotAt(int x, int y);

    @Unique private int mouseX = 0;
    @Unique private int mouseY = 0;

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
