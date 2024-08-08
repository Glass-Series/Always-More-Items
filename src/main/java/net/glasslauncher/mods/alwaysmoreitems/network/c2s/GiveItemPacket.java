package net.glasslauncher.mods.alwaysmoreitems.network.c2s;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.network.NetworkHelper;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.*;

public class GiveItemPacket extends Packet implements IdentifiablePacket {
    private static final Identifier IDENTIFIER = AlwaysMoreItems.NAMESPACE.id("give_item");
    private static final String STATION_ID = StationAPI.NAMESPACE.id("id").toString();

    private NbtCompound itemNbt;
    private int dataLength;

    public GiveItemPacket() {
    }

    public GiveItemPacket(NbtCompound nbt) {
        itemNbt = nbt;
    }

    @Override
    public void read(DataInputStream stream) {
        itemNbt = new NbtCompound();
        ((NbtElement) itemNbt).read(stream);
    }

    @Override
    public void write(DataOutputStream stream) {
        dataLength = NetworkHelper.writeAndGetNbtLength(itemNbt, stream);
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        try {
            handleServer(networkHandler);
        } catch (NoSuchMethodError ignored) {
            handleClient();
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        if (networkHandler instanceof ServerPlayNetworkHandler serverPlay) {
            String id = itemNbt.getString(STATION_ID);
            Item item = ItemRegistry.INSTANCE.get(Identifier.of(id));

            if (item == null) {
                AlwaysMoreItems.LOGGER.warn("{} tried to give an invalid item with id {}", serverPlay.getName(), id);
                return;
            }

            if (!serverPlay.server.field_2842.method_584(serverPlay.player.name)) {
                serverPlay.player.method_490(Formatting.RED + "You need to be opped to do this action!");
                return;
            }

            ItemStack itemStack = new ItemStack(itemNbt);
            itemStack.count = Math.min(itemStack.count, itemStack.getMaxCount());
            serverPlay.player.method_490("Gave " + itemStack.count + " " + itemStack.getItem().getTranslatedName() + "@" + itemStack.getDamage());
            serverPlay.player.inventory.method_671(itemStack);

            // Mark the inventory dirty
            serverPlay.player.inventory.markDirty();

            // Send content updates to client
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                if (serverPlay.player.container != null) {
                    serverPlay.player.container.sendContentUpdates();
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
            String id = itemNbt.getString(STATION_ID);
            Item item = ItemRegistry.INSTANCE.get(Identifier.of(id));
            if (item == null) {
                AlwaysMoreItems.LOGGER.warn("Invalid item id {}", id);
                return;
            }

            ItemStack itemStack = new ItemStack(itemNbt);
            itemStack.count = Math.min(itemStack.count, itemStack.getMaxCount());
            Minecraft.INSTANCE.player.inventory.method_671(itemStack);
    }

    @Override
    public int size() {
        return dataLength;
    }

    @Override
    public Identifier getId() {
        return IDENTIFIER;
    }

    public static void register() {
        IdentifiablePacket.register(IDENTIFIER, false, true, GiveItemPacket::new);
    }
}
