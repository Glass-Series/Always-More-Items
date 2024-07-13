package net.glasslauncher.alwaysmoreitems.init;

import net.glasslauncher.alwaysmoreitems.network.ActionButtonC2SPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event){
        ActionButtonC2SPacket.register();
    }
}
