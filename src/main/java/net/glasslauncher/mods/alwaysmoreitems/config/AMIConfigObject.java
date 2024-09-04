package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class AMIConfigObject {

    @ConfigEntry(name = "Cheat Mode")
    public Boolean cheatMode = false;

    @ConfigEntry(name = "Edit Mode")
    public Boolean editMode = false;

    @ConfigEntry(name = "Debug Mode")
    public Boolean debugMode = false;

    @ConfigEntry(name = "Show Mod Names")
    public Boolean showModNames = true;

    @ConfigEntry(name = "Show Nbt Counts")
    public Boolean showNbtCount = true;

    @ConfigEntry(
            name = "Max Item List Width",
            maxLength = 100L,
            minLength = 5L
    )
    public Integer maxItemListWidth = 250;

    @ConfigEntry(
            name = "Max Item List Height",
            maxLength = 200L,
            minLength = 10L
    )
    public Integer maxItemListHeight = 1000;

    @ConfigEntry(
            name = "Right Click Give Amount",
            maxLength = 64
    )
    public Integer rightClickGiveAmount = 1;

    @ConfigEntry(
            name = "Item Blacklist",
            description = "", // todo: do the desc
            hidden = true
    )
    public ConfigItemBlacklist itemBlacklist = new ConfigItemBlacklist();

    @ConfigEntry(
            name = "NBT Blacklist",
            description = "", // todo: do the desc
            hidden = true
    )
    public ConfigItemBlacklist nbtBlacklist = new ConfigItemBlacklist();
}
