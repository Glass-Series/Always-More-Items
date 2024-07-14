package net.glasslauncher.alwaysmoreitems.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.alwaysmoreitems.action.DummyActionButton;
import net.glasslauncher.alwaysmoreitems.action.HealActionButton;
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
        event.add(NAMESPACE.id("heal"), new HealActionButton());
        for (int i = 0; i < 20; i++) {
            if (i > 10 && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                continue;
            }
            event.add(NAMESPACE.id("dummy" + i), new DummyActionButton());
        }
    }
}
