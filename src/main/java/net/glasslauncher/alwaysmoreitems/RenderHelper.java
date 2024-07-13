package net.glasslauncher.alwaysmoreitems;

import net.minecraft.class_583;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
    public static ItemRenderer itemRenderer = new ItemRenderer();

    public static void bindTexture(Minecraft minecraft, String texturePath) {
        minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId(texturePath));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTexture(int x, int y, int width, int height){
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        tessellator.vertex(x, y + height, 0, 0, 1);
        tessellator.vertex(x + width, y + height, 0, 1, 1);
        tessellator.vertex(x + width, y, 0, 1, 0);
        tessellator.vertex(x, y, 0, 0, 0);
        tessellator.draw();
    }

    public static void drawItemStack(int x, int y, ItemStack item, boolean drawOverlay) {
        enableItemLighting();
        itemRenderer.method_1487(Minecraft.INSTANCE.textRenderer, Minecraft.INSTANCE.textureManager, item, x, y);
        if (drawOverlay) {
            itemRenderer.method_1488(Minecraft.INSTANCE.textRenderer, Minecraft.INSTANCE.textureManager, item, x, y);
        }
    }

    private static void enableItemLighting() {
        enableLighting();
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPushMatrix();
        GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
        class_583.method_1930();
        GL11.glPopMatrix();
    }

    private static void enableLighting() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }
}
