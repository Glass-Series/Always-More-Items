package net.glasslauncher.alwaysmoreitems.action;

import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class HealActionButton implements ActionButton {
    @Override
    public boolean perform(Minecraft minecraft, World world, PlayerEntity player, int mouseButton) {
        player.heal(player.maxHealth);
        return true;
    }

    @Override
    public String getTexture() {
        return "/assets/alwaysmoreitems/stationapi/textures/gui/heart.png";
    }
}
