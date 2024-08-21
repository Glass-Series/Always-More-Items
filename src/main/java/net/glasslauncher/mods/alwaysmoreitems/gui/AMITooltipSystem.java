package net.glasslauncher.mods.alwaysmoreitems.gui;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.mine_diver.spasm.impl.util.TriFunction;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Formatting;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class AMITooltipSystem {
    public static final String AMI_TOOLTIP_PHASE = "always_more_items:tooltip_phase";

    private static final Multimap<Integer, TriFunction<Screen, Integer, Integer, TooltipInstance>> map = MultimapBuilder.treeKeys().arrayListValues().build();
    private static TooltipInstance mainTip;

    public static void queueTooltip(ItemStack itemStack) {
        map.put(0, ((screen, mouseX, mouseY) -> new TooltipInstance(TranslationStorage.getInstance().get(itemStack.getTranslationKey() + ".name"), itemStack, mouseX, mouseY)));
    }

    public static void queueTooltip(List<String> tooltip) {
        map.put(0, ((screen, mouseX, mouseY) -> new TooltipInstance(new ArrayList<>(tooltip), mouseX, mouseY)));
    }

    public static void queueTooltip(TriFunction<Screen, Integer, Integer, TooltipInstance> tooltipProvider, int priority) {
        map.put(priority, tooltipProvider);
    }

    public static void clearTooltip() {
        map.clear();
    }

    public static TriFunction<Screen, Integer, Integer, TooltipInstance> getMainTip() {
        Set<Integer> keySet = AMITooltipSystem.map.keySet();
        if (keySet.isEmpty()) {
            return null;
        }
        //noinspection unchecked
        return AMITooltipSystem.map.get((Integer) keySet.toArray()[keySet.size() - 1]).toArray(TriFunction[]::new)[0];
    }

    @EventListener(phase = AMI_TOOLTIP_PHASE)
    private static void yourTooltipsAreNowMine(TooltipRenderEvent event) {
        if (event.isCanceled()) {
            return;
        }

        TooltipInstance tooltip = ((HijackedStapiTip) event).alwaysMoreItems$getTooltip();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.disableLighting();
        tooltip.render();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        event.cancel();
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    private static void yourTooltipsAreNowModified(TooltipBuildEvent event) {
        if(event.tooltip.isEmpty()) {
            return;
        }

        if (event.itemStack.getItem() instanceof RarityProvider rarityProvider) {
            event.tooltip.set(0, rarityProvider.getRarity(event.itemStack) + event.tooltip.get(0));
        }

        if (AMIConfig.isDebugModeEnabled()) {
            String extras = "";
            if (event.itemStack.getDamage() != 0) {
                extras += ":" + event.itemStack.getDamage();
            }
            event.tooltip.set(0, event.tooltip.get(0) + " " + event.itemStack.itemId + extras);

            event.add(Formatting.GRAY + AMITextRenderer.ITALICS + ItemRegistry.INSTANCE.getId(event.itemStack.getItem()));
        }

        if (AMIConfig.showNbtCount()) {
            event.add(Formatting.GRAY + AMITextRenderer.ITALICS + "NBT Count: " + event.itemStack.getStationNbt().values().size());
        }
        if (AMIConfig.showModNames()) {
            event.add(Formatting.BLUE + AMITextRenderer.ITALICS + AlwaysMoreItems.getItemRegistry().getModNameForItem(event.itemStack.getItem()));
        }
    }
}
