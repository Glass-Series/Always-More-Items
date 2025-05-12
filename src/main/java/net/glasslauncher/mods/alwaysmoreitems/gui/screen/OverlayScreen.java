package net.glasslauncher.mods.alwaysmoreitems.gui.screen;

import com.google.common.collect.ImmutableList;
import net.glasslauncher.mods.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.mods.alwaysmoreitems.api.action.ActionButton;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.config.OverlayMode;
import net.glasslauncher.mods.alwaysmoreitems.gui.RenderHelper;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.AMISettingsButton;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.ActionButtonWidget;
import net.glasslauncher.mods.alwaysmoreitems.gui.widget.SearchTextFieldWidget;
import net.glasslauncher.mods.alwaysmoreitems.init.KeybindListener;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.ActionButtonPacket;
import net.glasslauncher.mods.alwaysmoreitems.network.c2s.GiveItemPacket;
import net.glasslauncher.mods.alwaysmoreitems.recipe.Focus;
import net.glasslauncher.mods.alwaysmoreitems.recipe.ItemFilter;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.Commands;
import net.glasslauncher.mods.alwaysmoreitems.util.ItemStackElement;
import net.glasslauncher.mods.gcapi3.api.GCAPI;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.DoubleChestScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.management.AttributeNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverlayScreen extends Screen {
    public static final OverlayScreen INSTANCE = new OverlayScreen();

    // Parent Screen
    public Screen parent;

    public final RecipesGui recipesGui = new RecipesGui();

    // Search Field
    public SearchTextFieldWidget searchField;
    public static int searchFieldWidth = 160;

    // Trash Button
    public ActionButtonWidget trashButton;

    // Action Buttons
    ArrayList<ActionButtonWidget> actionButtons;
    public static int maxActionButtonPanelWidth = 100;
    public static int actionButtonOffset = 2;

    // Tooltip
    public List<String> currentTooltip;

    // Item Overlay
    public static int maxItemListWidth = 200;
    public static int maxItemListHeight = 400;
    public static int itemSize = 18;
    ArrayList<ItemRenderEntry> renderedItems;
    public ButtonWidget nextButton;
    public ButtonWidget previousButton;
    public ButtonWidget settingsButton;
    int currentPage = 0;
    int pageCount = 1;
    boolean rolloverPage = true;
    public boolean flipScrollDirection = false;
    public ItemRenderEntry hoveredItem = null;

    // Screen Rescaling Stuff
    private int lastWidth = 0;
    private int lastHeight = 0;
    private int lastItemWidth = 0;
    private int lastItemHeight = 0;
    private int lastScaledWidth = 0;
    private boolean lastCenteredBar = true;

    // GUI Size Refresh
    private int lastItemListWidth = 0;
    private int lastItemListHeight = 0;
    private int lastParent = 0;

    // Properly refresh buttons
    private OverlayMode lastOverlayMode = OverlayMode.RECIPE;

    // Mouse Pos
    int lastMouseX = 0;
    int lastMouseY = 0;

    ScreenScaler screenScaler;

    private OverlayScreen() {
    }

    public void init(Screen parent, int width, int height) {
        this.parent = parent;
        init(Minecraft.INSTANCE, width, height);
    }

    /**
     * Use init(Screen, int, int) instead.
     *
     * @see #init(Screen, int, int)
     */
    @ApiStatus.Internal
    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    int id;

    @SuppressWarnings({"unchecked"})
    @ApiStatus.Internal
    @Override
    public void init() {
        id = 100;

        buttons.clear();

        // Tooltip
        currentTooltip = null;

        // Search Field
        searchField = new SearchTextFieldWidget(textRenderer, 0, 0, 0, 20);
        searchField.setMaxLength(64);
        searchField.setText(AlwaysMoreItems.getItemFilter().getFilterText());

        // Settings Button
        settingsButton = new AMISettingsButton(12, 0, 0);
        settingsButton.visible = AlwaysMoreItems.overlayEnabled;
        buttons.add(settingsButton);

        // Adjust search field and settings button
        if (isSearchBarCentered()) {
            searchField.x = (width / 2) - (searchFieldWidth / 2) - 10;
            searchField.y = height - 25;
            searchField.width = searchFieldWidth - 2;

            settingsButton.x = (width / 2) - (searchFieldWidth / 2) + searchFieldWidth - 11;
            settingsButton.y = height - 26;
        } else {
            searchField.x = width - (getItemListWidth() * 18);
            searchField.y = height - 21;
            searchField.width = width - 23 - searchField.x;

            settingsButton.x = width - 22;
            settingsButton.y = height - 22;
        }

        // Item Overlay
        previousButton = new ButtonWidget(10, getOverlayStartX() - 1, 0, 20, 20, "<");
        previousButton.visible = AlwaysMoreItems.overlayEnabled;
        buttons.add(previousButton);

        nextButton = new ButtonWidget(11, width - 20, 0, 20, 20, ">");
        nextButton.visible = AlwaysMoreItems.overlayEnabled;
        buttons.add(nextButton);

        // Action Buttons
        initActionButtons();

        recipesGui.init();
    }

    public void initActionButtons() {
        actionButtons = new ArrayList<>();
        int actionButtonX = 0;
        int actionButtonY = 0;
        int maxHeightForLine = 0;

        for (int i = 0; i < ActionButtonRegistry.INSTANCE.size(); i++) {
            Identifier identifier = ActionButtonRegistry.INSTANCE.getId(i).get();
            ActionButton actionButton = ActionButtonRegistry.INSTANCE.get(identifier);
            if (actionButton == null) {
                AlwaysMoreItems.LOGGER.error("Identifier {} somehow returned null", identifier, new Throwable());
                continue;
            }

            if (actionButton.dontAddToScreen()) {
                continue;
            }

            List<OverlayMode> allowedOverlayModes = actionButton.allowedOverlayModes();
            if (allowedOverlayModes != null && !allowedOverlayModes.contains(AMIConfig.getOverlayMode())) {
                continue;
            }

            if (actionButtonX + actionButton.getWidth() > maxActionButtonPanelWidth) {
                actionButtonX = 0;
                actionButtonY += maxHeightForLine + actionButtonOffset;
                maxHeightForLine = 0;
            }

            if (actionButton.getHeight() > maxHeightForLine) {
                maxHeightForLine = actionButton.getHeight();
            }

            ActionButtonWidget widget = new ActionButtonWidget(id++, actionButtonX, actionButtonY, actionButton.getWidth(), actionButton.getHeight(), actionButton.getTexture());
            actionButtons.add(widget);
            widget.action = actionButton;
            widget.actionIdentifier = identifier;

            actionButtonX += actionButton.getWidth() + actionButtonOffset;
        }

        // Trash Button
        trashButton = new ActionButtonWidget(id + 1, 0, height - 20, 90, 20, "button." + AlwaysMoreItems.NAMESPACE + ".trash", "button." + AlwaysMoreItems.NAMESPACE + ".trash.alt");
        trashButton.actionIdentifier = AlwaysMoreItems.NAMESPACE.id("trash");
        trashButton.action = ActionButtonRegistry.INSTANCE.get(trashButton.actionIdentifier);

        if (trashButton.action != null) {
            List<OverlayMode> allowedOverlayModes = trashButton.action.allowedOverlayModes();
            if (allowedOverlayModes == null || allowedOverlayModes.contains(AMIConfig.getOverlayMode())) {
                actionButtons.add(trashButton);
            }
        }
    }

    @Override
    public void tick() {
        // Do not tick if not enabled
        if (AlwaysMoreItems.overlayEnabled) {
            rescale();

            if (AMIConfig.getOverlayMode() != lastOverlayMode) {
                lastOverlayMode = AMIConfig.getOverlayMode();
                initActionButtons();
            }
        }
        recipesGui.tick();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        if (parent.hashCode() != lastParent)
        {
            checkGuiSize();
            lastParent = parent.hashCode();
        }

        if (recipesGui.isActive()) {
            recipesGui.drawBackground();
        }
        super.render(mouseX, mouseY, delta);

        recipesGui.render(mouseX, mouseY, delta);
        amiRender(mouseX, mouseY);
        recipesGui.drawHovered(mouseX, mouseY);
    }

    public void amiRender(int mouseX, int mouseY) {
        // Do not render if not enabled
        if (!AlwaysMoreItems.overlayEnabled) {
            return;
        }

        // Reset Tooltip
        currentTooltip = null;

        // Draw Items
        if (renderedItems != null) {
            RenderHelper.enableItemLighting();
            for (ItemRenderEntry item : renderedItems) {
                if (AMIConfig.isEditModeEnabled()) {
                    if (AMIConfig.isItemOnConfigBlacklist(item.item, false)) { // This exact item is user blacklisted.
                        RenderHelper.disableItemLighting();
                        fill(item.x - 1, item.y - 1, item.x + 17, item.y + 17, ColorHelper.Argb.getArgb(255, 255, 0, 0));
                        RenderHelper.enableItemLighting();
                    } else if (AMIConfig.isItemOnConfigBlacklist(item.item, true)) { // This item's ID is user blacklisted.
                        RenderHelper.disableItemLighting();
                        fill(item.x - 1, item.y - 1, item.x + 17, item.y + 17, ColorHelper.Argb.getArgb(255, 255, 255, 0));
                        RenderHelper.enableItemLighting();
                    }
                }
                RenderHelper.drawItemStack(item.x, item.y, item.item, true);
            }
            RenderHelper.disableItemLighting();
        }

        // Draw Slot Highlight
        hoveredItem = getHoveredItem(mouseX, mouseY);
        if (hoveredItem != null) {
            this.fill(hoveredItem.x - 1, hoveredItem.y - 1, hoveredItem.x + itemSize - 1, hoveredItem.y + itemSize - 1, -2130706433);
            String simpleTip = TranslationStorage.getInstance().get(hoveredItem.item.getTranslationKey() + ".name");
            currentTooltip = TooltipHelper.getTooltipForItemStack(simpleTip, hoveredItem.item, Minecraft.INSTANCE.player.inventory, null);
        }

        // Draw Page Number
        String pageNumberString = (pageCount == 0 ? 0 : (currentPage + 1)) + "/" + pageCount;
        textRenderer.drawWithShadow(
                pageNumberString,
                (width - ((width - getOverlayStartX()) / 2)) - (textRenderer.getWidth(pageNumberString) / 2),
                6,
                -1
        );

        // Draw Search Field and Settings Button
        searchField.draw(mouseX, mouseY);

        // Draw Action Buttons
        for (var actionButton : actionButtons) {
            actionButton.render(minecraft, mouseX, mouseY);

            if (!actionButton.action.tooltipEnabled()) {
                continue;
            }

            if (currentTooltip == null && actionButton.isMouseOver(minecraft, mouseX, mouseY)) {
                boolean holdingShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                String translationKey = AlwaysMoreItems.NAMESPACE + ".actionButton." + actionButton.actionIdentifier.namespace + "." + actionButton.actionIdentifier.path;

                if (holdingShift && !TranslationStorage.getInstance().get(translationKey + ".alt").equals(translationKey + ".alt")) {
                    currentTooltip = Collections.singletonList(TranslationStorage.getInstance().get(translationKey + ".alt"));
                } else {
                    currentTooltip = Collections.singletonList(TranslationStorage.getInstance().get(translationKey));
                }
            }
        }

        if (Minecraft.INSTANCE.player.inventory.getCursorStack() != null && mouseX >= getOverlayStartX() && mouseY > 21) {
            currentTooltip = new ArrayList<>() {{
                add(TranslationStorage.getInstance().get("button.alwaysmoreitems.trash.cursor", TranslationStorage.getInstance().get(Minecraft.INSTANCE.player.inventory.getCursorStack().getTranslationKey() + ".name")));
            }};
        }

        // Queue Tooltip
        if (currentTooltip != null && !currentTooltip.isEmpty()) {
            Tooltip.INSTANCE.setTooltip(new ArrayList<>(currentTooltip), mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        // Pass thru clicks to recipes gui
        recipesGui.mouseClicked(mouseX, mouseY, button);

        // Do not process if not enabled
        if (!AlwaysMoreItems.overlayEnabled) {
            return;
        }

        super.mouseClicked(mouseX, mouseY, button);

        if (Minecraft.INSTANCE.player.inventory.getCursorStack() != null && mouseX >= getOverlayStartX() && mouseY > 21) {
            if (!minecraft.world.isRemote) {
                trashButton.performAction(minecraft, minecraft.world, minecraft.player, true, button, false);
            } else {
                PacketHelper.send(new ActionButtonPacket(trashButton.actionIdentifier, button, false));
            }
            return;
        }

        // Search Field
        searchField.mouseClicked(mouseX, mouseY, button);
        if (ItemFilter.setFilterText(searchField.getText())) {
            rebuildRenderList();
        }

        // Action Buttons
        for (var actionButton : actionButtons) {
            if (actionButton.isMouseOver(minecraft, mouseX, mouseY)) {
                this.minecraft.soundManager.playSound(actionButton.action.getClickSound(), 1.0F, 1.0F);
                boolean holdingShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                if (!minecraft.world.isRemote || actionButton.action.isClientsideOnly()) {
                    actionButton.performAction(minecraft, minecraft.world, minecraft.player, true, button, holdingShift);
                } else {
                    PacketHelper.send(new ActionButtonPacket(actionButton.actionIdentifier, button, holdingShift));
                }
            }
        }

        // Hovered Item
        if (hoveredItem != null) {
            if (AMIConfig.isEditModeEnabled() && button == 0) {
                boolean isCtrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
                if (!AMIConfig.isItemOnConfigBlacklist(hoveredItem.item, isCtrl)) {
                    AMIConfig.addItemToConfigBlacklist(hoveredItem.item, isCtrl);
                } else {
                    AMIConfig.removeItemFromConfigBlacklist(hoveredItem.item, isCtrl);
                }
                return;
            }

            if (!(AMIConfig.getOverlayMode() == OverlayMode.CHEAT) || recipesGui.isActive()) {
                if (button == 0) { // LMB - Show Recipe
                    showRecipe(new Focus(hoveredItem.item));
                } else if (button == 1) { // RMB - Show Uses
                    showUses(new Focus(hoveredItem.item));
                }
                return;
            }

            if (AlwaysMoreItems.isAMIOnServer()) {
                NbtCompound itemNbt = new NbtCompound();
                hoveredItem.item.writeNbt(itemNbt);

                if (button == 0) { // LMB - Give Stack
                    itemNbt.putByte("Count", (byte) hoveredItem.item.getMaxCount());
                } else if (button == 1) { // RMB - Give One
                    itemNbt.putByte("Count", (byte) Math.min(AMIConfig.getRightClickGiveAmount(), hoveredItem.item.getMaxCount()));
                } else {
                    return; // Some other mouse button was pressed
                }

                PacketHelper.send(new GiveItemPacket(itemNbt));
            } else {
                if (button == 0) { // LMB - Give Stack
                    Commands.giveStack(hoveredItem.item, hoveredItem.item.getMaxCount());
                } else if (button == 1) { // RMB - Give One
                    Commands.giveStack(hoveredItem.item, Math.min(AMIConfig.getRightClickGiveAmount(), hoveredItem.item.getMaxCount()));
                }
            }
        }
    }

    @Override
    public void onMouseEvent() {
        // Do not process if not enabled
        if (!AlwaysMoreItems.overlayEnabled) {
            return;
        }

        super.onMouseEvent();

        int mouseX = Mouse.getEventX() * this.width / this.minecraft.displayWidth;
        int dWheel = Mouse.getEventDWheel();
        if (mouseX >= getOverlayStartX() && dWheel != 0) {
            if (flipScrollDirection) {
                flipPage(dWheel);
            } else {
                flipPage(-dWheel);
            }
        }
    }

    @Override
    public void keyPressed(char character, int keyCode) {
        if (OverlayScreen.INSTANCE.overlayKeyPressed(character, keyCode)) {
            return;
        }
        super.keyPressed(character, keyCode);
    }

    public boolean overlayKeyPressed(char character, int keyCode) {
        // Toggle Overlay
        if (keyCode == KeybindListener.toggleOverlay.code && !searchField.isSelected()) {
            AlwaysMoreItems.overlayEnabled = !AlwaysMoreItems.overlayEnabled;

            screenScaler = new ScreenScaler(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
            minecraft.currentScreen.init(minecraft, screenScaler.getScaledWidth(), screenScaler.getScaledHeight());

            previousButton.visible = previousButton.active = AlwaysMoreItems.overlayEnabled;
            nextButton.visible = nextButton.active = AlwaysMoreItems.overlayEnabled;
            settingsButton.visible = settingsButton.active = AlwaysMoreItems.overlayEnabled;
        }

        if (recipesGui.recipeKeyPressed(keyCode)) {
            return true;
        }

        // Cancel keys if overlay is not enabled
        if (!AlwaysMoreItems.overlayEnabled) {
            return false;
        }

        // Search Field
        if (searchField.isSelected()) {
            searchField.keyPressed(character, keyCode);
            ItemFilter.setFilterText(searchField.getText());
            currentPage = 0; //Math.min(pageCount - 1, currentPage);
            rebuildRenderList();
            return true;
        }

        // Item Actions
        // Check items from item menu
        if (hoveredItem != null) {
            // Show Recipes
            if (keyCode == KeybindListener.showRecipe.code) {
                showRecipe(new Focus(hoveredItem.item));
                return true;
            }

            // Show Uses
            if (keyCode == KeybindListener.showUses.code) {
                showUses(new Focus(hoveredItem.item));
                return true;
            }
        }

        // Check Inventory Items
        if (parent instanceof HandledScreen handled) {
            Slot hoveredSlot = handled.getSlotAt(lastMouseX, lastMouseY);

            if (hoveredSlot != null && hoveredSlot.hasStack()) {
                // Show Recipes
                if (keyCode == KeybindListener.showRecipe.code) {
                    recipesGui.showRecipes(new Focus(hoveredSlot.getStack()));
                    return true;
                }

                // Show Uses
                else if (keyCode == KeybindListener.showUses.code) {
                    recipesGui.showUses(new Focus(hoveredSlot.getStack()));
                    return true;
                }
            }
        }

        // Go Back
        if (keyCode == KeybindListener.recipeBack.code) {
            recipesGui.back();
            return true;
        }

        return false;
    }

    public void showRecipe(Focus item) {
        recipesGui.showRecipes(item);
    }

    public void showUses(Focus item) {
        recipesGui.showUses(item);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        recipesGui.buttonClicked(button);

        if (button.id == previousButton.id) {
            flipPage(-1);
        }

        if (button.id == nextButton.id) {
            flipPage(1);
        }

        if (button.id == settingsButton.id) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                GCAPI.reloadConfig(AlwaysMoreItems.NAMESPACE.id("config").toString(), new GlassYamlFile() {{
                    set("overlayMode", AMIConfig.getOverlayMode() != OverlayMode.CHEAT ? 1 : 0);
                }});
                initActionButtons();
                return;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                GCAPI.reloadConfig(AlwaysMoreItems.NAMESPACE.id("config").toString(), new GlassYamlFile() {{
                    set("overlayMode", AMIConfig.getOverlayMode() != OverlayMode.UTILITY ? 2 : 0);
                }});
                initActionButtons();
                return;
            }

            try {
                Minecraft.INSTANCE.setScreen(GCAPI.getRootConfigScreen(AlwaysMoreItems.NAMESPACE.id("config").toString(), parent));
            } catch (AttributeNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if the display dimensions have changed since the last time the method was ran and if so,
     * recalculates the scaled width and height
     */
    public void rescale() {
        if (minecraft == null) {
            minecraft = Minecraft.INSTANCE;
        }

        screenScaler = new ScreenScaler(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);

        if (minecraft.displayWidth != lastWidth || minecraft.displayHeight != lastHeight || AMIConfig.INSTANCE.maxItemListWidth != lastItemWidth || AMIConfig.INSTANCE.maxItemListHeight != lastItemHeight || screenScaler.getScaledWidth() != lastScaledWidth || isSearchBarCentered() != lastCenteredBar) {
            // Get the item list dimensions
            maxItemListWidth = AMIConfig.INSTANCE.maxItemListWidth;
            maxItemListHeight = AMIConfig.INSTANCE.maxItemListHeight;

            // Run screen scaler
            width = screenScaler.getScaledWidth();
            height = screenScaler.getScaledHeight();

            // Store Last values
            lastWidth = minecraft.displayWidth;
            lastHeight = minecraft.displayHeight;
            lastItemWidth = maxItemListWidth;
            lastItemHeight = maxItemListHeight;
            lastScaledWidth = width;
            lastCenteredBar = isSearchBarCentered();

            // Rebuild the screen
            currentPage = 0;
            minecraft.currentScreen.init(minecraft, screenScaler.getScaledWidth(), screenScaler.getScaledHeight());
            rebuildRenderList();
        }
    }

    /*
     * Checks to see if the current GUI size is different
     * than the cached width, if it is, rebuild the cache
     */
    private void checkGuiSize()
    {
        // Get the current item width/height
        int itemListWidth = getItemListWidth();
        int itemListHeight = getItemListHeight();

        // Compare with the cached item width/height
        if (itemListWidth != lastItemListWidth || itemListHeight != lastItemListHeight)
        {
            // Rebuild the screen
            currentPage = 0;
            minecraft.currentScreen.init(minecraft, screenScaler.getScaledWidth(), screenScaler.getScaledHeight());
            rebuildRenderList();
        }
    }

    public int getItemListWidth() {
        int possibleOverlayStartX;
        if (parent instanceof HandledScreen handledScreen) {
            possibleOverlayStartX = ((parent.width - handledScreen.backgroundWidth) / 2) + handledScreen.backgroundWidth + 10;
        } else if (parent instanceof RecipesGui recipesGuiParent) {
            possibleOverlayStartX = ((parent.width - recipesGuiParent.getXSize()) / 2) + recipesGuiParent.getXSize() + 10;
        } else {
            throw new RuntimeException("Unsupported Screen fed to OverlayScreen!");
        }
        return Math.min(((width - possibleOverlayStartX) / itemSize), maxItemListWidth);
    }

    public int getItemListHeight() {
        if (isSearchBarCentered()) {
            return Math.min(((height - 20) / itemSize), maxItemListHeight);
        } else {
            return Math.min(((height - 42) / itemSize), maxItemListHeight);
        }
    }

    public boolean isSearchBarCentered() {
        if (screenScaler == null) {
            screenScaler = new ScreenScaler(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
        }

        return AMIConfig.centeredSearchBar() && !(screenScaler.getScaledHeight() <= 275 && minecraft.currentScreen instanceof DoubleChestScreen);
    }

    public int getOverlayStartX() {
        return width - (getItemListWidth() * itemSize);
    }

    public ItemRenderEntry getHoveredItem(int mouseX, int mouseY) {
        if (renderedItems == null || !AlwaysMoreItems.overlayEnabled) {
            return null;
        }

        if (mouseY <= 21 || mouseX < getOverlayStartX()) {
            return null;
        }

        int itemX = (mouseX - getOverlayStartX()) / itemSize;
        int itemY = (mouseY - 21) / itemSize;
        int itemIndexOnPage = (itemY * getItemListWidth()) + itemX;

        if (itemIndexOnPage < renderedItems.size() && itemIndexOnPage >= 0) {
            return renderedItems.get(itemIndexOnPage);
        } else {
            return null;
        }
    }

    // Rebuild the list of items that are rendered
    public void rebuildRenderList() {
        // Filtered
        ImmutableList<ItemStackElement> filteredItems = AlwaysMoreItems.getItemFilter().getItemList();

        // Rendered
        renderedItems = new ArrayList<>();

        int itemListWidth = getItemListWidth();
        int itemListHeight = getItemListHeight();
        int overlayStartX = getOverlayStartX();
        int itemsPerPage = itemListWidth * itemListHeight;
        pageCount = (int) Math.ceil((double) filteredItems.size() / itemsPerPage);

        lastItemListWidth = itemListWidth;
        lastItemListHeight = itemListHeight;

        for (int yIndex = 0; yIndex < itemListHeight; yIndex++) {
            for (int xIndex = 0; xIndex < itemListWidth; xIndex++) {
                int itemIndexOnThisPage = (yIndex * itemListWidth) + xIndex;
                int itemIndex = (currentPage * itemsPerPage) + itemIndexOnThisPage;

                if (itemIndex >= filteredItems.size()) {
                    return;
                }

                renderedItems.add(
                        new ItemRenderEntry(
                                overlayStartX + (xIndex * itemSize),
                                21 + (yIndex * itemSize),
                                filteredItems.get(itemIndex).getItemStack()
                        )
                );
            }
        }
    }

    public void flipPage(int direction) {
        if (direction > 0) {
            if (currentPage < pageCount - 1) {
                currentPage++;
            } else {
                if (rolloverPage) {
                    currentPage = 0;
                }
            }
        } else if (direction < 0) {
            if (currentPage > 0) {
                currentPage--;
            } else {
                if (rolloverPage) {
                    currentPage = Math.max(pageCount - 1, 0);
                }
            }
        }
        rebuildRenderList();
    }

    public record ItemRenderEntry(int x, int y, ItemStack item) {
    }
}
