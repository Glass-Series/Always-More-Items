package net.glasslauncher.alwaysmoreitems.action;

import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.LinkedHashMap;

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
