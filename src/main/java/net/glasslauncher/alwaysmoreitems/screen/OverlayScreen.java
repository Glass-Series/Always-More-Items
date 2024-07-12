package net.glasslauncher.alwaysmoreitems.screen;

import net.glasslauncher.alwaysmoreitems.SearchTextFieldWidget;
import net.glasslauncher.alwaysmoreitems.SearchHelper;
import net.minecraft.class_564;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

public class OverlayScreen extends Screen {

    public HandledScreen parent;

    public SearchTextFieldWidget searchField;
    public static int searchFieldWidth = 180;

    // Screen Rescaling Stuff
    int lastWidth = 0;
    int lastHeight = 0;
    class_564 screenScaler;

    public OverlayScreen(HandledScreen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        searchField = new SearchTextFieldWidget(textRenderer, (width / 2) - (searchFieldWidth / 2), height - 25, searchFieldWidth, 20);
        searchField.setMaxLength(64);
        searchField.setText(SearchHelper.searchTerm);
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
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        // Search Field
        searchField.mouseClicked(mouseX, mouseY, button);
        SearchHelper.searchTerm = searchField.getText();
    }

    @Override
    public void keyPressed(char character, int keyCode) {
        super.keyPressed(character, keyCode);

        // Search Field
        if(searchField.isSelected()){
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
