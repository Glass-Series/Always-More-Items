package net.glasslauncher.mods.alwaysmoreitems.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ModList {

    private final Map<String, String> modNamesForIds = new HashMap<>();

    public ModList() {
        for (ModContainer modEntry : FabricLoader.getInstance().getAllMods()) {
            String lowercaseId = modEntry.getMetadata().getId().toLowerCase(Locale.ENGLISH);
            String modName = modEntry.getMetadata().getName();
            modNamesForIds.put(lowercaseId, modName);
        }
    }

    @Nonnull
    public String getModNameForItem(@Nonnull Item item) {
        Identifier itemResourceLocation = ItemRegistry.INSTANCE.getId(item);
        if(itemResourceLocation == null) {
            AlwaysMoreItems.LOGGER.error("Null modId", new NullPointerException());
            return "";
        }
        String modId = itemResourceLocation.namespace.toString();
        String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
        String modName = modNamesForIds.get(lowercaseModId);
        if (modName == null) {
            modName = WordUtils.capitalize(modId);
            modNamesForIds.put(lowercaseModId, modName);
        }
        return modName;
    }
}
