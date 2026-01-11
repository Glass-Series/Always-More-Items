package net.glasslauncher.mods.alwaysmoreitems.gui.multiblock;

import net.minecraft.server.world.PlayerSaveHandler;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.chunk.storage.ChunkStorage;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;

import java.io.File;
import java.util.List;

public class InventoryWorldStorage implements WorldStorage {
    @Override
    public WorldProperties loadProperties() {
        return null;
    }

    @Override
    public void checkSessionLock() {

    }

    @Override
    public ChunkStorage getChunkStorage(Dimension dimension) {
        return null;
    }

    @Override
    public void save(WorldProperties properties, List players) {

    }

    @Override
    public void save(WorldProperties properties) {

    }

    @Override
    public PlayerSaveHandler getPlayerSaveHandler() {
        return null;
    }

    @Override
    public void forceSave() {

    }

    @Override
    public File getWorldPropertiesFile(String name) {
        return null;
    }
}
