package net.glasslauncher.mods.alwaysmoreitems.action;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.util.Formatting;

public class SetTimeActionButton implements ActionButton {

    public int time;
    public String texture;

    public SetTimeActionButton(int time, String texture) {
        this.time = time;
        this.texture = texture;
    }

    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        if (!isOperator) {
            player.method_490(Formatting.RED + "You need to be opped to do this action!");
            return false;
        }

        WorldProperties worldProperties = world.getProperties();

        AlwaysMoreItems.LOGGER.debug("Time Before: {}", world.getTime());
        long worldTime = world.getTime();
        worldProperties.setTime((worldTime - (worldTime % 24000L)) + time);
        AlwaysMoreItems.LOGGER.debug("Time After: {}", world.getTime());

        return true;
    }

    @Override
    public String getTexture() {
        return texture;
    }
}
