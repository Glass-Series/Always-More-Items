package net.glasslauncher.mods.alwaysmoreitems.config;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.glasslauncher.mods.gcapi3.api.GCAPI;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.minecraft.item.ItemStack;

import java.io.*;

public class AMIConfig {

    @ConfigRoot(
            value = "config",
            visibleName = "Main Config"
    )
    public static final AMIConfigObject INSTANCE = new AMIConfigObject();

    public static boolean isRecipeAnimationsEnabled() {
        return true;
    }

    public static void addItemToConfigBlacklist(ItemStack itemStack, boolean wildcard) {
        if (itemStack == null) {
            return;
        }
        String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack, wildcard);
        if (INSTANCE.itemBlacklist.add(uid)) {
            updateBlacklist();
        }
    }

    public static void removeItemFromConfigBlacklist(ItemStack itemStack, boolean wildcard) {
        if (itemStack == null) {
            return;
        }
        String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack, wildcard);
        if (INSTANCE.itemBlacklist.remove(uid)) {
            updateBlacklist();
        }
    }

    public static boolean isItemOnConfigBlacklist(ItemStack itemStack, boolean wildcard) {
        String uid = AlwaysMoreItems.getStackHelper().getUniqueIdentifierForStack(itemStack, wildcard);
        return INSTANCE.itemBlacklist.contains(uid);
    }

    public static boolean isDebugModeEnabled() {
        return INSTANCE.debugMode;
    }

    public static boolean isEditModeEnabled() {
        return INSTANCE.editMode;
    }

    private static void updateBlacklist() {
        GCAPI.reloadConfig(AlwaysMoreItems.NAMESPACE.id("config").toString(), new GlassYamlFile(new File(FabricLoader.getInstance().getConfigDir().toFile(), AlwaysMoreItems.NAMESPACE + "/config.yml")));
    }

    public static boolean showModNames() {
        return INSTANCE.showModNames;
    }

    public static int getRightClickGiveAmount() {
        return INSTANCE.rightClickGiveAmount;
    }

    public static boolean showNbtCount() {
        return INSTANCE.showNbtCount;
    }

    public static boolean ignoreBadNames() {
        return INSTANCE.ignoreBadNames;
    }
}
