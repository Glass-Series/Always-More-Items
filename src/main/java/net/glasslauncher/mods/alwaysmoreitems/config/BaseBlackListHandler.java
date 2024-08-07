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

public abstract class BaseBlackListHandler<T extends ArrayList<String>> extends ConfigEntryHandler<T> implements ConfigEntryWithButton {
    protected T blacklist;

    public BaseBlackListHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, T value, T defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
        blacklist = value;
    }

    @Override
    public T getDrawableValue() {
        return blacklist;
    }

    @Override
    public void setDrawableValue(T value) {
        blacklist = value;
    }

    @Override
    public @NotNull List<HasDrawable> getDrawables() {
        return drawableList;
    }

    @Override
    public void reset(Object defaultValue) throws IllegalAccessException {
        value = (T) ((T) defaultValue).clone();
        saveToField();
    }

    @Override
    public void onClick() {

    }
}

