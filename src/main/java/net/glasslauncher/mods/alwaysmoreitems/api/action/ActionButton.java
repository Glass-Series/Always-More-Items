package net.glasslauncher.mods.alwaysmoreitems.api.action;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

@SuppressWarnings("UnusedReturnValue")
public interface ActionButton {
    /**
     * @param minecraft   {@link Minecraft} on client and {@link MinecraftServer} on server
     * @param world       The world the player is currently in
     * @param player      The player who executed the action
     * @param isOperator  If the player is opped. This will always be true in Singleplayer
     * @param mouseButton Mouse button used to click on the button.
     *                    <p> 0 is Left Button, 1 is Right Button, 2 is Middle Button, 3 and 4 are Side Buttons
     * @return Whether the action was performed
     */
    boolean perform(Object minecraft, World world, PlayerEntity player, boolean isOperator, int mouseButton, boolean holdingShift);

    /**
     * I would not recommend touching this to keep the button grid consistent
     *
     * @return The width of the button
     */
    default int getWidth() {
        return 20;
    }

    /**
     * I would not recommend touching this to keep the button grid consistent
     *
     * @return The height of the button
     */
    default int getHeight() {
        return 20;
    }

    /**
     * It is recommended to use 16x16 texture
     *
     * @return The path to the texture that will be drawn on the button
     */
    String getTexture();

    /**
     * @return The sound that will be played when the button is clicked
     */
    default String getClickSound() {
        return "random.click";
    }

    /**
     * If the button is only clientsided, it will be executed on client even in a multiplayer environment
     *
     * @return Whether the button is only clientsided
     */
    default boolean isClientsideOnly() {
        return false;
    }

    default boolean dontAddToScreen() {
        return false;
    }

    default boolean tooltipEnabled() {
        return true;
    }

    /**
     * If the button is only visible in cheat or utility mode
     * @return Whether the button is only visible in cheat or utility mode
     */
    default boolean cheatModeOnly(){
        return false;
    }
}
