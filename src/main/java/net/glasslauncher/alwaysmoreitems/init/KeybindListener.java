package net.glasslauncher.alwaysmoreitems.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class KeybindListener {

    public static KeyBinding toggleOverlay;
    public static KeyBinding showRecipe;
    public static KeyBinding showUses;
    public static KeyBinding recipeBack;

    @EventListener
    public static void registerKeybinds(KeyBindingRegisterEvent event) {
        event.keyBindings.add(toggleOverlay = new KeyBinding("key.alwaysmoreitems.toggleOverlay", Keyboard.KEY_O));
        event.keyBindings.add(showRecipe = new KeyBinding("key.alwaysmoreitems.showRecipe", Keyboard.KEY_R));
        event.keyBindings.add(showUses = new KeyBinding("key.alwaysmoreitems.showUses", Keyboard.KEY_U));
        event.keyBindings.add(recipeBack = new KeyBinding("key.alwaysmoreitems.recipeBack", Keyboard.KEY_BACK));
    }
}
