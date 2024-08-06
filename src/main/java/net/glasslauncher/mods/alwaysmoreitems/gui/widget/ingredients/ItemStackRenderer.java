package net.glasslauncher.mods.alwaysmoreitems.gui.widget.ingredients;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import org.lwjgl.opengl.GL11;

import javax.annotation.*;
import java.util.*;

public class ItemStackRenderer implements IIngredientRenderer<ItemStack> {
    private static final ItemRenderer itemRenderer = new ItemRenderer();

    @Override
    public void draw(@Nonnull Minecraft minecraft, int xPosition, int yPosition, @Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        TextRenderer font = Minecraft.INSTANCE.textRenderer;
        TextureManager texture = Minecraft.INSTANCE.textureManager;

        itemRenderer.method_1487(font, texture, itemStack, xPosition, yPosition);
        itemRenderer.method_1488(font, texture, itemStack, xPosition, yPosition);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Nonnull
    @Override
    public List<String> getTooltip(@Nonnull Minecraft minecraft, @Nonnull ItemStack itemStack) {
        String simpleTip = TranslationStorage.getInstance().get(itemStack.getTranslationKey() + ".name");
        return TooltipHelper.getTooltipForItemStack(simpleTip, itemStack, Minecraft.INSTANCE.player.inventory, null);
    }
}
