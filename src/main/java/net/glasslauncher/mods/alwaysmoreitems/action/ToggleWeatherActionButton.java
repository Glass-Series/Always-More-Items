package net.glasslauncher.mods.alwaysmoreitems.action;

import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.glasslauncher.mods.alwaysmoreitems.config.OverlayMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToggleWeatherActionButton implements ActionButton {

    private final List<OverlayMode> allowedOverlayModes = List.of(OverlayMode.CHEAT, OverlayMode.UTILITY);

    public ToggleWeatherActionButton() {
    }

    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        if (!isOperator) {
            player.sendMessage(Formatting.RED + "You need to be opped to do this action!");
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
    public @Nullable List<OverlayMode> allowedOverlayModes() {
        return allowedOverlayModes;
    }

    @Override
    public String getTexture() {
        return "/assets/alwaysmoreitems/stationapi/textures/gui/rain.png";
    }
}
