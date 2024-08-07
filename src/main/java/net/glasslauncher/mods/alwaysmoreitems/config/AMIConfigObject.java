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
