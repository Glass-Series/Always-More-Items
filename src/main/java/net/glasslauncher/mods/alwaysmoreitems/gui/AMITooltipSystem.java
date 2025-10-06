package net.glasslauncher.mods.alwaysmoreitems.gui;

import net.glasslauncher.mods.alwaysmoreitems.api.RarityProvider;
import net.glasslauncher.mods.alwaysmoreitems.config.AMIConfig;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Formatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class AMITooltipSystem {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    public static final String AMI_TOOLTIP_PHASE = "always_more_items:tooltip_phase";

    @EventListener(phase = AMI_TOOLTIP_PHASE)
    private static void yourTooltipsAreNowMine(TooltipRenderEvent event) {
        if (event.isCanceled()) {
            return;
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.disableLighting();
        Tooltip.INSTANCE.render();

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

            List<TagKey<Item>> tags = event.itemStack.getItem().getRegistryEntry().streamTags().toList();
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !tags.isEmpty()) {
                List tooltip = event.tooltip;
                tooltip.add(Tooltip.Divider.INSTANCE);

                for (TagKey<Item> tag : tags) {
                    event.add(Formatting.GRAY + AMITextRenderer.ITALICS + tag.id());
                }

                tooltip.add(Tooltip.Divider.INSTANCE);
            }
            else if (!tags.isEmpty()) {
                event.add(Formatting.DARK_GRAY + AMITextRenderer.ITALICS + "Hold CTRL to see " + tags.size() + (tags.size() == 1 ? " tag..." : " tags..."));
            }
        }

        if (AMIConfig.showNbtCount()) {
            event.add(Formatting.GRAY + AMITextRenderer.ITALICS + "NBT Count: " + event.itemStack.getStationNbt().values().size());
        }
        if (AMIConfig.showModNames()) {
            event.add(Formatting.BLUE + AMITextRenderer.ITALICS + AlwaysMoreItems.getItemRegistry().getModNameForItem(event.itemStack.getItem()));
        }
    }
}
