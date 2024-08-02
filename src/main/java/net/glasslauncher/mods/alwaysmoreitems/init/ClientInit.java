package net.glasslauncher.mods.alwaysmoreitems.init;

import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.ItemFilter;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientInit {

	@EventListener(priority = ListenerPriority.CUSTOM, numPriority = Integer.MIN_VALUE + 1)
	public static void initAMI(DimensionRegistryEvent event) {
		CommonInit.initAMI();
		AlwaysMoreItems.setItemFilter(new ItemFilter(AlwaysMoreItems.getItemRegistry()));
	}
}
