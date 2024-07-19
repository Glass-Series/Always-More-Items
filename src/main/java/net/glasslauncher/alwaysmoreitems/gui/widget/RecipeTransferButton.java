package net.glasslauncher.alwaysmoreitems.gui.widget;

import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.alwaysmoreitems.transfer.RecipeTransferUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;

public class RecipeTransferButton extends ButtonWidget {
	private static final String transferTooltip = TranslationStorage.getInstance().get("ami.tooltip.transfer");
	private RecipeLayout recipeLayout;
	private IRecipeTransferError recipeTransferError;

	public RecipeTransferButton(int id, int xPos, int yPos, int width, int height, String displayString) {
		super(id, xPos, yPos, width, height, displayString);
	}

	public void init(RecipeLayout recipeLayout, PlayerEntity player) {
		this.recipeLayout = recipeLayout;
		this.recipeTransferError = RecipeTransferUtil.getTransferRecipeError(recipeLayout, player);

		if (this.recipeTransferError == null) {
			this.active = true;
			this.visible = true;
		} else {
			this.active = false;
			IRecipeTransferError.Type type = this.recipeTransferError.getType();
			this.visible = (type == IRecipeTransferError.Type.USER_FACING);
		}
	}

	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		super.render(mc, mouseX, mouseY);
		if (isMouseOver(mc, mouseX, mouseY) && visible) {
			if (recipeTransferError != null) {
				recipeTransferError.showError(mc, mouseX, mouseY, 0, 0, recipeLayout);
			} else {
				Minecraft.INSTANCE.textRenderer.drawWithShadow(transferTooltip, mouseX, mouseY, -1);
			}
		}
	}
}
