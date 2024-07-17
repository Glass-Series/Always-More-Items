package net.glasslauncher.alwaysmoreitems.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class GiveItemC2SPacket extends Packet implements IdentifiablePacket {
    private static final Identifier identifier = AlwaysMoreItems.NAMESPACE.id("give_item");

    public int id;
    public int count;
    public int damage;
//    public Identifier itemIdentifier;

    public GiveItemC2SPacket() {
    }

    public GiveItemC2SPacket(int id) {
        this(id, 64);
    }

    public GiveItemC2SPacket(int id, int count) {
        this(id, count, 0);
    }

    public GiveItemC2SPacket(int id, int count, int damage) {
        this.id = id;
        this.count = count;
        this.damage = damage;
    }

    //    public GiveItemC2SPacket(Identifier itemIdentifier, int damage, int count) {
//        this.itemIdentifier = itemIdentifier;
//        this.damage = damage;
//        this.count = count;
//    }

    @Override
    public void read(DataInputStream stream) {
        try {
            id = stream.readInt();
            count = stream.readInt();
            damage = stream.readInt();
//            itemIdentifier = Identifier.of(stream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(id);
            stream.writeInt(count);
            stream.writeInt(damage);
//            stream.writeUTF(itemIdentifier.toString());
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
//            Item item = ItemRegistry.INSTANCE.get(itemIdentifier);

//            if (item == null) {
//                AlwaysMoreItems.LOGGER.warn("{} tried to give an invalid item with id {}", serverPlay.getName(), itemIdentifier);
//                return;
//            }

            if (!serverPlay.server.field_2842.method_584(serverPlay.player.name)) {
                serverPlay.player.method_490(Formatting.RED + "You need to be opped to do this action!");
                return;
            }

            serverPlay.player.inventory.method_671(new ItemStack(id, count, damage));
        }
    }

    @Override
    public int size() {
        return 12;// + itemIdentifier.toString().length();
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    public static void register() {
        IdentifiablePacket.register(identifier, false, true, GiveItemC2SPacket::new);
    }
}
