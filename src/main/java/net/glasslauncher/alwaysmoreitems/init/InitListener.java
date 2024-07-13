package net.glasslauncher.alwaysmoreitems.init;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

public class InitListener {
    @EventListener(priority = ListenerPriority.HIGHEST, phase = InitEvent.PRE_INIT_PHASE)
    public void preInit(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitem:event_bus", Object.class).forEach(EntrypointManager::setup);
    }
}
