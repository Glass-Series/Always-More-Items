package net.glasslauncher.mods.alwaysmoreitems.action;

import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

public class ActionButtonRegistry {
    public static LinkedHashMap<Identifier, ActionButton> registry = new LinkedHashMap<>();

    public static boolean add(Identifier identifier, ActionButton button) {
        if(identifier != null && button != null && !registry.containsKey(identifier)) {
            registry.put(identifier, button);
            return true;
        }
        return false;
    }

    public static ActionButton get(Identifier id) {
        return registry.get(id);
    }
}
