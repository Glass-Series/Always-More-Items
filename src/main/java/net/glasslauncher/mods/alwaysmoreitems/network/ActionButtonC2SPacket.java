package net.glasslauncher.mods.alwaysmoreitems.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.mods.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.*;

@SuppressWarnings("CallToPrintStackTrace")
public class ActionButtonC2SPacket extends Packet implements IdentifiablePacket {
    private static final Identifier identifier = AlwaysMoreItems.NAMESPACE.id("action_button");

    public Identifier actionIdentifier;
    public int mouseButton;
    boolean holdingShift;

    public ActionButtonC2SPacket() {
    }

    public ActionButtonC2SPacket(Identifier actionIdentifier, int mouseButton, boolean holdingShift) {
        this.actionIdentifier = actionIdentifier;
        this.mouseButton = mouseButton;
        this.holdingShift = holdingShift;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            mouseButton = stream.readInt();
            holdingShift = stream.readBoolean();
            actionIdentifier = Identifier.of(stream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(mouseButton);
            stream.writeBoolean(holdingShift);
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
    public void handleServer(NetworkHandler networkHandler) {
        if (networkHandler instanceof ServerPlayNetworkHandler serverPlay) {
            ActionButton actionButton = ActionButtonRegistry.INSTANCE.get(actionIdentifier);
            if (actionButton != null) {
                actionButton.perform(
                        serverPlay.server,
                        serverPlay.player.world,
                        serverPlay.player,
                        serverPlay.server.field_2842.method_584(serverPlay.player.name), // serverPlay.server.ops.contains(playername)
                        mouseButton,
                        holdingShift
                );
            } else {
                AlwaysMoreItems.LOGGER.warn("Player {} tried to execute invalid action {}", serverPlay.player.name, actionIdentifier);
            }
        }
    }

    @Override
    public int size() {
        return 5 + actionIdentifier.toString().length();
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    public static void register() {
        IdentifiablePacket.register(identifier, false, true, ActionButtonC2SPacket::new);
    }
}
