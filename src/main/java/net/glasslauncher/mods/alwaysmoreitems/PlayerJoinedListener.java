package net.glasslauncher.mods.alwaysmoreitems;

import net.glasslauncher.mods.alwaysmoreitems.network.s2c.RecipeSyncPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;

public class PlayerJoinedListener {

    @EventListener
    public void playerJoined(PlayerLoginEvent event) {
        PacketHelper.sendTo(event.player, new RecipeSyncPacket());
    }
}
