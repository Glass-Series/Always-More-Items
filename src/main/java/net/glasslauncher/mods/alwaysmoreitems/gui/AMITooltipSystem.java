package net.glasslauncher.mods.alwaysmoreitems.gui;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import org.lwjgl.opengl.GL11;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class AMITooltipSystem {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    public static final String AMI_TOOLTIP_PHASE = "always_more_items:tooltip_phase";

    @EventListener(phase = AMI_TOOLTIP_PHASE)
    private static void yourTooltipsAreNowMine(TooltipRenderEvent event) {
        if (event.isCanceled()) {
            return;
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.disableLighting();
        Tooltip.INSTANCE.render();

        event.cancel();
    }
}
