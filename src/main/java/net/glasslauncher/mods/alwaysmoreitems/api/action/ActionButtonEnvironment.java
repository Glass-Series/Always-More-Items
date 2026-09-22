package net.glasslauncher.mods.alwaysmoreitems.api.action;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.client.Minecraft;

public enum ActionButtonEnvironment {
    /// Only runs client method, not required on server
    CLIENT_ONLY(() -> FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT),
    /// Only available in singleplayer
    SINGLEPLAYER_ONLY(() -> FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && !Minecraft.INSTANCE.world.isRemote),
    /// Use client method if AMI isn't present on the server
    SERVER_AMI_PRESENT_OR_CLIENT(() -> true),
    /// Hide the button if AMI isn't present on the server
    SERVER_AMI_PRESENT_ONLY(AlwaysMoreItems::isAMIOnServer),
    ;

    private final Supplier supplier;

    ActionButtonEnvironment(Supplier supplier) {
        this.supplier = supplier;
    }

    public boolean canBeUsed() {
        return supplier.get();
    }

    @FunctionalInterface
    public interface Supplier {
        boolean get();
    }
}
