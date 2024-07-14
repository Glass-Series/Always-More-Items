package net.glasslauncher.alwaysmoreitems.gui.screen;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.alwaysmoreitems.gui.widget.ActionButtonWidget;
import net.glasslauncher.alwaysmoreitems.gui.widget.SearchTextFieldWidget;
import net.glasslauncher.alwaysmoreitems.SearchHelper;
import net.glasslauncher.alwaysmoreitems.network.ActionButtonC2SPacket;
import net.minecraft.class_564;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class OverlayScreen extends Screen {

    public HandledScreen parent;

    public SearchTextFieldWidget searchField;
    public static int searchFieldWidth = 180;

    ArrayList<ActionButtonWidget> actionButtons;
    public static int maxActionButtonPanelWidth = 100;
    public static int actionButtonOffset = 2;


    // Screen Rescaling Stuff
    int lastWidth = 0;
    int lastHeight = 0;
    class_564 screenScaler;

    public OverlayScreen(HandledScreen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        int id = 0;

        searchField = new SearchTextFieldWidget(textRenderer, (width / 2) - (searchFieldWidth / 2), height - 25, searchFieldWidth, 20);
        searchField.setMaxLength(64);
        searchField.setText(SearchHelper.searchTerm);

        actionButtons = new ArrayList<>();
        int actionButtonX = 0;
        int actionButtonY = 0;
        int maxHeightForLine = 0;

        for (var action : ActionButtonRegistry.registry.entrySet()) {
            if (actionButtonX + action.getValue().getWidth() > maxActionButtonPanelWidth) {
                actionButtonX = 0;
                actionButtonY += maxHeightForLine + actionButtonOffset;
                maxHeightForLine = 0;
            }

            if (action.getValue().getHeight() > maxHeightForLine) {
                maxHeightForLine = action.getValue().getHeight();
            }

            ActionButtonWidget widget = new ActionButtonWidget(id++, actionButtonX, actionButtonY, action.getValue().getWidth(), action.getValue().getHeight(), action.getValue().getTexture());
            actionButtons.add(widget);
            widget.action = action.getValue();
            widget.actionIdentifier = action.getKey();

            actionButtonX += action.getValue().getWidth() + actionButtonOffset;
        }

    }

    @Override
    public void tick() {
        rescale();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        searchField.draw(mouseX, mouseY);
        textRenderer.drawWithShadow("CAALM", 0, 0, 16722100);

        for (var actionButton : actionButtons) {
            actionButton.render(minecraft, mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        // Search Field
        searchField.mouseClicked(mouseX, mouseY, button);
        SearchHelper.searchTerm = searchField.getText();

        for (var actionButton : actionButtons) {
            if (actionButton.isMouseOver(minecraft, mouseX, mouseY)) {
                this.minecraft.soundManager.playSound(actionButton.action.getClickSound(), 1.0F, 1.0F);
                if (!minecraft.world.isRemote || actionButton.action.isClientsideOnly()) {
                    actionButton.performAction(minecraft, minecraft.world, minecraft.player, true, button);
                } else {
                    System.out.println("Not done yet ¯\\_(ツ)_/¯");
                    PacketHelper.send(new ActionButtonC2SPacket(actionButton.actionIdentifier, button));
                }
            }
        }
    }

    @Override
    public void keyPressed(char character, int keyCode) {
        super.keyPressed(character, keyCode);

        // Search Field
        if (searchField.isSelected()) {
            searchField.keyPressed(character, keyCode);
            SearchHelper.searchTerm = searchField.getText();
        }
    }

    /**
     * Checks if the display dimensions have changed since the last time the method was ran and if so,
     * recalculates the scaled width and height
     */
    public void rescale() {
        if (minecraft.displayWidth != lastWidth || minecraft.displayHeight != lastHeight) {
            screenScaler = new class_564(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
            width = screenScaler.method_1857();
            height = screenScaler.method_1858();
            lastWidth = minecraft.displayWidth;
            lastHeight = minecraft.displayHeight;
            init();
        }
    }
}
