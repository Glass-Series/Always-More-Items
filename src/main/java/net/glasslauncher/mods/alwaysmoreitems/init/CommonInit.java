package net.glasslauncher.mods.alwaysmoreitems.init;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.glasslauncher.mods.alwaysmoreitems.AMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.AMIItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.ActionButtonPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.GiveItemPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.RecipeTransferPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.s2c.RecipeSyncPacket;
import net.glasslauncher.mods.alwaysmoreitems.util.ModRegistry;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class CommonInit {
    @Getter
    private static ImmutableMap<Identifier, IModPlugin> plugins;
    @Getter
    private static ModRegistry modRegistry;

    @EventListener(priority = ListenerPriority.HIGHEST, phase = InitEvent.PRE_INIT_PHASE)
    public static void preInit(InitEvent event) {
        AlwaysMoreItems.LOGGER.info("Hello");
        AlwaysMoreItems.setHelpers(new AMIHelpers());

        LinkedHashMap<Identifier, IModPlugin> pluginsMap = new LinkedHashMap<>();
        FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:plugin", IModPlugin.class).stream().map(EntrypointContainer::getEntrypoint).forEach(iModPlugin -> pluginsMap.put(iModPlugin.getId(), iModPlugin));

        LinkedHashMap<Identifier, IModPlugin> oldPlugins = new LinkedHashMap<>(pluginsMap);
        pluginsMap.clear();

        pluginsMap.put(AlwaysMoreItems.NAMESPACE.id("vanilla"), oldPlugins.remove(AlwaysMoreItems.NAMESPACE.id("vanilla")));
        pluginsMap.putAll(oldPlugins);

        pluginsMap.values().forEach(iModPlugin -> {
            try {
                iModPlugin.onAMIHelpersAvailable(AlwaysMoreItems.getHelpers());
            } catch (RuntimeException e) {
                AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}/{}", iModPlugin.getId(), iModPlugin.getClass(), e);
                pluginsMap.remove(iModPlugin.getId());
            }
        });
        plugins = ImmutableMap.copyOf(pluginsMap);
        FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:action", Object.class).forEach(EntrypointManager::setup);
    }

    @EventListener
    public static void registerPackets(PacketRegisterEvent event){
        ActionButtonPacket.register();
        GiveItemPacket.register();
        RecipeTransferPacket.register();
        RecipeSyncPacket.register();
    }

    public static void initAMI() {
        AlwaysMoreItems.setStarted(true);
        AMIItemRegistry itemRegistry = new AMIItemRegistry();
        AlwaysMoreItems.setItemRegistry(itemRegistry);
        ImmutableMap<Identifier, IModPlugin> plugins = CommonInit.getPlugins();

        plugins.values().forEach(iModPlugin -> {
            try {
                iModPlugin.onItemRegistryAvailable(itemRegistry);
            } catch (RuntimeException e) {
                AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}/{}", iModPlugin.getId(), iModPlugin.getClass(), e);
                plugins.remove(iModPlugin.getId());
            }
        });

        modRegistry = new ModRegistry();

        plugins.values().forEach(iModPlugin -> {
            try {
                iModPlugin.register(modRegistry);
                AlwaysMoreItems.LOGGER.info("Registered plugin: {}/{}", iModPlugin.getId(), iModPlugin.getClass().getName());
            } catch (RuntimeException e) {
                AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}/{}", iModPlugin.getId(), iModPlugin.getClass(), e);
                plugins.remove(iModPlugin.getId());
            }
        });

        RecipeRegistry recipeRegistry = modRegistry.createRecipeRegistry();
        AlwaysMoreItems.setRecipeRegistry(recipeRegistry);

        plugins.values().forEach(iModPlugin -> {
            AlwaysMoreItems.LOGGER.info("Initializing plugin {}", iModPlugin.getName());
            try {
                iModPlugin.onRecipeRegistryAvailable(recipeRegistry);
            } catch (RuntimeException e) {
                AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}/{}", iModPlugin.getId(), iModPlugin.getClass(), e);
                plugins.remove(iModPlugin.getId());
            }
        });
    }
}
