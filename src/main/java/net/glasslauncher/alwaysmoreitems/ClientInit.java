package net.glasslauncher.alwaysmoreitems;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.glasslauncher.alwaysmoreitems.api.IModPlugin;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.VanillaPlugin;
import net.glasslauncher.alwaysmoreitems.util.ModRegistry;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import org.lwjgl.input.Keyboard;

import javax.annotation.*;
import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientInit {
//	private GuiEventHandler guiEventHandler;
	private static List<IModPlugin> plugins;

	private static void initVersionChecker() {
		final NbtCompound compound = new NbtCompound();
		compound.putString("curseProjectName", "just-enough-items-jei");
		compound.putString("curseFilenameParser", "always_more_items_b1.7.3-[].jar");
//		FMLInterModComms.sendRuntimeMessage(Constants.MOD_ID, "VersionChecker", "addCurseCheck", compound);
	}

	@EventListener(phase = InitEvent.PRE_INIT_PHASE)
	public static void preInit(InitEvent event) {
//		Config.preInit(event);
		AlwaysMoreItems.LOGGER.info("Hello");
		AlwaysMoreItems.setHelpers(new JeiHelpers());
		initVersionChecker();

		plugins = FabricLoader.getInstance().getEntrypointContainers("alwaysmoreitems:plugin", IModPlugin.class).stream().map(EntrypointContainer::getEntrypoint).toList();

		IModPlugin vanillaPlugin = getVanillaPlugin(plugins);
		if (vanillaPlugin != null) {
			plugins.remove(vanillaPlugin);
			plugins.add(0, vanillaPlugin);
		}

		Iterator<IModPlugin> iterator = plugins.iterator();
		while (iterator.hasNext()) {
			IModPlugin plugin = iterator.next();
			try {
				plugin.onJeiHelpersAvailable(AlwaysMoreItems.getHelpers());
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

	@EventListener
	public static void initKeys(KeyBindingRegisterEvent event) {
		event.keyBindings.add(new KeyBinding("key.alwaysmoreitems.toggleOverlay", Keyboard.KEY_O));
		event.keyBindings.add(new KeyBinding("key.alwaysmoreitems.showRecipe", Keyboard.KEY_R));
		event.keyBindings.add(new KeyBinding("key.alwaysmoreitems.showUses", Keyboard.KEY_U));
		event.keyBindings.add(new KeyBinding("key.alwaysmoreitems.recipeBack", Keyboard.KEY_BACK));
	}

	@EventListener
	public static void init(InitEvent event) {
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
//		ItemListOverlay itemListOverlay = new ItemListOverlay(itemFilter);
//		guiEventHandler.setItemListOverlay(itemListOverlay);
	}

	public static void resetItemFilter() {
		if (AlwaysMoreItems.getItemFilter() != null) {
			AlwaysMoreItems.getItemFilter().reset();
		}
	}
}
