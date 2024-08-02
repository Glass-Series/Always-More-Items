package net.glasslauncher.mods.alwaysmoreitems.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.resource.DataReloadEvent;

public class ServerInit {

    @EventListener(priority = ListenerPriority.CUSTOM, numPriority = Integer.MIN_VALUE + 1)
    public static void initAMI(DataReloadEvent event) {
        CommonInit.initAMI();
    }
}
