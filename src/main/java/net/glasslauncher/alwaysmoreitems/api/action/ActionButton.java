package net.glasslauncher.alwaysmoreitems.api.action;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("UnusedReturnValue")
public interface ActionButton {
    boolean perform(Minecraft minecraft, World world, PlayerEntity player, int mouseButton);

    default int getWidth(){
        return 20;
    }

    default int getHeight(){
        return 20;
    }

    String getTexture();

    default String getClickSound(){
        return "random.click";
    }

    default boolean isClientsideOnly(){
        return false;
    }
}
