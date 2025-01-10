package net.glasslauncher.mods.alwaysmoreitems.config;

public enum OverlayMode {
    RECIPE("gui.config.alwaysmoreitems.overlaymode.recipe", false),
    CHEAT("gui.config.alwaysmoreitems.overlaymode.cheat", true),
    UTILITY("gui.config.alwaysmoreitems.overlaymode.utility", true);

    public final String translationKey;
    
    public final boolean showCheatActionButtons;

    OverlayMode(String translationKey, boolean showCheatActionButtons) {
        this.translationKey = translationKey;
        this.showCheatActionButtons = showCheatActionButtons;
    }

    @Override
    public String toString() {
        return translationKey;
    }
}
