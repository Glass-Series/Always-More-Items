package net.glasslauncher.mods.alwaysmoreitems.api.event;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class AMIItemTooltipEvent extends Event {

    public final @NotNull ItemStack itemStack;
    public final List<Object> tooltip;
}
