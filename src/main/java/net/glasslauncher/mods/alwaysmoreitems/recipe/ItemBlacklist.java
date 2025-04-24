package net.glasslauncher.mods.alwaysmoreitems.recipe;

import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemBlacklist implements net.glasslauncher.mods.alwaysmoreitems.api.ItemBlacklist {
    @Nonnull
    private final Set<String> itemBlacklist = new HashSet<>();

    @Override
    public void addItemToBlacklist(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
            return;
        }
        String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack);
        itemBlacklist.add(uid);

        AlwaysMoreItems.resetItemFilter();
    }

    @Override
    public void removeItemFromBlacklist(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
            return;
        }
        String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack);
        itemBlacklist.remove(uid);

        AlwaysMoreItems.resetItemFilter();
    }

    @Override
    public boolean isItemBlacklisted(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
            return false;
        }
        List<String> uids = AlwaysMoreItems.getStackHelper().getUniqueIdentifiersWithWildcard(itemStack);
        return uids.stream().anyMatch(uid -> itemBlacklist.contains(uid) || AMIConfig.INSTANCE.itemBlacklist.contains(uid));
    }

    @Override
    public boolean isItemAPIBlacklisted(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            AlwaysMoreItems.LOGGER.error("Null itemStack", new NullPointerException());
            return false;
        }
        List<String> uids = AlwaysMoreItems.getStackHelper().getUniqueIdentifiersWithWildcard(itemStack);
        return uids.stream().anyMatch(itemBlacklist::contains);
    }

    public static void reset() {
        AlwaysMoreItems.getHelpers().getItemBlacklist().itemBlacklist.clear();
    }
}
