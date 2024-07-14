package net.glasslauncher.alwaysmoreitems.action;

import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DummyActionButton implements ActionButton {

    @Override
    public boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        return false;
    }

    @Override
    public String getTexture() {
        return "/assets/alwaysmoreitems/stationapi/textures/gui/question_mark.png";
    }

    @Override
    public String getClickSound() {
        return "random.drr";
    }
}
