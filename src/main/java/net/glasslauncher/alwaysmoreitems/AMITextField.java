package net.glasslauncher.alwaysmoreitems;

import net.glasslauncher.mods.gcapi.impl.screen.widget.ExtensibleTextFieldWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class AMITextField extends ExtensibleTextFieldWidget {

    public AMITextField(TextRenderer textRenderer, int x, int y, int height, int width) {
        super(textRenderer);
        glass_config_api$setXYWH(x, y, width, height);
    }
}
