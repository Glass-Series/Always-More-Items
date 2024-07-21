package net.glasslauncher.alwaysmoreitems.gui.screen;

import net.glasslauncher.alwaysmoreitems.AMITextRenderer;
import net.glasslauncher.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.alwaysmoreitems.Focus;
import net.glasslauncher.alwaysmoreitems.api.gui.IDrawable;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.alwaysmoreitems.gui.widget.RecipeTransferButton;
import net.glasslauncher.alwaysmoreitems.transfer.RecipeTransferUtil;
import net.glasslauncher.alwaysmoreitems.util.HoverChecker;
import net.glasslauncher.alwaysmoreitems.util.RecipeGuiLogic;
import net.glasslauncher.alwaysmoreitems.util.StringUtil;
import net.minecraft.class_35;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class RecipesGui extends Screen {
    public static final RecipesGui INSTANCE = new RecipesGui();
    private static final int borderPadding = 8;
    private static final int textPadding = 5;

    private Screen parent;

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

    private ButtonWidget nextRecipeCategory;
    private ButtonWidget previousRecipeCategory;
    private ButtonWidget nextPage;
    private ButtonWidget previousPage;

	@Nullable
    private RecipeLayout hovered;

    private int guiLeft;
    private int guiTop;
    private int xSize;
    private int ySize;

    public RecipesGui() {
        init();
    }

    @Override
    public void init() {
        minecraft = Minecraft.INSTANCE;
        field_157 = new class_35(minecraft);
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

        guiLeft = (minecraft.currentScreen.width - xSize) / 2;
        guiTop = (minecraft.currentScreen.height - ySize) / 2;

        titleHeight = AMITextRenderer.FONT_HEIGHT + borderPadding;
        headerHeight = titleHeight + AMITextRenderer.FONT_HEIGHT + textPadding;

        buttonWidth = 13;

        int buttonHeight = AMITextRenderer.FONT_HEIGHT + 3;

        int rightButtonX = guiLeft + xSize - borderPadding - buttonWidth;
        int leftButtonX = guiLeft + borderPadding;

        int recipeClassButtonTop = guiTop + borderPadding - 2;
        nextRecipeCategory = new ButtonWidget(2, rightButtonX, recipeClassButtonTop, buttonWidth, buttonHeight, ">");
        previousRecipeCategory = new ButtonWidget(3, leftButtonX, recipeClassButtonTop, buttonWidth, buttonHeight, "<");

        int pageButtonTop = guiTop + titleHeight + 3;
        nextPage = new ButtonWidget(4, rightButtonX, pageButtonTop, buttonWidth, buttonHeight, ">");
        previousPage = new ButtonWidget(5, leftButtonX, pageButtonTop, buttonWidth, buttonHeight, "<");

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

    // workaround to see if a button was clicked
    private boolean guiActionPerformed = false;


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isMouseOver(mouseX, mouseY)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        guiActionPerformed = false;

        if (!guiActionPerformed) {
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
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    public boolean handleMouseScrolled(int mouseX, int mouseY) {
        if (!isMouseOver(mouseX, mouseY)) {
            return false;
        }

        int scrollDelta = Mouse.getDWheel();
        if (scrollDelta < 0) {
            logic.nextPage();
            updateLayout();
            return true;
        } else if (scrollDelta > 0) {
            logic.previousPage();
            updateLayout();
            return true;
        }
        return false;
    }

    public void open(Screen newParent) {
        parent = newParent;
        Minecraft.INSTANCE.setScreen(this);
    }

    public void close() {
        if (parent != null) {
            Minecraft.INSTANCE.setScreen(parent);
            return;
        }
        Minecraft.INSTANCE.setScreen(null);
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
                guiActionPerformed = true;
                updateLayout = false;
            }
        } else {
            updateLayout = false;
        }

        if (updateLayout) {
            updateLayout();
            guiActionPerformed = true;
        }
    }

    private void updateLayout() {
        IRecipeCategory recipeCategory = logic.getRecipeCategory();
        if (recipeCategory == null) {
            return;
        }

        IDrawable recipeBackground = recipeCategory.getBackground();

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
    public void render(int mouseX, int mouseY, float delta) {
        handleMouseScrolled(mouseX, mouseY);

        Minecraft minecraft = Minecraft.INSTANCE;

        drawBackground();

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

        drawHovered(mouseX, mouseY);
    }

    public void drawHovered(int mouseX, int mouseY) {
        if (hovered != null) {
            hovered.draw(minecraft, mouseX, mouseY);
        }
        if (titleHoverChecker.isOver(mouseX, mouseY)) {
            Focus focus = logic.getFocus();
            if (focus != null && !focus.isBlank()) {
                DrawableHelper.drawTooltip(Collections.singletonList(TranslationStorage.getInstance().get("alwaysmoreitems.tooltip.show.all.recipes")), mouseX, mouseY);
            }
        }
    }

    public void drawBackground() {
        renderBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(backgroundTexture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexture(x, y, 0, 0, xSize, ySize);
    }

    private void bindTexture(String texturePath) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId(texturePath));
    }

	@Override
	public boolean shouldPause() {
		return false;
	}

}
