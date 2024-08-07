package net.glasslauncher.mods.alwaysmoreitems.gui.widget;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.gcapi3.impl.screen.widget.ExtensibleTextFieldWidget;
import net.minecraft.client.font.TextRenderer;

public class SearchTextFieldWidget extends ExtensibleTextFieldWidget {

    public SearchTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer);
        setXYWH(x, y, width, height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(mouseX >= getXYWH()[0] && mouseX < getXYWH()[0] + getXYWH()[2] && mouseY >= getXYWH()[1] && mouseY < getXYWH()[1] + getXYWH()[3] && button == 1){
            this.setText("");
            AlwaysMoreItems.getItemFilter().reset();
        }

        super.mouseClicked(mouseX, mouseY, button);
    }
}
