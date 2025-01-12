package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

import java.lang.reflect.Field;

public class ItemBlackListHandler extends BaseBlackListHandler<ConfigItemBlacklist> {

    public ItemBlackListHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, ConfigItemBlacklist value, ConfigItemBlacklist defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    public boolean isValueValid() {
        return true;
    }
}
