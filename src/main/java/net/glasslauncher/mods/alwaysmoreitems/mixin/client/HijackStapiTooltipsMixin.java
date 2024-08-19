package net.glasslauncher.mods.alwaysmoreitems.mixin.client;

import net.glasslauncher.mods.alwaysmoreitems.gui.HijackedStapiTip;
import net.glasslauncher.mods.alwaysmoreitems.gui.TooltipInstance;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TooltipRenderEvent.class)
public class HijackStapiTooltipsMixin implements HijackedStapiTip {
    @Unique
    private TooltipInstance instance;

    @Override
    public void alwaysMoreItems$setTooltip(TooltipInstance instance) {
        this.instance = instance;
    }

    @Override
    public TooltipInstance alwaysMoreItems$getTooltip() {
        return instance;
    }
}
