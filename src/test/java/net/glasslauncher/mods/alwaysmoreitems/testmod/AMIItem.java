package net.glasslauncher.mods.alwaysmoreitems.testmod;

import net.glasslauncher.mods.alwaysmoreitems.api.SubItemProvider;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.api.Rarity;
import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

public class AMIItem extends TemplateItem implements CustomTooltipProvider, RarityProvider {

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

    @SubItemProvider
    public List<ItemStack> getSubItems() {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ItemStack(this, 1, i));
        }
        return list;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.values()[itemStack.getDamage() % (Rarity.values().length)];
    }
}
