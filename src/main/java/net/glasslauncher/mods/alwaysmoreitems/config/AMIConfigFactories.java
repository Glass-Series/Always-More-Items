package net.glasslauncher.mods.alwaysmoreitems.config;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.SeptFunction;
import net.glasslauncher.mods.gcapi3.impl.factory.DefaultFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi3.impl.object.entry.EnumConfigEntryHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Function;

public class AMIConfigFactories implements ConfigFactoryProvider {
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(ConfigItemBlacklist.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) -> new ItemBlackListHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, new ConfigItemBlacklist((Collection<? extends String>) enumOrOrdinal), (ConfigItemBlacklist) defaultEnum)));
        immutableBuilder.put(ConfigNbtBlacklist.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) -> new NbtBlackListHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, new ConfigNbtBlacklist((Collection<? extends String>) enumOrOrdinal), (ConfigNbtBlacklist) defaultEnum)));
        immutableBuilder.put(OverlayMode.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) -> new EnumConfigEntryHandler<OverlayMode>(id, configEntry, parentField, parentObject, isMultiplayerSynced, DefaultFactoryProvider.enumOrOrdinalToOrdinal(enumOrOrdinal), ((OverlayMode) defaultEnum).ordinal(), OverlayMode.class)));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(ConfigItemBlacklist.class, object -> object);
        immutableBuilder.put(ConfigNbtBlacklist.class, object -> object);
        immutableBuilder.put(OverlayMode.class, enumEntry -> enumEntry);
    }
}
