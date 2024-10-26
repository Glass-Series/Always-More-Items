package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class AMIConfigObject {

    @ConfigEntry(name = "Cheat Mode")
    public Boolean cheatMode = false;

    @ConfigEntry(name = "Edit Mode", description = "Click on items to hide them, CTRL to hide all of the same item ID.")
    public Boolean editMode = false;

    @ConfigEntry(name = "Debug Mode", description = "Shows item IDs and damage values.")
    public Boolean debugMode = false;

    @ConfigEntry(name = "Show Mod Names")
    public Boolean showModNames = true;

    @ConfigEntry(name = "Show Nbt Counts")
    public Boolean showNbtCount = false;

    @ConfigEntry(name = "Ignore Bad Names", description = "Should AMI show untranslated names? Modders might want to enable this in dev.")
    public Boolean ignoreBadNames = false;

    @ConfigEntry(name = "Show Redundant/Internal Items", description = "Shows hidden stuff like the lit furnace and wheat crop block.")
    public Boolean showRedundantItems = false;

    @ConfigEntry(
            name = "Max Item List Width",
            maxLength = 100L,
            minLength = 5L
    )
    public Integer maxItemListWidth = 100;

    @ConfigEntry(
            name = "Max Item List Height",
            maxLength = 200L,
            minLength = 10L
    )
    public Integer maxItemListHeight = 200;

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
