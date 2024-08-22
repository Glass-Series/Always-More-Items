package net.glasslauncher.mods.alwaysmoreitems.api.gui;

import java.util.*;

public interface TooltipCallback<T> {
    /**
     * Change the tooltip for an ingredient.
     */
    void onTooltip(int slotIndex, boolean input, T ingredient, ArrayList<Object> tooltip);
}
