package net.glasslauncher.alwaysmoreitems.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class ActionButtonC2SPacket extends Packet implements IdentifiablePacket {
    private static final Identifier identifier = AlwaysMoreItems.NAMESPACE.id("action_button");

    public Identifier actionIdentifier;
    public int mouseButton;

    public ActionButtonC2SPacket() {
    }

    public ActionButtonC2SPacket(Identifier actionIdentifier, int mouseButton) {
        this.actionIdentifier = actionIdentifier;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            mouseButton = stream.readInt();
            actionIdentifier = Identifier.of(stream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(mouseButton);
            stream.writeUTF(actionIdentifier.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        handleServer(networkHandler);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler){
        if(networkHandler instanceof ServerPlayNetworkHandler serverPlay) {
            ActionButton actionButton = ActionButtonRegistry.get(actionIdentifier);
            actionButton.perform(null, serverPlay.player.world, serverPlay.player, mouseButton);
        }
    }

    @Override
    public int size() {
        return 4 + actionIdentifier.toString().length();
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    public static void register(){
        IdentifiablePacket.register(identifier, false, true, ActionButtonC2SPacket::new);
    }
}
