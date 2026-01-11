package net.glasslauncher.mods.alwaysmoreitems.gui.multiblock;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.Arrays;

public class InventoryBiomeSource extends BiomeSource {

    public InventoryBiomeSource(World world){
        super(world);
    }

    @Override
    public Biome[] getBiomesInArea(Biome[] biomes, int x, int z, int width, int depth) {
        biomes = new Biome[width * depth];
        this.temperatureMap = new double[width * depth];
        this.downfallMap = new double[width * depth];
        this.weirdnessMap = new double[width * depth];
        Arrays.fill(biomes, Biome.PLAINS);

        // Setting these values to something to avoid crashing
        Arrays.fill(temperatureMap, 0f);
        Arrays.fill(downfallMap, 0f);
        Arrays.fill(weirdnessMap, 0f);
        return biomes;
    }
}
