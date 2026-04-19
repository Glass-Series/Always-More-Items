package net.glasslauncher.mods.alwaysmoreitems.api;

import lombok.SneakyThrows;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.MethodFinder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SubItemHelper {
    @SneakyThrows // Bad access? Perish.
    public static @Nullable List<ItemStack> getSubItems(Item item) {
        Method method = MethodFinder.findMethodWithAnnotation(item.getClass(), SubItemProvider.class);
        if (method != null) {
            AlwaysMoreItems.LOGGER.info("Found SubItemProvider method for {}", item.getClass());
            //noinspection unchecked Wrong type? Believe it or not, also perish.
            return (List<ItemStack>) method.invoke(item);
        }
        return null;
    }
}
