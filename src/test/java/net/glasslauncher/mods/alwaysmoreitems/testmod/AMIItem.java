package net.glasslauncher.mods.alwaysmoreitems.testmod;

import net.glasslauncher.alwaysmoreitems.AMITextRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

public class AMIItem extends TemplateItem implements CustomTooltipProvider {

    public AMIItem(Identifier identifier) {
        super(identifier);
        setTranslationKey("amitestitem");
        setHasSubtypes(true);
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip) {
        return new String[]{
                Formatting.DARK_AQUA + originalTooltip,
                Formatting.GRAY + AMITextRenderer.UNDERLINE + "Index " + stack.getDamage()
        };
    }

    @Override
    public List<ItemStack> getSubItems() {
        return List.of(
                new ItemStack(this, 1, 0),
                new ItemStack(this, 1, 2),
                new ItemStack(this, 1, 3),
                new ItemStack(this, 1, 4)
        );
    }
}
