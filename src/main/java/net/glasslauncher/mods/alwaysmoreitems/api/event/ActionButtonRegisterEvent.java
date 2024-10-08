package net.glasslauncher.mods.alwaysmoreitems.api.event;

import net.glasslauncher.mods.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;

public class ActionButtonRegisterEvent extends Event {

    public boolean add(Identifier identifier, ActionButton button) {
        try {
            Registry.register(ActionButtonRegistry.INSTANCE, identifier, button);
            AlwaysMoreItems.LOGGER.info("Button {} registered.", identifier);
            return true;
        }
        catch (Exception e) {
            AlwaysMoreItems.LOGGER.error("Failed to register action button", e);
            return false;
        }
    }
}
