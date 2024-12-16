package net.glasslauncher.mods.alwaysmoreitems.network.c2s;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.mods.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class ActionButtonPacket extends Packet implements ManagedPacket<ActionButtonPacket> {
    public static final PacketType<ActionButtonPacket> TYPE = PacketType.builder(false, true, ActionButtonPacket::new).build();

    public Identifier actionIdentifier;
    public int mouseButton;
    boolean holdingShift;

    public ActionButtonPacket() {
    }

    public ActionButtonPacket(Identifier actionIdentifier, int mouseButton, boolean holdingShift) {
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
    public @NotNull PacketType<ActionButtonPacket> getType() {
        return TYPE;
    }
}
