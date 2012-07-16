package net.caldonia.bukkit.plugins.RemapGroup;

import com.platymuus.bukkit.permissions.PermissionsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class RGPlugin extends JavaPlugin {
    private CRGCommandExecutor commandExecutor;
    private PermissionsPlugin permissionsPlugin = null;
    HashMap<String, Mapping> groupMappings = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        // Bind command executor
        commandExecutor = new CRGCommandExecutor(this);
        getCommand("crg").setExecutor(commandExecutor);

        // Find PermissionsBukkit, as without it this plugin is as good as a chocolate fireguard.
        permissionsPlugin = (PermissionsPlugin) this.getServer().getPluginManager().getPlugin("PermissionsBukkit");

        if (permissionsPlugin == null) {
            consoleOut("Could not hook PermissionsBukkit, aborting load!");
            return;
        }

        // Copy out default config.yml file out of the JAR if we need to.
        if (!new File(this.getDataFolder() + "/config.yml").isFile())
            this.saveDefaultConfig();

        // Now load in our mappings.
        loadConfig();

        // Register our monitor.
        this.getServer().getPluginManager().registerEvents(new PlayerMonitor(permissionsPlugin, this), this);

        consoleOut("Loaded!");
    }

    public void loadConfig() {
        // Reload from disk
        this.reloadConfig();

        // Get configuration object
        FileConfiguration config = this.getConfig();
        ConfigurationSection mapping = config.getConfigurationSection("config.mapping");

        Set<String> mappings = mapping.getKeys(false);

        // Empty out current mappings.
        groupMappings = new HashMap<String, Mapping>();

        // Cycle through all the mappings
        for (String map: mappings) {
            String from = mapping.getString(map + ".from");
            String to = mapping.getString(map + ".to");
            String direct = mapping.getString(map + ".direct");
            String announce = mapping.getString(map + ".announce");

            if (from == null || to == null) {
                consoleOut("'" + map + "' is an invalid mapping (missing from or to).");
                continue;
            }

            groupMappings.put(from, new Mapping(map, from, to, direct, announce));
        }
    }

    public HashMap<String, Mapping> getGroupMappings() {
        return groupMappings;
    }

    public void consoleOut(String output) {
        System.out.println("[CaldoniaRemapGroup] " + output);
    }
}
