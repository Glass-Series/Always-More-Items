package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.reflect.*;
import java.util.stream.*;

public class ItemBlackListHandler extends BaseBlackListHandler<ConfigItemBlacklist> {

    public ItemBlackListHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, ConfigItemBlacklist value, ConfigItemBlacklist defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    public boolean isValueValid() {
        blacklist = blacklist.stream().filter(string -> ItemRegistry.INSTANCE.containsId(Identifier.of(string))).collect(Collectors.toCollection(ConfigItemBlacklist::new));
        return true;
    }
}
