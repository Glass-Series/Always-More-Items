package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import com.mojang.datafixers.util.Either;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.VanillaRecipeCategoryUid;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.crafting.ShapelessOreRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Formatting;

import javax.annotation.Nonnull;

public class FurnaceSmeltingCategory extends FurnaceRecipeCategory {
    @Nonnull
    private final AMIDrawable background;
    @Nonnull
    private final String localizedName;

    public FurnaceSmeltingCategory() {
        super();
        background = DrawableHelper.createDrawable("/gui/furnace.png", 55, 16, 82, 54);
        localizedName = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.smelting");
    }

    @Override
    @Nonnull
    public AMIDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void drawAnimations(Minecraft minecraft) {
        flame.draw(minecraft, 1, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public String getUid() {
        return VanillaRecipeCategoryUid.SMELTING;
    }

    @Override
    public void setRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull RecipeWrapper recipeWrapper) {
        GuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(inputSlot, true, 0, 0);
        guiItemStacks.init(outputSlot, false, 60, 18);

        guiItemStacks.setFromRecipe(inputSlot, recipeWrapper.getInputs());
        guiItemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (!input) {
                return;
            }
            if (recipeWrapper instanceof SmeltingRecipe smeltingRecipe) {
                Either<TagKey<Item>, ItemStack> ing = smeltingRecipe.getInputs().get(0).get(0);
                ing.mapLeft(tagKey -> {
                    tooltip.add(Tooltip.Divider.INSTANCE);
                    tooltip.add(Formatting.GRAY + "Takes any " + tagKey.id());
                    return tagKey;
                });
            }
        });
    }
}
