package net.glasslauncher.mods.alwaysmoreitems.api.event;

import net.glasslauncher.mods.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class ActionButtonRegisterEvent extends Event {
    public boolean add(Identifier identifier, ActionButton button) {
        return ActionButtonRegistry.add(identifier, button);
    }
}
