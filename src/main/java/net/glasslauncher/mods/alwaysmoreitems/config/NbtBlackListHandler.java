package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.reflect.*;
import java.util.stream.*;

public class NbtBlackListHandler extends BaseBlackListHandler<ConfigNbtBlacklist> {

    public NbtBlackListHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, ConfigNbtBlacklist value, ConfigNbtBlacklist defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    public boolean isValueValid() {
        blacklist = blacklist.stream().filter(string -> !string.isEmpty()).collect(Collectors.toCollection(ConfigNbtBlacklist::new));
        return true;
    }
}
