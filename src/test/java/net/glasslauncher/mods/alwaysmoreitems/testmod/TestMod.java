package net.glasslauncher.mods.alwaysmoreitems.testmod;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class TestMod {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    Item amiTortureTester;

    @EventListener
    public void init(ItemRegistryEvent event) {
        amiTortureTester = new AMIItem(NAMESPACE.id("amiitem"));
    }
}
