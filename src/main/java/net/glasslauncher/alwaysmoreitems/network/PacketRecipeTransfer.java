package net.glasslauncher.alwaysmoreitems.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.AMINbt;
import net.glasslauncher.alwaysmoreitems.transfer.BasicRecipeTransferHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.nbt.NbtIntArray;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.annotation.*;
import java.io.*;
import java.util.*;

public class PacketRecipeTransfer extends Packet implements IdentifiablePacket {

	public static final Identifier identifier = AlwaysMoreItems.NAMESPACE.id("transfer");

	private int outSize;

	private Map<Integer, ItemStack> recipeMap;
	private List<Integer> craftingSlots;
	private List<Integer> inventorySlots;
	private boolean maxTransfer;

	public PacketRecipeTransfer() {

	}

	public PacketRecipeTransfer(@Nonnull Map<Integer, ItemStack> recipeMap, @Nonnull List<Integer> craftingSlots, @Nonnull List<Integer> inventorySlots, boolean maxTransfer) {
		this.recipeMap = recipeMap;
		this.craftingSlots = craftingSlots;
		this.inventorySlots = inventorySlots;
		this.maxTransfer = maxTransfer;
	}

	@Override
	public Identifier getId() {
		return identifier;
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

		// Semi-wasteful, but better than an actual output stream.
		DataOutputStream outputStream = new DataOutputStream(ByteArrayOutputStream.nullOutputStream());
		((NbtElement) outData).write(outputStream);
		outSize = outputStream.size();
		((NbtElement) outData).write(buf);
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

	public static void register() {
		IdentifiablePacket.register(identifier, false, true, PacketRecipeTransfer::new);
	}
}
