package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class class_555Mixin {

    @Redirect(method = "onFrameUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V"))
    private void iAmNoLongerSuggesting(Screen instance, int mouseX, int mouseY, float delta) {
        if (OverlayScreen.INSTANCE.recipesGui.isActive() && instance instanceof HandledScreen) {
            OverlayScreen.INSTANCE.render(mouseX, mouseY, delta);
        }
        else {
            instance.render(mouseX, mouseY, delta);
        }

        if (Tooltip.INSTANCE.isEmpty()) {
            return;
        }

        TooltipRenderEvent.TooltipRenderEventBuilder<?, ?> builder = TooltipRenderEvent.builder()
                .mouseX(mouseX)
                .mouseY(mouseY)
                .delta(delta)
                .textManager(Minecraft.INSTANCE.textRenderer)
                .inventory(Minecraft.INSTANCE.player.inventory)
                ;

        ItemStack itemStack = Tooltip.INSTANCE.getItemStack();
        if (itemStack != null) {
                builder
                        .originalTooltip(TranslationStorage.getInstance().get(itemStack.getTranslationKey() + ".name"))
                        .itemStack(itemStack)
                ;
        }
        else {
            builder.originalTooltip("");
        }

        builder.container(Tooltip.INSTANCE.getContainerScreen());

        TooltipRenderEvent event = builder.build();

        StationAPI.EVENT_BUS.post(event);
        Tooltip.INSTANCE.clear();
    }
}
