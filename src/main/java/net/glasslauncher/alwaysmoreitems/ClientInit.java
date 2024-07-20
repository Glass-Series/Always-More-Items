package net.glasslauncher.alwaysmoreitems;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.glasslauncher.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.glasslauncher.alwaysmoreitems.util.ModRegistry;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import javax.annotation.*;
import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientInit {
//	private GuiEventHandler guiEventHandler;
	private static List<IModPlugin> plugins;

	private static void initVersionChecker() {
		final NbtCompound compound = new NbtCompound();
		compound.putString("curseProjectName", "just-enough-items-ami");
		compound.putString("curseFilenameParser", "always_more_items_b1.7.3-[].jar");
//		FMLInterModComms.sendRuntimeMessage(Constants.MOD_ID, "VersionChecker", "addCurseCheck", compound);
	}

	@EventListener(phase = InitEvent.PRE_INIT_PHASE)
	public static void preInit(InitEvent event) {
//		Config.preInit(event);
		AlwaysMoreItems.LOGGER.info("Hello");
		AlwaysMoreItems.setHelpers(new AMIHelpers());
		initVersionChecker();

		plugins = new ArrayList<>(FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:plugin", IModPlugin.class).stream().map(EntrypointContainer::getEntrypoint).toList());

		IModPlugin vanillaPlugin = getVanillaPlugin(plugins);
		if (vanillaPlugin != null) {
            plugins.remove(vanillaPlugin);
			plugins.add(0, vanillaPlugin);
		}

		Iterator<IModPlugin> iterator = plugins.iterator();
		while (iterator.hasNext()) {
			IModPlugin plugin = iterator.next();
			try {
				plugin.onAMIHelpersAvailable(AlwaysMoreItems.getHelpers());
			} catch (AbstractMethodError ignored) {
				// older plugins don't have this method
			} catch (RuntimeException e) {
				AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}", plugin.getClass(), e);
				iterator.remove();
			}
		}
	}

	@Nullable
	private static IModPlugin getVanillaPlugin(List<IModPlugin> modPlugins) {
		for (IModPlugin modPlugin : modPlugins) {
			if (modPlugin instanceof VanillaPlugin) {
				return modPlugin;
			}
		}
		return null;
	}

	// TODO: Move to registries frozen event when StAPI a3 merges into master
	@EventListener
	public static void init(DimensionRegistryEvent event) {
		startAMI();
	}

	public static void startAMI() {
		AlwaysMoreItems.setStarted(true);
		AMIItemRegistry itemRegistry = new AMIItemRegistry();
		AlwaysMoreItems.setItemRegistry(itemRegistry);

		Iterator<IModPlugin> iterator = plugins.iterator();
		while (iterator.hasNext()) {
			IModPlugin plugin = iterator.next();
			try {
				plugin.onItemRegistryAvailable(itemRegistry);
			} catch (AbstractMethodError ignored) {
				// older plugins don't have this method
			} catch (RuntimeException e) {
				AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}", plugin.getClass(), e);
				iterator.remove();
			}
		}

		ModRegistry modRegistry = new ModRegistry();

		iterator = plugins.iterator();
		while (iterator.hasNext()) {
			IModPlugin plugin = iterator.next();
			try {
				plugin.register(modRegistry);
				AlwaysMoreItems.LOGGER.info("Registered plugin: {}", plugin.getClass().getName());
			} catch (RuntimeException e) {
				AlwaysMoreItems.LOGGER.error("Failed to register mod plugin: {}", plugin.getClass(), e);
				iterator.remove();
			}
		}

		RecipeRegistry recipeRegistry = modRegistry.createRecipeRegistry();
		AlwaysMoreItems.setRecipeRegistry(recipeRegistry);

		iterator = plugins.iterator();
		while (iterator.hasNext()) {
			IModPlugin plugin = iterator.next();
            AlwaysMoreItems.LOGGER.info("Initializing plugin {}", plugin.getName());
			try {
				plugin.onRecipeRegistryAvailable(recipeRegistry);
			} catch (AbstractMethodError ignored) {
				// older plugins don't have this method
			} catch (RuntimeException e) {
				AlwaysMoreItems.LOGGER.error("Mod plugin failed: {}", plugin.getClass(), e);
				iterator.remove();
			}
		}

		AlwaysMoreItems.setItemFilter(new ItemFilter(itemRegistry));
	}

	public static void resetItemFilter() {
		if (AlwaysMoreItems.getItemFilter() != null) {
			AlwaysMoreItems.getItemFilter().reset();
		}
	}
}
