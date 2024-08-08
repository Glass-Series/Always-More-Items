package net.glasslauncher.mods.alwaysmoreitems.gui;

public enum TooltipEdge {
    LEFT(1, 3),
    RIGHT(1, 3),
    LEFT_RIGHT(2, 6),
    TOP(0, 3),
    BOTTOM(0, 2),
    HEADER_BOTTOM(0, 1),
    TOP_BOTTOM(0, 5),
    HEADER_TOP_BOTTOM(0, 4),
    ;

    public final int rarityIconCount;
    public final int padding;

    TooltipEdge(int rarityIconCount, int padding) {
        this.rarityIconCount = rarityIconCount;
        this.padding = padding;
    }
}
