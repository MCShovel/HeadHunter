package com.asiru.headhunter.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigAccessor {
	private final String fileName;
	private final Plugin plugin;
	private File configFile;
	private FileConfiguration fileConfiguration;
	
	public ConfigAccessor(Plugin plugin, String fileName) {
		if(plugin == null)
			throw new IllegalArgumentException("plugin cannot be null");
		if(!plugin.isEnabled())
			throw new IllegalArgumentException("plugin must be initialized");
		this.plugin = plugin;
		this.fileName = fileName;
		File dataFolder = plugin.getDataFolder();
		if(dataFolder == null)
			throw new IllegalStateException();
		configFile = new File(plugin.getDataFolder(), fileName);
	}
	
	public void reloadConfig() {
		fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
		InputStream defConfigStream = plugin.getResource(fileName);
		if(defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(configFile);
			fileConfiguration.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration getConfig() {
		if(fileConfiguration == null)
			reloadConfig();
		return fileConfiguration;
	}
	
	public void saveConfig() {
		if(fileConfiguration == null || configFile == null)
			return;
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
		}
	}
	
	public void saveDefaultConfig() {
		if(!configFile.exists())
			plugin.saveResource(fileName, false);
	}
}