package net.glasslauncher.mods.alwaysmoreitems.recipe.multiblock;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;

import javax.annotation.Nullable;

public record BlockPatternEntry(char key, BlockState blockstate, int meta, @Nullable ItemStack item, @Nullable
                                BlockEntity blockEntity){}
