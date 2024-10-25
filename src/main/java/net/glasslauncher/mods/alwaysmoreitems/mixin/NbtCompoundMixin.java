package net.glasslauncher.mods.alwaysmoreitems.mixin;

import net.glasslauncher.mods.alwaysmoreitems.api.AMINbt;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;

@Mixin(NbtCompound.class)
public class NbtCompoundMixin implements AMINbt {
    @Shadow private Map entries;

    @Override
    public void always_More_Items$removeTag(String name) {
        entries.remove(name);
    }

    @Override
    public Set<String> always_More_Items$getKeySet() {
        //noinspection unchecked If this isn't a string set, we've got bigger issues
        return (Set<String>) entries.keySet();
    }

    @Override
    public boolean always_More_Items$hasNoTags() {
        return entries.isEmpty();
    }

    @Override
    public Set<Map.Entry> always_More_Items$entrySet() {
        return entries.entrySet();
    }
}
