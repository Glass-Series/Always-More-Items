package net.glasslauncher.alwaysmoreitems.action;

import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Formatting;

public class HealActionButton implements ActionButton {
    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        if(isOperator){
            player.heal(player.maxHealth);
            return true;
        }
        player.method_490(Formatting.RED + "You need to be opped to do this action!");
        return false;
    }

    @Override
    public String getTexture() {
        return "/assets/alwaysmoreitems/stationapi/textures/gui/heart.png";
    }
}
