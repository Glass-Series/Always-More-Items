package net.glasslauncher.mods.alwaysmoreitems.network.s2c;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.api.ModPluginProvider;
import net.glasslauncher.mods.alwaysmoreitems.api.SyncableRecipe;
import net.glasslauncher.mods.alwaysmoreitems.init.CommonInit;
import net.glasslauncher.mods.alwaysmoreitems.network.NetworkHelper;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.ModRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * This is a LARGE packet, so don't resend it, instead make a blacklist plugin instead of fucking with this packet.
 * I'm not kidding, this thing's going to end up multiple megabytes in size on even a small modpack, nevermind when you load 100+ mods.
 */
public class RecipeSyncPacket extends Packet implements ManagedPacket<RecipeSyncPacket> {
    public static final PacketType<RecipeSyncPacket> TYPE = PacketType.builder(true, false, RecipeSyncPacket::new).build();
    private static final String PLUGIN_KEY = AlwaysMoreItems.NAMESPACE.id("plugin").toString();

    private static NbtList CACHED_DATA;

    private int outSize;
    private ArrayList<SyncableRecipe> readData;

    @Override
    public void read(DataInputStream stream) {
        NbtList nbtList = new NbtList();
        nbtList.read(stream);
        ArrayList<SyncableRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = (NbtCompound) nbtList.get(i);
            String pluginKey = nbtCompound.getString(PLUGIN_KEY);
            ModPluginProvider plugin = CommonInit.getPlugins().get(Identifier.of(pluginKey));
            if (plugin == null) {
                AlwaysMoreItems.LOGGER.warn("Plugin {} not found, recipes from this plugin won't function. Are you missing a mod?", pluginKey);
                continue;
            }
            SyncableRecipe syncableRecipe = plugin.deserializeRecipe(nbtCompound);
            if (syncableRecipe != null) {
                recipes.add(syncableRecipe);
            }
        }
        readData = recipes;
    }

    @Override
    public void write(DataOutputStream stream) {
        if (CACHED_DATA == null) {
            AlwaysMoreItems.LOGGER.info("Loading recipes to server cache.");
            CACHED_DATA = new NbtList();
            AlwaysMoreItems.getRecipeRegistry().getSyncableRecipes().forEach(syncableRecipe -> {
                NbtCompound nbtCompound = syncableRecipe.exportRecipe();
                nbtCompound.putString(PLUGIN_KEY, syncableRecipe.getPlugin().toString());
                CACHED_DATA.add(nbtCompound);
            });
        }
        outSize = NetworkHelper.writeAndGetNbtLength(CACHED_DATA, stream);
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
            return;
        }
        // Holy shit this code is awful.
        // TODO: Make this way less awful when I find the inspiration
        ModRegistry modRegistry = new ModRegistry(CommonInit.getModRegistry());
        modRegistry.addRecipes(readData);
        modRegistry.addRecipes(AlwaysMoreItems.getRecipeRegistry().getUnsyncableRecipes());
        AlwaysMoreItems.setRecipeRegistry(modRegistry.createRecipeRegistry());
        AlwaysMoreItems.LOGGER.info("Synced {} recipes from server to client, skipped {} unhandled recipes.", readData.size(), AlwaysMoreItems.getRecipeRegistry().getUnsyncableRecipes().size());
    }

    @Override
    public int size() {
        return outSize;
    }

    @Override
    public @NotNull PacketType<RecipeSyncPacket> getType() {
        return TYPE;
    }
}
