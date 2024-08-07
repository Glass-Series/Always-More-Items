package net.glasslauncher.mods.alwaysmoreitems.api.gui;

/**
 * A timer to help render things that normally depend on ticks.
 * Get an instance from the IGuiHelper
 */
public interface TickTimer {
    int getValue();

    int getMaxValue();
}
