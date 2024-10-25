package net.glasslauncher.mods.alwaysmoreitems.gui;


import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Something something make modder's lives easier.
 */
public class RecipeClickableArea {
	@Nonnull
	private final List<String> recipeCategoryUids;
	int top;
	int bottom;
	int left;
	int right;

	public RecipeClickableArea(int top, int bottom, int left, int right, @Nonnull String... recipeCategoryUids) {
		this.recipeCategoryUids = Arrays.asList(recipeCategoryUids);
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}

	@Nonnull
	public List<String> getRecipeCategoryUids() {
		return recipeCategoryUids;
	}

	public boolean isHovering(int mouseX, int mouseY) {
		return mouseY >= this.top && mouseY <= this.bottom && mouseX >= this.left && mouseX <= this.right;
	}
}
