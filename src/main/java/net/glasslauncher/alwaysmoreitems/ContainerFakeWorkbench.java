package net.glasslauncher.alwaysmoreitems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

public class ContainerFakeWorkbench extends ScreenHandler {
    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
