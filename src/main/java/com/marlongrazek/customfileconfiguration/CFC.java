package com.marlongrazek.customfileconfiguration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class CFC extends YamlConfiguration {

    private final Plugin plugin;
    private final File file;

    public CFC(String name, Plugin plugin) {

        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name.endsWith(".yml") ? name : name + ".yml");

        try {
            loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error while saving the " + file.getName() + " configuration!", e);
        }
    }

    public void reload() {
        try {
            loadConfig();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error while creating the " + file.getName() + " configuration file!", e);
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "The configuration " + file.getName() + " is invalid!", e);
        }
    }

    private void loadConfig() throws IOException, InvalidConfigurationException {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        if (!file.exists()) {
            try {
                // Save the config file if found inside the jar
                plugin.saveResource(file.getName(), false);
            } catch (IllegalArgumentException e) {
                // File not found in resources, creating a blank one
                file.createNewFile();
            }
        }

        load(file);
    }

    public void set(String path, Object object) {
        YamlConfiguration.loadConfiguration(file).set(path, object);
        save();
    }
}
