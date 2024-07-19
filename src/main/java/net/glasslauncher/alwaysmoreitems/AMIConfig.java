package net.glasslauncher.alwaysmoreitems;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.alwaysmoreitems.config.ConfigItemBlacklist;
import net.glasslauncher.mods.gcapi.api.ConfigEntry;
import net.glasslauncher.mods.gcapi.api.ConfigRoot;
import net.glasslauncher.mods.gcapi.api.GCAPI;
import net.glasslauncher.mods.gcapi.api.GeneratedConfig;
import net.glasslauncher.mods.gcapi.impl.GlassYamlFile;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class AMIConfig {

    @ConfigRoot(
            value = "config",
            visibleName = "Main Config"
    )
    public static final AMIConfigInstance INSTANCE = new AMIConfigInstance();

    public static boolean isRecipeAnimationsEnabled() {
        return true;
    }

    // TODO: Hide item blacklist?
    public static class AMIConfigInstance {

        @ConfigEntry(name = "Edit Mode")
        public Boolean editMode = false;

        @ConfigEntry(name = "Debug Mode")
        public Boolean debugMode = false;

        @ConfigEntry(
                name = "Item Blacklist",
                description = ""// todo: do the desc
        )
        public ConfigItemBlacklist itemBlacklist = new ConfigItemBlacklist();

        @ConfigEntry(
                name = "NBT Blacklist",
                description = ""// todo: do the desc
        )
        public ConfigItemBlacklist nbtBlacklist = new ConfigItemBlacklist();
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
        GCAPI.reloadConfig(AlwaysMoreItems.NAMESPACE.id("config"), new GlassYamlFile(new File(FabricLoader.getInstance().getConfigDir().toFile(), AlwaysMoreItems.NAMESPACE + "/config.yml")));
    }
}