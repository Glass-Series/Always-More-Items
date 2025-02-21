package net.glasslauncher.mods.alwaysmoreitems.util;

import lombok.Getter;
import lombok.Setter;
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

    public static boolean isAMIOnServer() {
        return true;
    }
}
