package net.glasslauncher.mods.alwaysmoreitems.gui.screen;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ActionButtonWidget;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.RecipeTransferButton;
import net.glasslauncher.mods.alwaysmoreitems.init.KeybindListener;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.glasslauncher.mods.alwaysmoreitems.transfer.RecipeTransferUtil;
import net.glasslauncher.mods.alwaysmoreitems.util.HoverChecker;
import net.glasslauncher.mods.alwaysmoreitems.util.RecipeGuiLogic;
import net.glasslauncher.mods.alwaysmoreitems.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ParticlesGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class RecipesGui extends Screen {
    private static final int borderPadding = 8;
    private static final int textPadding = 5;
    @Getter @Setter
    private boolean active = false;

    public Screen parent;

    private int titleHeight;
    private int headerHeight;
    private int buttonWidth;

    /* Internal logic for the gui, handles finding recipes */
    private final RecipeGuiLogic logic = new RecipeGuiLogic();

    /* List of RecipeLayout to display */
    @Nonnull
    private final List<RecipeLayout> recipeLayouts = new ArrayList<>();

    private String pageString;
    private String title;
    private String backgroundTexture;
    private HoverChecker titleHoverChecker;

    private ActionButtonWidget nextRecipeCategory;
    private ActionButtonWidget previousRecipeCategory;
    private ActionButtonWidget nextPage;
    private ActionButtonWidget previousPage;

    @Nullable
    private RecipeLayout hovered;

    private int guiLeft;
    private int guiTop;
    @Getter
    private int xSize;
    @Getter
    private int ySize;

    public RecipesGui() {
    }

    @Override
    public void init() {
        minecraft = Minecraft.INSTANCE;
        // Race condition go fucking lmao
        if (minecraft.currentScreen == null) {
            return;
        }
        particlesGui = new ParticlesGui(minecraft);
        textRenderer = minecraft.textRenderer;
        buttons.clear();

        xSize = 176;

        if (height > 300) {
            ySize = 256;
            backgroundTexture = "/assets/alwaysmoreitems/stationapi/textures/gui/recipeBackgroundTall.png";
        } else {
            ySize = 166;
            backgroundTexture = "/assets/alwaysmoreitems/stationapi/textures/gui/recipeBackground.png";
        }

        width = minecraft.currentScreen.width;
        height = minecraft.currentScreen.height;

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        titleHeight = AMITextRenderer.FONT_HEIGHT + borderPadding;
        headerHeight = titleHeight + AMITextRenderer.FONT_HEIGHT + textPadding;

        buttonWidth = 13;

        int buttonHeight = AMITextRenderer.FONT_HEIGHT + 3;

        int rightButtonX = guiLeft + xSize - borderPadding - buttonWidth;
        int leftButtonX = guiLeft + borderPadding;

        int recipeClassButtonTop = guiTop + borderPadding - 2;
        nextRecipeCategory = new ActionButtonWidget(2, rightButtonX, recipeClassButtonTop, buttonWidth, buttonHeight, "gui.alwaysmoreitems.nextCharacter", "gui.alwaysmoreitems.nextCharacter");
        previousRecipeCategory = new ActionButtonWidget(3, leftButtonX, recipeClassButtonTop, buttonWidth, buttonHeight, "gui.alwaysmoreitems.previousCharacter", "gui.alwaysmoreitems.previousCharacter");

        int pageButtonTop = guiTop + titleHeight + 3;
        nextPage = new ActionButtonWidget(4, rightButtonX, pageButtonTop, buttonWidth, buttonHeight, "gui.alwaysmoreitems.nextCharacter", "gui.alwaysmoreitems.nextCharacter");
        previousPage = new ActionButtonWidget(5, leftButtonX, pageButtonTop, buttonWidth, buttonHeight, "gui.alwaysmoreitems.previousCharacter", "gui.alwaysmoreitems.previousCharacter");

        addButtons();

        updateLayout();
    }

    @SuppressWarnings("unchecked")
    private void addButtons() {
        buttons.add(nextRecipeCategory);
        buttons.add(previousRecipeCategory);
        buttons.add(nextPage);
        buttons.add(previousPage);
    }
    
    public boolean isMouseOver(int mouseX, int mouseY) {
        return (mouseX >= guiLeft) && (mouseY >= guiTop) && (mouseX < guiLeft + xSize) && (mouseY < guiTop + ySize);
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!active) {
            return;
        }

        if (!isMouseOver(mouseX, mouseY)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        for (RecipeLayout recipeLayout : recipeLayouts) {
            if (recipeLayout.handleClick(minecraft, mouseX, mouseY, mouseButton)) {
                return;
            }
        }

        if (titleHoverChecker.isOver(mouseX, mouseY)) {
            boolean success = logic.setCategoryFocus();
            if (success) {
                updateLayout();
            } else {
                super.mouseClicked(mouseX, mouseY, mouseButton);
            }
            return;
        }

        if (hovered != null) {
            if (mouseButton == 0) {
                Focus focus = hovered.getItemStacks().getFocusUnderMouse(mouseX - hovered.getPosX(), mouseY - hovered.getPosY());
                if (focus != null) {
                    showRecipes(focus);
                    return;
                }
            }
            if (mouseButton == 1) {
                Focus focus = hovered.getItemStacks().getFocusUnderMouse(mouseX - hovered.getPosX(), mouseY - hovered.getPosY());
                if (focus != null) {
                    showUses(focus);
                    return;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void handleMouseScrolled(int mouseX, int mouseY) {
        if (!isMouseOver(mouseX, mouseY)) {
            return;
        }

        int scrollDelta = Mouse.getDWheel();
        
        if (hovered != null && scrollDelta != 0) {
            if (hovered.handleMouseScrolled(minecraft, mouseX, mouseY, scrollDelta)) {
                return;
            }
        }
        
        if (scrollDelta < 0) {
            logic.nextPage();
            updateLayout();
        }
        else if (scrollDelta > 0) {
            logic.previousPage();
            updateLayout();
        }
    }

    @Override
    public void onMouseEvent() {
        if (!active) {
            return;
        }

        super.onMouseEvent();
    }

    @Override
    protected void keyPressed(char character, int keyCode) {
        if (!active) {
            return;
        }
        
        if (hovered != null) {
            if (hovered.handleKeyPress(minecraft, character, keyCode)) {
                return;
            }
        }
        
        super.keyPressed(character, keyCode);
    }

    public void open(Screen newParent) {
        if (newParent != this) {
            parent = newParent;
        }
        active = true;
        init();
    }

    public void close() {
        active = false;
    }

    public void showRecipes(@Nonnull Focus focus) {
        focus.setMode(Focus.Mode.OUTPUT);
        if (logic.setFocus(focus)) {
            updateLayout();
            open(Minecraft.INSTANCE.currentScreen);
        }
    }

    public void showUses(@Nonnull Focus focus) {
        focus.setMode(Focus.Mode.INPUT);
        if (logic.setFocus(focus)) {
            updateLayout();
            open(Minecraft.INSTANCE.currentScreen);
        }
    }

    public void showCategories(@Nonnull List<String> recipeCategoryUids) {
        if (logic.setCategoryFocus(recipeCategoryUids)) {
            updateLayout();
            open(Minecraft.INSTANCE.currentScreen);
        }
    }

    public void back() {
        if (logic.back()) {
            updateLayout();
        }
    }

    @Override
    protected void buttonClicked(@Nonnull ButtonWidget guibutton) {
        boolean updateLayout = true;

        if (guibutton.id == nextPage.id) {
            logic.nextPage();
        } else if (guibutton.id == previousPage.id) {
            logic.previousPage();
        } else if (guibutton.id == nextRecipeCategory.id) {
            logic.nextRecipeCategory();
        } else if (guibutton.id == previousRecipeCategory.id) {
            logic.previousRecipeCategory();
        } else if (guibutton.id >= RecipeLayout.recipeTransferButtonIndex) {
            int recipeIndex = guibutton.id - RecipeLayout.recipeTransferButtonIndex;
            RecipeLayout recipeLayout = recipeLayouts.get(recipeIndex);
            boolean maxTransfer = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
            if (RecipeTransferUtil.transferRecipe(recipeLayout, minecraft.player, maxTransfer)) {
                close();
                updateLayout = false;
            }
        } else {
            updateLayout = false;
        }

        if (updateLayout) {
            updateLayout();
        }
    }

    private void updateLayout() {
        RecipeCategory recipeCategory = logic.getRecipeCategory();
        if (recipeCategory == null) {
            return;
        }

        AMIDrawable recipeBackground = recipeCategory.getBackground();

        final int recipesPerPage = Math.max(1, (ySize - headerHeight) / (recipeBackground.getHeight() + borderPadding));
        final int recipeXOffset = (xSize - recipeBackground.getWidth()) / 2;
        final int recipeSpacing = (ySize - headerHeight - (recipesPerPage * recipeBackground.getHeight())) / (recipesPerPage + 1);

        logic.setRecipesPerPage(recipesPerPage);

        title = recipeCategory.getTitle();
        int titleWidth = textRenderer.getWidth(title);
        int titleX = guiLeft + (xSize - titleWidth) / 2;
        int titleY = guiTop + borderPadding;
        titleHoverChecker = new HoverChecker(titleY, titleY + AMITextRenderer.FONT_HEIGHT, titleX, titleX + titleWidth);

        int posX = guiLeft + recipeXOffset;
        int posY = guiTop + headerHeight + recipeSpacing;
        int spacingY = recipeBackground.getHeight() + recipeSpacing;

        recipeLayouts.clear();
        recipeLayouts.addAll(logic.getRecipeWidgets(posX, posY, spacingY));
        addRecipeTransferButtons(recipeLayouts);

        nextPage.active = previousPage.active = logic.hasMultiplePages();
        nextRecipeCategory.active = previousRecipeCategory.active = logic.hasMultipleCategories();

        pageString = logic.getPageString();
    }

    private void addRecipeTransferButtons(List<RecipeLayout> recipeLayouts) {
        buttons.clear();
        addButtons();

        PlayerEntity player = Minecraft.INSTANCE.player;

        for (RecipeLayout recipeLayout : recipeLayouts) {
            RecipeTransferButton button = recipeLayout.getRecipeTransferButton();
            button.init(recipeLayout, player);
            buttons.add(button);
        }
    }

    @Override
    public void tick() {
        if (!active) {
            return;
        }
        
        for (RecipeLayout recipeLayout : recipeLayouts) {
            recipeLayout.tickWrapper(recipeLayout == hovered);
        }

        super.tick();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        if (!active) {
            return;
        }

        handleMouseScrolled(mouseX, mouseY);

        Minecraft minecraft = Minecraft.INSTANCE;

        nextRecipeCategory.render(minecraft, mouseX, mouseY);
        previousRecipeCategory.render(minecraft, mouseX, mouseY);

        nextPage.render(minecraft, mouseX, mouseY);
        previousPage.render(minecraft, mouseX, mouseY);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPushMatrix();
        GL11.glTranslatef(guiLeft, guiTop, 0.0F);

        fill(borderPadding + buttonWidth, borderPadding - 2, xSize - borderPadding - buttonWidth, borderPadding + 10, 0x30000000);
        fill(borderPadding + buttonWidth, titleHeight + textPadding - 2, xSize - borderPadding - buttonWidth, titleHeight + textPadding + 10, 0x30000000);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        StringUtil.drawCenteredString(textRenderer, title, xSize, borderPadding, Color.WHITE.getRGB(), true);
        StringUtil.drawCenteredString(textRenderer, pageString, xSize, titleHeight + textPadding, Color.WHITE.getRGB(), true);
        GL11.glPopMatrix();

        hovered = null;
        for (RecipeLayout recipeWidget : recipeLayouts) {
            if (recipeWidget.getFocusUnderMouse(mouseX, mouseY) != null) {
                hovered = recipeWidget;
            } else {
                recipeWidget.draw(minecraft, mouseX, mouseY);
            }
        }
    }

    public void drawHovered(int mouseX, int mouseY) {
        if (!active) {
            return;
        }
        
        if (hovered != null) {
            hovered.draw(minecraft, mouseX, mouseY);
        }
        
        if (titleHoverChecker.isOver(mouseX, mouseY)) {
            Focus focus = logic.getFocus();
            if (focus != null && !focus.isBlank()) {
                Tooltip.INSTANCE.setTooltip(new ArrayList<>(){{add(TranslationStorage.getInstance().get("alwaysmoreitems.tooltip.show.all.recipes"));}}, mouseX, mouseY);
            }
        }
    }

    public void drawBackground() {
        renderBackground();

        bindTexture(backgroundTexture);

        drawTexture(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    private void bindTexture(String texturePath) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId(texturePath));
    }

    protected boolean recipeKeyPressed(int keyCode) {
        if (!active) {
            return false;
        }

        ScreenScaler screenScaler = new ScreenScaler(Minecraft.INSTANCE.options, Minecraft.INSTANCE.displayWidth, Minecraft.INSTANCE.displayHeight);
        int scaledWidth = screenScaler.getScaledWidth();
        int scaledHeight = screenScaler.getScaledHeight();
        int mouseX = Mouse.getX() * scaledWidth / Minecraft.INSTANCE.displayWidth;
        int mouseY = scaledHeight - Mouse.getY() * scaledHeight / Minecraft.INSTANCE.displayHeight - 1;

        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == minecraft.options.inventoryKey.code) {
            close();
            return true;
        }

        if (hovered != null) {
            if (KeybindListener.showRecipe.code == keyCode) {
                Focus focus = hovered.getItemStacks().getFocusUnderMouse(mouseX - hovered.getPosX(), mouseY - hovered.getPosY());
                if (focus != null) {
                    showRecipes(focus);
                    return true;
                }
            }
            if (KeybindListener.showUses.code == keyCode) {
                Focus focus = hovered.getItemStacks().getFocusUnderMouse(mouseX - hovered.getPosX(), mouseY - hovered.getPosY());
                if (focus != null) {
                    showUses(focus);
                    return true;
                }
            }
        }
        return false;
    }
}
