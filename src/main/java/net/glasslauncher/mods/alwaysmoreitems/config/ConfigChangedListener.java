package net.glasslauncher.mods.alwaysmoreitems.config;

import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;

public class ConfigChangedListener implements PreConfigSavedListener {
    @Override
    public void onPreConfigSaved(int source, GlassYamlFile oldValues, GlassYamlFile newValues) {

        if (oldValues.getBoolean("showRedundantItems", false) != newValues.getBoolean("showRedundantItems", false)) {
            AlwaysMoreItems.reloadBlacklist();
        }
        else if (oldValues.getBoolean("editMode", false) != newValues.getBoolean("editMode", false)) {
            AlwaysMoreItems.reloadBlacklist();
        }
    }
}
