package net.glasslauncher.mods.alwaysmoreitems.network.c2s;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.api.AMINbt;
import net.glasslauncher.mods.alwaysmoreitems.network.NetworkHelper;
import net.glasslauncher.mods.alwaysmoreitems.transfer.BasicRecipeTransferHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.nbt.NbtIntArray;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.*;

public class RecipeTransferPacket extends Packet implements ManagedPacket<RecipeTransferPacket> {
    public static final PacketType<RecipeTransferPacket> TYPE = PacketType.builder(false, true, RecipeTransferPacket::new).build();

    private int outSize;

    private Map<Integer, ItemStack> recipeMap;
    private List<Integer> craftingSlots;
    private List<Integer> inventorySlots;
    private boolean maxTransfer;

    public RecipeTransferPacket() {

    }

    public RecipeTransferPacket(@Nonnull Map<Integer, ItemStack> recipeMap, @Nonnull List<Integer> craftingSlots, @Nonnull List<Integer> inventorySlots, boolean maxTransfer) {
        this.recipeMap = recipeMap;
        this.craftingSlots = craftingSlots;
        this.inventorySlots = inventorySlots;
        this.maxTransfer = maxTransfer;
    }

    @Override
    public void read(DataInputStream buf) {
        NbtCompound data = new NbtCompound();
        ((NbtElement) data).read(buf);

        NbtCompound nbtRecipeMap = data.getCompound("recipeMap");
        recipeMap = new HashMap<>();
        ((AMINbt) nbtRecipeMap).always_More_Items$entrySet().forEach(entry -> recipeMap.put(Integer.parseInt((String) entry.getKey()), new ItemStack((NbtCompound) entry.getValue())));

        craftingSlots = new ArrayList<>();
        Arrays.stream(data.getIntArray("craftingSlots")).forEach(i -> craftingSlots.add(i));

        inventorySlots = new ArrayList<>();
        Arrays.stream(data.getIntArray("inventorySlots")).forEach(i -> inventorySlots.add(i));

        maxTransfer = data.getBoolean("maxTransfer");
    }

    @Override
    public void write(DataOutputStream buf) {
        NbtCompound outData = new NbtCompound();

        NbtCompound nbtRecipeMap = new NbtCompound();
        recipeMap.forEach((key, value) -> {
            NbtCompound item = new NbtCompound();
            value.writeNbt(item);
            nbtRecipeMap.put(key.toString(), item);
        });
        outData.put("recipeMap", nbtRecipeMap);

        outData.put("craftingSlots", new NbtIntArray(craftingSlots.stream().mapToInt(Integer::intValue).toArray()));
        outData.put("inventorySlots", new NbtIntArray(inventorySlots.stream().mapToInt(Integer::intValue).toArray()));

        outData.putBoolean("maxTransfer", maxTransfer);

        outSize = NetworkHelper.writeAndGetNbtLength(outData, buf);
    }

    @Environment(EnvType.CLIENT)
    private PlayerEntity getClientPlayer() {
        return Minecraft.INSTANCE.player;
    }

    @Environment(EnvType.SERVER)
    private PlayerEntity getServerPlayer(NetworkHandler networkHandler) {
        return ((ServerPlayNetworkHandler) networkHandler).player;
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        PlayerEntity player;
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
            player = getServerPlayer(networkHandler);
        }
        else {
            player = getClientPlayer();
        }
        BasicRecipeTransferHandler.setItems(player, recipeMap, craftingSlots, inventorySlots, maxTransfer);
    }

    @Override
    public int size() {
        return outSize;
    }

    @Override
    public @NotNull PacketType<RecipeTransferPacket> getType() {
        return TYPE;
    }
}
