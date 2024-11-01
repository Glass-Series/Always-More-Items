package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class ItemBlackListHandler extends BaseBlackListHandler<ConfigItemBlacklist> {

    public ItemBlackListHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, ConfigItemBlacklist value, ConfigItemBlacklist defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    public boolean isValueValid() {
        return true;
    }
}
