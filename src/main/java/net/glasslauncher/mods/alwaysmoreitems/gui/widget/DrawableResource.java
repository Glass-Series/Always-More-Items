package net.glasslauncher.mods.alwaysmoreitems.gui.widget;

import net.glasslauncher.mods.alwaysmoreitems.gui.AMIDrawContext;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.StaticDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

import javax.annotation.*;

public class DrawableResource implements StaticDrawable {

	@Nonnull
	private final String resourceLocation;
	private final int u;
	private final int v;
	private final int width;
	private final int height;
	private final int paddingTop;
	private final int paddingBottom;
	private final int paddingLeft;
	private final int paddingRight;

	public DrawableResource(@Nonnull String resourceLocation, int u, int v, int width, int height) {
		this(resourceLocation, u, v, width, height, 0, 0, 0, 0);
	}

	public DrawableResource(@Nonnull String resourceLocation, int u, int v, int width, int height, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
		this.resourceLocation = resourceLocation;

		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;

		this.paddingTop = paddingTop;
		this.paddingBottom = paddingBottom;
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
	}

	@Override
	public int getWidth() {
		return width + paddingLeft + paddingRight;
	}

	@Override
	public int getHeight() {
		return height + paddingTop + paddingBottom;
	}

	@Override
	public void draw(@Nonnull Minecraft minecraft) {
		draw(minecraft, 0, 0);
	}

	@Override
	public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset) {
		draw(minecraft, xOffset, yOffset, 0, 0, 0, 0);
	}

	@Override
	public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId(resourceLocation));

		int x = xOffset + this.paddingLeft + maskLeft;
		int y = yOffset + this.paddingTop + maskTop;
		int u = this.u + maskLeft;
		int v = this.v + maskTop;
		int width = this.width - maskRight - maskLeft;
		int height = this.height - maskBottom - maskTop;
		AMIDrawContext.INSTANCE.drawTexture(x, y, u, v, width, height);
	}

	public void renderBackgroundTexture(int vOFfset) {
		GL11.glDisable(2896);
		GL11.glDisable(2912);
		Tessellator var2 = Tessellator.INSTANCE;
		GL11.glBindTexture(3553, Minecraft.INSTANCE.textureManager.getTextureId("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var3 = 32.0F;
		var2.startQuads();
		var2.color(4210752);
		var2.vertex(0.0, this.height, 0.0, 0.0, (float)this.height / var3 + (float)vOFfset);
		var2.vertex(this.width, this.height, 0.0, (float)this.width / var3, (float)this.height / var3 + (float)vOFfset);
		var2.vertex(this.width, 0.0, 0.0, (float)this.width / var3, vOFfset);
		var2.vertex(0.0, 0.0, 0.0, 0.0, vOFfset);
		var2.draw();
	}
}
