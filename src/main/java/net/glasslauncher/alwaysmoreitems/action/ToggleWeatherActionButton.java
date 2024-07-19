package net.glasslauncher.alwaysmoreitems.action;

import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Formatting;

public class ToggleWeatherActionButton implements ActionButton {

    public ToggleWeatherActionButton() {
    }

    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        if (!isOperator) {
            player.method_490(Formatting.RED + "You need to be opped to do this action!");
            return false;
        }

        if (world.getProperties().getRaining()) {
            world.getProperties().setRaining(false);
            world.getProperties().setRainTime(world.random.nextInt(168000) + 12000); // Required or rain starts again ~60s later
        }
        else {
            world.getProperties().setRaining(true);
            world.getProperties().setRainTime(world.random.nextInt(12000) + 12000); // Also required, or rain will dissipate very quickly.
        }

        return true;
    }

    @Override
    public String getTexture() {
        return "/assets/alwaysmoreitems/stationapi/textures/gui/weather.png";
    }
}
