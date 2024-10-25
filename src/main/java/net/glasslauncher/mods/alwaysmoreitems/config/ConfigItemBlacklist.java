package net.glasslauncher.mods.alwaysmoreitems.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigItemBlacklist extends ArrayList<String> {
    public ConfigItemBlacklist(int initialCapacity) {
        super(initialCapacity);
    }

    public ConfigItemBlacklist() {
        super();
    }

    public ConfigItemBlacklist(@NotNull Collection<? extends String> c) {
        super(c);
    }
}
