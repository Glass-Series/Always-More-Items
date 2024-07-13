package net.glasslauncher.alwaysmoreitems.init;

import net.glasslauncher.alwaysmoreitems.api.event.ActionButtonRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;

public class AfterBlockAndItemListener {
    @EventListener
    public void listen(AfterBlockAndItemRegisterEvent event){
        StationAPI.EVENT_BUS.post(new ActionButtonRegisterEvent());
    }
}
