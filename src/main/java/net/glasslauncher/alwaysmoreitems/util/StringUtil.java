package net.glasslauncher.alwaysmoreitems.util;

import net.minecraft.client.font.TextRenderer;

public class StringUtil {
	private StringUtil() {

	}

	public static void drawCenteredString(TextRenderer fontRenderer, String string, int guiWidth, int yPos, int color, boolean shadow) {
		fontRenderer.draw(string, (guiWidth - fontRenderer.getWidth(string)) / 2, yPos, color, shadow);
	}

}
