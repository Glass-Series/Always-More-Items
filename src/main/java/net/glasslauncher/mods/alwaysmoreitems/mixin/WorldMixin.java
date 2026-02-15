package net.glasslauncher.mods.alwaysmoreitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class WorldMixin {

    @WrapOperation(method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;initializeSpawnPoint()V"))
    private void e(World instance, Operation<Void> original) {
        if (!AlwaysMoreItems.isInitializing()) {
            original.call(instance);
        }
    }
}
