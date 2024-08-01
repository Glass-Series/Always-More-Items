package net.glasslauncher.mods.alwaysmoreitems.init;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.ActionButtonPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.GiveItemPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.RecipeTransferPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

public class CommonListener {
    @EventListener(priority = ListenerPriority.HIGHEST, phase = InitEvent.PRE_INIT_PHASE)
    public void preInit(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:action", Object.class).forEach(EntrypointManager::setup);
    }

    @EventListener
    public void registerPackets(PacketRegisterEvent event){
        ActionButtonPacket.register();
        GiveItemPacket.register();
        RecipeTransferPacket.register();
    }
}
