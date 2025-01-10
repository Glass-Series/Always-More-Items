package net.glasslauncher.mods.alwaysmoreitems.action;

import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Formatting;

public class HealActionButton implements ActionButton {
    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        if (!isOperator) {
            player.method_490(Formatting.RED + "You need to be opped to do this action!");
            return false;
        }

        // If Holding SHIFT and pressing MMB, then cause suicide
        if (holdingShift && mouseButton == 2) {
            player.damage(null, 9001);
        } else {
            player.heal(player.maxHealth);
        }

        return true;
    }

    @Override
    public boolean cheatModeOnly() {
        return true;
    }

    @Override
    public String getTexture() {
        return "/assets/alwaysmoreitems/stationapi/textures/gui/heart.png";
    }
}
