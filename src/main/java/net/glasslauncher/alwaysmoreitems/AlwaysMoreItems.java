package net.glasslauncher.alwaysmoreitems;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.alwaysmoreitems.util.StackHelper;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class AlwaysMoreItems {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    @Getter @Setter
    private static AMIHelpers helpers;
    @Getter @Setter
    private static ItemFilter itemFilter;
    @Getter @Setter
    private static AMIItemRegistry itemRegistry;
    @Getter @Setter
    private static RecipeRegistry recipeRegistry;
    @Getter @Setter(value = AccessLevel.PACKAGE)
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
