package net.glasslauncher.mods.alwaysmoreitems.init;

import net.glasslauncher.mods.alwaysmoreitems.gui.AMITooltipSystem;
import net.glasslauncher.mods.alwaysmoreitems.recipe.ItemFilter;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.mine_diver.unsafeevents.event.PhaseOrdering;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.event.init.InitFinishedEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ClientInit {

    @EventListener(priority = ListenerPriority.LOW)
    public static void initAMI(InitFinishedEvent event) {
        //noinspection UnstableApiUsage
        PhaseOrdering.of(TooltipRenderEvent.class).addPhaseOrdering(AMITooltipSystem.AMI_TOOLTIP_PHASE, StationAPI.INTERNAL_PHASE);
        AlwaysMoreItems.setItemFilter(new ItemFilter(AlwaysMoreItems.getItemRegistry()));
    }

    @EventListener
    public static void fixNames(BlockRegistryEvent event) {
        // See the lang file for more fixed names.
        Block.PISTON_HEAD.setTranslationKey("pistonHead");
        Block.MOVING_PISTON.setTranslationKey("pistonMoving");
    }
}
