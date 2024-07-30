package net.glasslauncher.mods.alwaysmoreitems.init;

import net.glasslauncher.mods.alwaysmoreitems.AMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class ServerInit {

    @EventListener(phase = InitEvent.PRE_INIT_PHASE)
    public static void preInit(InitEvent event) {
//		Config.preInit(event);
        AlwaysMoreItems.LOGGER.info("Hello");
        AlwaysMoreItems.setHelpers(new AMIHelpers());
    }
}
