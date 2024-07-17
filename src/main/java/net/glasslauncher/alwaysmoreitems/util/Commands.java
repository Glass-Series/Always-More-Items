package net.glasslauncher.alwaysmoreitems.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.ClientPlayerEntity;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class Commands {

	public static void giveFullStack(@Nonnull ItemStack itemstack) {
		giveStack(itemstack, itemstack.getMaxCount());
	}

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
//		commandStrings.add(String.valueOf(itemStack.getMetadata())); // custom packet needed

//		if (itemStack.hasTagCompound()) {
//			commandStrings.add(itemStack.getTagCompound().toString());
//		}

		String fullCommand = StringUtils.join(commandStrings, " ");
		sender.sendChatMessage(fullCommand);
	}
}
