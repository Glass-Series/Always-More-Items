package net.glasslauncher.mods.alwaysmoreitems.init;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.glasslauncher.mods.alwaysmoreitems.api.ModPluginProvider;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.ActionButtonPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.GiveItemPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.RecipeTransferPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.s2c.RecipeSyncPacket;
import net.glasslauncher.mods.alwaysmoreitems.registry.AMIItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.registry.RecipeRegistry;
import net.glasslauncher.mods.alwaysmoreitems.util.AMIHelpers;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.ModRegistry;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.init.InitFinishedEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class CommonInit {
    @Getter
    private static ImmutableMap<Identifier, ModPluginProvider> plugins;
    @Getter
    private static ModRegistry modRegistry;

    @EventListener
    public static void init(InitFinishedEvent event) {
        AlwaysMoreItems.setHelpers(new AMIHelpers());
        initPlugins();
        FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:action", Object.class).forEach(EntrypointManager::setup);
        initAMI();
    }

    public static void initPlugins() {
        LinkedHashMap<Identifier, ModPluginProvider> pluginsMap = new LinkedHashMap<>();
        FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:plugin", ModPluginProvider.class).stream().map(EntrypointContainer::getEntrypoint).forEach(iModPlugin -> pluginsMap.put(iModPlugin.getId(), iModPlugin));

        LinkedHashMap<Identifier, ModPluginProvider> oldPlugins = new LinkedHashMap<>(pluginsMap);
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
    }

    @EventListener
    public static void registerPackets(PacketRegisterEvent event){
        Registry.register(PacketTypeRegistry.INSTANCE, AlwaysMoreItems.NAMESPACE.id("action_button"), ActionButtonPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, AlwaysMoreItems.NAMESPACE.id("give_item"), GiveItemPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, AlwaysMoreItems.NAMESPACE.id("transfer"), RecipeTransferPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, AlwaysMoreItems.NAMESPACE.id("sync"), RecipeSyncPacket.TYPE);
    }

    public static void initAMI() {
        AlwaysMoreItems.setStarted(true);
        AMIItemRegistry itemRegistry = new AMIItemRegistry();
        AlwaysMoreItems.setItemRegistry(itemRegistry);
        HashMap<Identifier, ModPluginProvider> plugins = (HashMap<Identifier, ModPluginProvider>) CommonInit.getPlugins().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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
        CommonInit.plugins = ImmutableMap.copyOf(plugins);
    }
}
