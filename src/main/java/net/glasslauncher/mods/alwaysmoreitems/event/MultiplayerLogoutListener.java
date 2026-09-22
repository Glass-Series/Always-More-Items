package net.glasslauncher.mods.alwaysmoreitems.event;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.event.world.WorldEvent;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;

public class MultiplayerLogoutListener {

    @EventListener
    public static void onLogout(MultiplayerLogoutEvent event) {
        AlwaysMoreItems.setAMIOnServer(true);
    }

    @EventListener
    public void loadWorld(WorldEvent.Init event) {
        if (Minecraft.INSTANCE.currentScreen instanceof ConnectScreen connectScreen) {
            ModdedPacketHandler handler = (ModdedPacketHandler) connectScreen.networkHandler;
            AlwaysMoreItems.setAMIOnServer(handler.isModded() && handler.getMods().containsKey(AlwaysMoreItems.NAMESPACE.toString()));
        }
        else {
            AlwaysMoreItems.setAMIOnServer(true);
        }
    }
}
