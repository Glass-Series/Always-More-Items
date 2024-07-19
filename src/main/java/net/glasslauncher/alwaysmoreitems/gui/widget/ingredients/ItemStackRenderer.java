package net.glasslauncher.alwaysmoreitems.gui.widget.ingredients;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
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

		itemRenderer.method_1487(Minecraft.INSTANCE.textRenderer, Minecraft.INSTANCE.textureManager, itemStack, xPosition, yPosition);
		itemRenderer.method_1488(Minecraft.INSTANCE.textRenderer, Minecraft.INSTANCE.textureManager, itemStack, xPosition, yPosition);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Nonnull
	@Override
	public List<String> getTooltip(@Nonnull Minecraft minecraft, @Nonnull ItemStack itemStack) {
		String tooltip = itemStack.getItem().getTranslatedName();
		List<String> list = null;
		if (itemStack.getItem() instanceof CustomTooltipProvider tooltipProvider) {
			list = List.of(tooltipProvider.getTooltip(itemStack, tooltip));
		}
		if(list == null) {
			list = Collections.singletonList(tooltip);
		}
//		for (int k = 0; k < list.size(); ++k) {
//			if (k == 0) {
//				list.set(k, itemStack.getRarity().rarityColor + list.get(k));
//			} else {
//				list.set(k, Formatting.GRAY + list.get(k));
//			}
//		}

		return list;
	}
}
