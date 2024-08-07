package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigEntryWithButton;
import net.glasslauncher.mods.gcapi3.api.HasDrawable;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class ConfigItemBlackListHandler extends ConfigEntryHandler<ConfigItemBlacklist> implements ConfigEntryWithButton {
    private ConfigItemBlacklist blacklist;

    public ConfigItemBlackListHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, ConfigItemBlacklist value, ConfigItemBlacklist defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
        blacklist = value;
    }

    @Override
    public ConfigItemBlacklist getDrawableValue() {
        return blacklist;
    }

    @Override
    public boolean isValueValid() {
        blacklist = blacklist.stream().filter(string -> ItemRegistry.INSTANCE.containsId(Identifier.of(string))).collect(Collectors.toCollection(ConfigItemBlacklist::new));
        return true;
    }

    @Override
    public void setDrawableValue(ConfigItemBlacklist value) {
        blacklist = value;
    }

    @Override
    public @NotNull List<HasDrawable> getDrawables() {
        return drawableList;
    }

    @Override
    public void reset(Object defaultValue) throws IllegalAccessException {
        value = (ConfigItemBlacklist) ((ConfigItemBlacklist) defaultValue).clone();
        saveToField();
    }

    @Override
    public void onClick() {

    }
}

