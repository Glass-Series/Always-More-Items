package net.glasslauncher.mods.alwaysmoreitems.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.*;
import java.util.*;

public class Commands {

	public static void giveOneFromStack(@Nonnull ItemStack itemstack) {
		giveStack(itemstack, 1);
	}

	/**
	 * /give <player> <item> [amount] [data] [dataTag]
	 */
	public static void giveStack(@Nonnull ItemStack itemStack, int amount) {
		ClientPlayerEntity sender = Minecraft.INSTANCE.player;
		String senderName = sender.name;

		List<String> commandStrings = new ArrayList<>();
		commandStrings.add("/give");
		commandStrings.add(senderName);
		commandStrings.add(ItemRegistry.INSTANCE.getId(itemStack.getItem()).toString());
		commandStrings.add(String.valueOf(amount));

		String fullCommand = StringUtils.join(commandStrings, " ");
		sender.sendChatMessage(fullCommand);
	}
}
