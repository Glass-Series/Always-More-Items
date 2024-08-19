package net.glasslauncher.mods.alwaysmoreitems.action;

import com.mojang.serialization.Lifecycle;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

public class ActionButtonRegistry extends SimpleRegistry<ActionButton> {
    public static final RegistryKey<Registry<ActionButton>> KEY = RegistryKey.ofRegistry(AlwaysMoreItems.NAMESPACE.id("action_buttons"));
    public static final ActionButtonRegistry INSTANCE = new ActionButtonRegistry(KEY, Lifecycle.stable());

    public ActionButtonRegistry(RegistryKey<? extends Registry<ActionButton>> key, Lifecycle lifecycle) {
        super(key, lifecycle);
    }
}
