package net.glasslauncher.mods.alwaysmoreitems.init;

import net.glasslauncher.mods.alwaysmoreitems.AMITooltipSystem;
import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.ItemFilter;
import net.mine_diver.unsafeevents.event.PhaseOrdering;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.event.registry.RegistriesFrozenEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientInit {

	@EventListener(priority = ListenerPriority.LOW)
	public static void initAMI(RegistriesFrozenEvent event) {
        //noinspection UnstableApiUsage
        PhaseOrdering.of(TooltipRenderEvent.class).addPhaseOrdering(AMITooltipSystem.AMI_TOOLTIP_PHASE, StationAPI.INTERNAL_PHASE);
		AlwaysMoreItems.setItemFilter(new ItemFilter(AlwaysMoreItems.getItemRegistry()));
	}
}
