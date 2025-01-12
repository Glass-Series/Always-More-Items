package net.glasslauncher.mods.alwaysmoreitems.config;

public enum OverlayMode {
    RECIPE("gui.config.alwaysmoreitems.overlaymode.recipe"),
    CHEAT("gui.config.alwaysmoreitems.overlaymode.cheat"),
    UTILITY("gui.config.alwaysmoreitems.overlaymode.utility");

    public final String translationKey;

    OverlayMode(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String toString() {
        return translationKey;
    }
}
