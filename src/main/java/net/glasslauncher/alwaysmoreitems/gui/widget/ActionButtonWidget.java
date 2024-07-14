package net.glasslauncher.alwaysmoreitems.gui.widget;

import net.glasslauncher.alwaysmoreitems.RenderHelper;
import net.glasslauncher.alwaysmoreitems.api.action.ActionButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class ActionButtonWidget extends ButtonWidget {
    ButtonIconType iconType;
    ItemStack item;
    String texture;
    public ActionButton action;
    public Identifier actionIdentifier;

    protected ActionButtonWidget(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height, "");
    }

    public ActionButtonWidget(int id, int x, int y, int width, int height, String texture) {
        this(id, x, y, width, height);
        this.texture = texture;
        this.iconType = ButtonIconType.TEXTURE;
    }

    public ActionButtonWidget(int id, int x, int y, int width, int height, ItemStack item) {
        this(id, x, y, width, height);
        this.item = item;
        this.iconType = ButtonIconType.ITEM;
    }

    public ActionButtonWidget(int id, int x, int y, int width, int height, String text, String a) {
        this(id, x, y, width, height);
        this.text = text;
        this.iconType = ButtonIconType.TEXT;
    }

    public void performAction(Minecraft minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift) {
        action.perform(minecraft, world, player, isOperator, mouseButton, holdingShift);
    }

    @Override
    public void render(Minecraft minecraft, int mouseX, int mouseY) {
        if (!visible) {
            return;
        }

        TextRenderer fontrenderer = minecraft.textRenderer;
        RenderHelper.bindTexture(minecraft, "/gui/gui.png");
        boolean isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        int k = method_1187(isHovered);

        drawTexture(x, y, 0, 46 + k * 20, width / 2, height);
        drawTexture(x + width / 2, y, 200 - width / 2, 46 + k * 20, width / 2, height / 2);
        drawTexture(x, y + height / 2, 0, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);
        drawTexture(x + width / 2, y + height / 2, 200 - width / 2, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);

        switch (iconType) {
            case ITEM -> {
                RenderHelper.drawItemStack(x + 2, y + 2, item, false);
            }
            case TEXTURE -> {
                RenderHelper.bindTexture(minecraft, texture);
                RenderHelper.drawTexture(x + 2, y + 2, 16, 16);
            }
            case TEXT -> {
                if (!active) {
                    drawCenteredTextWithShadow(fontrenderer, text, x + width / 2, y + (height - 8) / 2, 0xffa0a0a0);
                } else if (isHovered) {
                    drawCenteredTextWithShadow(fontrenderer, text, x + width / 2, y + (height - 8) / 2, 0xffffa0);
                } else {
                    drawCenteredTextWithShadow(fontrenderer, text, x + width / 2, y + (height - 8) / 2, 0xe0e0e0);
                }
            }
        }

        method_1188(minecraft, mouseX, mouseY); // postRender
    }

    public enum ButtonIconType {
        ITEM,
        TEXTURE,
        TEXT
    }
}
