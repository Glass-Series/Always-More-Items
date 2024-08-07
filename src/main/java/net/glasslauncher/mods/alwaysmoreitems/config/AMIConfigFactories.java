package net.glasslauncher.mods.alwaysmoreitems.config;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import uk.co.benjiweber.expressions.function.SeptFunction;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class AMIConfigFactories implements ConfigFactoryProvider {
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(ConfigItemBlacklist.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) -> new ConfigItemBlackListHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, new ConfigItemBlacklist((Collection<? extends String>) enumOrOrdinal), (ConfigItemBlacklist) defaultEnum)));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(ConfigItemBlacklist.class, object -> object);
    }
}
