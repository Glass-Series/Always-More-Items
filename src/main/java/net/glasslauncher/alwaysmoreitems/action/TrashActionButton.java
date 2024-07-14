package net.glasslauncher.alwaysmoreitems.action;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

public class TrashActionButton implements ActionButton {
    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        // If player inventory is null for some reason, return
        if(player.inventory == null){
            return false;
        }

        // If not LMB, return
        if(mouseButton != 0){
            return false;
        }

        // Delete the cursors stack
        player.inventory.setCursorStack(null);

        // If Player is holding SHIFT and is opped, delete the inventory
        if (holdingShift && isOperator) {
            for (int i = 0; i < player.inventory.size(); i++) {
                player.inventory.setStack(i, null);
            }
        }

        // Mark the inventory dirty
        player.inventory.markDirty();

        // If on server, send content updates to client
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER){
            if(player instanceof ServerPlayerEntity serverPlayer){
                if(serverPlayer.container != null){
                    serverPlayer.container.sendContentUpdates();
                }
            }
        }
        return true;
    }

    @Override
    public String getTexture() {
        return null;
    }

    @Override
    public boolean dontAddToScreen() {
        return true;
    }

    @Override
    public boolean tooltipEnabled() {
        return false;
    }
}
