package net.glasslauncher.mods.alwaysmoreitems.init;

import net.glasslauncher.mods.alwaysmoreitems.api.event.ActionButtonRegisterEvent;
import net.glasslauncher.mods.alwaysmoreitems.action.HealActionButton;
import net.glasslauncher.mods.alwaysmoreitems.action.SetTimeActionButton;
import net.glasslauncher.mods.alwaysmoreitems.action.ToggleWeatherActionButton;
import net.glasslauncher.mods.alwaysmoreitems.action.TrashActionButton;
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
        event.add(NAMESPACE.id("set_time_day"), new SetTimeActionButton(6000, "/assets/alwaysmoreitems/stationapi/textures/gui/day.png"));
        event.add(NAMESPACE.id("set_time_night"), new SetTimeActionButton(18000, "/assets/alwaysmoreitems/stationapi/textures/gui/night.png"));
        event.add(NAMESPACE.id("toggle_weather"), new ToggleWeatherActionButton());
        event.add(NAMESPACE.id("heal"), new HealActionButton());
        event.add(NAMESPACE.id("trash"), new TrashActionButton());
    }
}
