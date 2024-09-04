package net.glasslauncher.mods.alwaysmoreitems.gui.widget;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.transfer.RecipeTransferError;
import net.glasslauncher.mods.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.transfer.RecipeTransferUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class RecipeTransferButton extends ActionButtonWidget {
	private static final String transferTooltip = TranslationStorage.getInstance().get("alwaysmoreitems.tooltip.transfer");
	private static final String transferMaxTooltip = TranslationStorage.getInstance().get("alwaysmoreitems.tooltip.transfer.max");
	private RecipeLayout recipeLayout;
	private RecipeTransferError recipeTransferError;

	public RecipeTransferButton(int id, int xPos, int yPos, int width, int height, String displayString) {
		super(id, xPos, yPos, width, height, displayString, displayString);
	}

	public void init(RecipeLayout recipeLayout, PlayerEntity player) {
		this.recipeLayout = recipeLayout;
		this.recipeTransferError = RecipeTransferUtil.getTransferRecipeError(recipeLayout, player);

		if (this.recipeTransferError == null) {
			this.active = true;
			this.visible = true;
		} else {
			this.active = false;
			RecipeTransferError.Type type = this.recipeTransferError.getType();
			this.visible = (type == RecipeTransferError.Type.PLAYER);
		}
	}

	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		super.render(mc, mouseX, mouseY);
		if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height && visible) {
			if (recipeTransferError != null) {
				recipeTransferError.showError(mc, mouseX, mouseY, 0, 0, recipeLayout);
			} else {
				Tooltip.INSTANCE.setTooltip(new ArrayList<>(){{add(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? transferMaxTooltip : transferTooltip);}}, mouseX, mouseY);
			}
		}
	}
}
