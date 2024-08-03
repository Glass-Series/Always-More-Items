package net.glasslauncher.mods.alwaysmoreitems.testmod;

import net.glasslauncher.mods.alwaysmoreitems.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.api.AMIRarity;
import net.glasslauncher.mods.alwaysmoreitems.api.IAMIRarity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

public class AMIItem extends TemplateItem implements CustomTooltipProvider, IAMIRarity {

    public AMIItem(Identifier identifier) {
        super(identifier);
        setTranslationKey(TestMod.NAMESPACE, "amitestitem");
        setHasSubtypes(true);
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip) {
        return new String[]{
                originalTooltip,
                Formatting.GRAY + AMITextRenderer.UNDERLINE + "Index " + stack.getDamage(),
                "a",
                "a",
                "a",
                "a",
                "a",
                "a",
                "a",
                "a",
                "a",
                "a"
        };
    }

    @Override
    public List<ItemStack> getSubItems() {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ItemStack(this, 1, i));
        }
        return list;
    }

    @Override
    public AMIRarity getRarity(ItemStack itemStack) {
        return AMIRarity.values()[itemStack.getDamage() % (AMIRarity.values().length)];
    }
}
