package net.glasslauncher.mods.alwaysmoreitems;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi.api.ConfigEntry;
import net.glasslauncher.mods.gcapi.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi.impl.object.ConfigEntryHandler;
import uk.co.benjiweber.expressions.function.SeptFunction;

import java.lang.reflect.*;
import java.util.function.*;

public class AMIConfigFactories implements ConfigFactoryProvider {
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {

    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {

    }
}
