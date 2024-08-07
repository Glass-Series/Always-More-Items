package net.glasslauncher.mods.alwaysmoreitems.gui.widget;

import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.gui.RenderHelper;
import net.minecraft.client.Minecraft;

public class AMISettingsButton extends ActionButtonWidget {
    public AMISettingsButton(int id, int x, int y) {
        super(id, x, y, 22, 22, "");
        iconType = ButtonIconType.OTHER;
    }

    @Override
    public void render(Minecraft minecraft, int mouseX, int mouseY) {
        if(!visible) {
            return;
        }
        texture = "/assets/alwaysmoreitems/stationapi/textures/gui/settings%s.png".formatted(AMIConfig.INSTANCE.cheatMode ? "_cheat" : "");
        super.render(minecraft, mouseX, mouseY);
        RenderHelper.bindTexture(texture);
        RenderHelper.drawTexture(x + 1, y + 1, width - 2, height - 2);
    }
}
