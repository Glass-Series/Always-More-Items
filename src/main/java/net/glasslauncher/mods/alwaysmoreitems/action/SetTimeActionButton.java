package net.glasslauncher.mods.alwaysmoreitems.action;

import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButtonEnvironment;
import net.glasslauncher.mods.alwaysmoreitems.config.OverlayMode;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetTimeActionButton implements ActionButton {

    private final List<OverlayMode> allowedOverlayModes = List.of(OverlayMode.CHEAT, OverlayMode.UTILITY);

    public int time;
    public String texture;

    public SetTimeActionButton(int time, String texture) {
        this.time = time;
        this.texture = texture;
    }

    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        if (!isOperator) {
            player.sendMessage(Formatting.RED + "You need to be opped to do this action!");
            return false;
        }

        AlwaysMoreItems.LOGGER.debug("Time Before: {}", world.getTime());
        world.getProperties().setTime(getNewTime(world));
        AlwaysMoreItems.LOGGER.debug("Time After: {}", world.getTime());

        return true;
    }

    @Override
    public boolean performClient(Minecraft minecraft, int mouseButton, boolean holdingShift) {
        minecraft.player.sendChatMessage("/time set " + getNewTime(minecraft.world));
        return true;
    }

    @Override
    public @Nullable List<OverlayMode> allowedOverlayModes() {
        return allowedOverlayModes;
    }

    @Override
    public String getTexture() {
        return texture;
    }

    public long getNewTime(World world) {
        long worldTime = world.getTime();
        long timeRemainder = worldTime % 24000L;
        return worldTime - timeRemainder + (timeRemainder <= time ? 0 : 24000L) + time;
    }

    @Override
    public ActionButtonEnvironment getActionEnvironment() {
        return ActionButtonEnvironment.SERVER_AMI_PRESENT_OR_CLIENT;
    }
}
