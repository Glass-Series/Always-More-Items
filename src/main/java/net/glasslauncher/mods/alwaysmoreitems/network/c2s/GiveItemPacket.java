package net.glasslauncher.mods.alwaysmoreitems.network.c2s;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.*;

@SuppressWarnings("CallToPrintStackTrace")
public class GiveItemPacket extends Packet implements IdentifiablePacket {
    private static final Identifier IDENTIFIER = AlwaysMoreItems.NAMESPACE.id("give_item");

    //    public int id;
    public int damage;
    public int count;
    public Identifier itemIdentifier;

    public GiveItemPacket() {
    }

    public GiveItemPacket(Identifier itemIdentifier, int damage, int count) {
        this.itemIdentifier = itemIdentifier;
        this.damage = damage;
        this.count = count;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            count = stream.readInt();
            damage = stream.readInt();
            itemIdentifier = Identifier.of(stream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(count);
            stream.writeInt(damage);
            stream.writeUTF(itemIdentifier.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        handleServer(networkHandler);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        if (networkHandler instanceof ServerPlayNetworkHandler serverPlay) {
            Item item = ItemRegistry.INSTANCE.get(itemIdentifier);

            if (item == null) {
                AlwaysMoreItems.LOGGER.warn("{} tried to give an invalid item with id {}", serverPlay.getName(), itemIdentifier);
                return;
            }

            if (!serverPlay.server.field_2842.method_584(serverPlay.player.name)) {
                serverPlay.player.method_490(Formatting.RED + "You need to be opped to do this action!");
                return;
            }

            serverPlay.player.inventory.method_671(new ItemStack(ItemRegistry.INSTANCE.getRawId(item), count, damage));
            serverPlay.player.method_490("Gave " + count + " " + item.getTranslatedName() + "@" + damage);

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

    @Override
    public int size() {
        return 8 + itemIdentifier.toString().length();
    }

    @Override
    public Identifier getId() {
        return IDENTIFIER;
    }

    public static void register() {
        IdentifiablePacket.register(IDENTIFIER, false, true, GiveItemPacket::new);
    }

    @Override
    public String toString() {
        return "GiveItemC2SPacket{" +
                "count=" + count +
                ", damage=" + damage +
                ", itemIdentifier=" + itemIdentifier +
                '}';
    }
}
