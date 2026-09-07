package net.glasslauncher.mods.alwaysmoreitems.api;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.MethodFinder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;

public class SubItemHelper {
    public static @Nullable List<ItemStack> getSubItems(Item item) {
        try {
            Method method = MethodFinder.findMethodWithAnnotation(item.getClass(), SubItemProvider.class);
            if (method != null) {
                AlwaysMoreItems.LOGGER.info("Found SubItemProvider method for {} ({})", item.getClass(), ItemRegistry.INSTANCE.getId(item));
                //noinspection unchecked Wrong type? Believe it or not, also perish.
                return (List<ItemStack>) method.invoke(item);
            }
        } catch (Exception | LinkageError e) {
            AlwaysMoreItems.LOGGER.error("Failed to get SubItemProvider for {} ({})", item.getClass(), ItemRegistry.INSTANCE.getId(item), e);
        }
        
        return null;
    }
}
