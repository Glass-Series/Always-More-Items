package net.glasslauncher.mods.alwaysmoreitems.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigNbtBlacklist extends ArrayList<String> {
    public ConfigNbtBlacklist(int initialCapacity) {
        super(initialCapacity);
    }

    public ConfigNbtBlacklist() {
        super();
    }

    public ConfigNbtBlacklist(@NotNull Collection<? extends String> c) {
        super(c);
    }
}
