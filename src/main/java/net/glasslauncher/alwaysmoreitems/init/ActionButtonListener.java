package net.glasslauncher.alwaysmoreitems.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.alwaysmoreitems.action.DummyActionButton;
import net.glasslauncher.alwaysmoreitems.action.HealActionButton;
import net.glasslauncher.alwaysmoreitems.action.SetTimeActionButton;
import net.glasslauncher.alwaysmoreitems.api.event.ActionButtonRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ActionButtonListener {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @EventListener
    public void registerActionButtons(ActionButtonRegisterEvent event) {
        // 23000 = Dusk, 6000 = Day, 13000 = Dusk, 18000 = Night
        event.add(NAMESPACE.id("set_time_day"), new SetTimeActionButton(6000, "assets/alwaysmoreitems/stationapi/textures/gui/day.png"));
        event.add(NAMESPACE.id("set_time_night"), new SetTimeActionButton(18000, "assets/alwaysmoreitems/stationapi/textures/gui/night.png"));

        event.add(NAMESPACE.id("heal"), new HealActionButton());

        // TODO: Move to test mod
        for (int i = 0; i < 15; i++) {
            // Not registering these on server to test handling of actions missing on server
            if (i > 10 && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                continue;
            }
            event.add(NAMESPACE.id("dummy" + i), new DummyActionButton());
        }
    }
}
