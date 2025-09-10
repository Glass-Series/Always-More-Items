package net.glasslauncher.mods.alwaysmoreitems.util;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.init.CommonInit;
import net.glasslauncher.mods.alwaysmoreitems.recipe.ItemBlacklist;
import net.glasslauncher.mods.alwaysmoreitems.recipe.ItemFilter;
import net.glasslauncher.mods.alwaysmoreitems.registry.AMIItemRegistry;
import net.glasslauncher.mods.alwaysmoreitems.registry.RecipeRegistry;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

public class AlwaysMoreItems {
    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();
    public static final Logger LOGGER = NAMESPACE.getLogger();

    @Getter @Setter
    private static AMIHelpers helpers;
    @Getter @Setter
    private static ItemFilter itemFilter;
    @Getter @Setter
    private static AMIItemRegistry itemRegistry;
    @Getter @Setter
    private static RecipeRegistry recipeRegistry;
    @Getter @Setter
    private static boolean started;

    public static boolean overlayEnabled = true;

    public static StackHelper getStackHelper() {
        return helpers.getStackHelper();
    }

    public static void resetItemFilter() {
        if (itemFilter != null) {
            itemFilter.reset();
        }
    }

    public static void reloadBlacklist() {
        ItemBlacklist.reset();
        CommonInit.getPlugins().values().forEach(iModPlugin -> {
            try {
                iModPlugin.updateBlacklist(helpers);
            } catch (IncompatibleClassChangeError ignored) {}
        });
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && AlwaysMoreItems.getItemFilter() != null) {
            AlwaysMoreItems.getItemFilter().reset();
            OverlayScreen.INSTANCE.rebuildRenderList();
        }
    }

    public static boolean isAMIOnServer() {
        return true;
    }
}
