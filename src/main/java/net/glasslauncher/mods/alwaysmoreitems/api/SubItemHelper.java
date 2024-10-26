package net.glasslauncher.mods.alwaysmoreitems.api;

import lombok.SneakyThrows;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
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
        Optional<Method> method = Arrays.stream(item.getClass().getDeclaredMethods()).filter(m -> m.getAnnotation(SubItemProvider.class) != null).findFirst();
        if (method.isPresent()) {
            AlwaysMoreItems.LOGGER.info("Found method for " + item.getClass());
            //noinspection unchecked Wrong type? Believe it or not, also perish.
            return (List<ItemStack>) method.get().invoke(item);
        }
        return null;
    }
}
