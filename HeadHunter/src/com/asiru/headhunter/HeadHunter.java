package com.asiru.headhunter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.asiru.headhunter.command.MainExecutor;
import com.asiru.headhunter.listener.PlayerListeners;
import com.asiru.headhunter.listener.SignListeners;
import com.asiru.headhunter.listener.SkullListeners;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Node;

public class HeadHunter extends JavaPlugin {
	private static Plugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		ConfigAccessor accessor1 = new ConfigAccessor(this, "offers.yml");
		ConfigAccessor accessor2 = new ConfigAccessor(this, "skulls.yml");
		ConfigAccessor accessor3 = new ConfigAccessor(this, "signs.yml");
		getCommand("hunter").setExecutor(new MainExecutor());
		getCommand("sellhead").setExecutor(new MainExecutor());
		getCommand("bounty").setExecutor(new MainExecutor());
		getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
		getServer().getPluginManager().registerEvents(new SignListeners(), this);
		getServer().getPluginManager().registerEvents(new SkullListeners(), this);
		saveDefaultConfig();
		refreshDefaults();
		accessor1.saveDefaultConfig();
		accessor2.saveDefaultConfig();
		accessor3.saveDefaultConfig();
		saveConfig();
		Bukkit.getConsoleSender().sendMessage("§a" + getTag() + " has been Enabled!");
	}
	
	@Override
	public void onDisable() {
		saveConfig();
		
		Bukkit.getConsoleSender().sendMessage("§a" + getTag() + " has been Disabled!");
		
		plugin = null;
	}
	
	/**
	 * @return The public instance of this plugin.
	 */
	public static Plugin getPlugin() {
		return plugin;
	}
	
	/**
	 * @return This server's economy manager hooked to Vault.
	 */
	public static Economy getEco() {
		return (Economy) getPlugin().getServer().getServicesManager().getRegistration(Economy.class).getProvider();
	}
	
	public static FileConfiguration getCon() {
		return getPlugin().getConfig();
	}
	
	/**
	 * @return This plugin's name and version number found in the plugin.yml file.
	 */
	public static String getTag() {
		String r = getPlugin().getName() + " v" + getPlugin().getDescription().getVersion();
		return r;
	}
	
	public static List<String> initList() {
		List<String> r = new ArrayList<String>();
		r.add("world");
		r.add("world_nether");
		r.add("world_the_end");
		return r;
	}
	
	/**
	 * Replaces any removed or missing default config values.
	 */
	public static void refreshDefaults() {
		FileConfiguration config = plugin.getConfig();
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put(Node.Option.HOARD_MODE, false);
		map.put(Node.Option.SELL_RATE, 10.0);
		map.put(Node.Option.USE_PERCENT, true);
		map.put(Node.Option.USE_PERMS, true);
		map.put(Node.Option.LIST_SIZE, -1);
		
		map.put(Node.Option.Drop.RATE, 100.0);
		map.put(Node.Option.Drop.BALANCE, true);
		map.put(Node.Option.Drop.BOUNTY, true);
		
		map.put(Node.Option.Message.SELL_NT, true);
		map.put(Node.Option.Message.SELL_PB, true);
		map.put(Node.Option.Message.BOUNTY_NT, true);
		map.put(Node.Option.Message.BOUNTY_PB, true);
		
		map.put(Node.Option.ValuePlacement.BALANCE, true);
		map.put(Node.Option.ValuePlacement.BOUNTY, true);
		map.put(Node.Option.ValuePlacement.CUMULATIVE, false);
		
		map.put(Node.Option.Format.SIGN, "&4&l[SellHead]");
		map.put(Node.Option.Format.SIGN_VALUE, "VALUE");
		map.put(Node.Option.Format.SKULL_VALUE, "&eSell Price:&a $VALUE");
		map.put(Node.Option.Format.SKULL_WORTHLESS, "&eSell Price: &cWorthless");
		map.put(Node.Option.Format.SELL_NOTIFY, "&6HUNTER sold VICTIM's head for $VALUE!");
		map.put(Node.Option.Format.SELL_WORTHLESS, "&cThis skull is worthless! It has been removed from your inventory.");
		map.put(Node.Option.Format.BOUNTY_PLACE, "&6Player &eHUNTER &6has added&e $VALUE &6to &eVICTIM&6's bounty!");
		map.put(Node.Option.Format.BOUNTY_REMOVE, "&6Player &eHUNTER &6has removed&e $VALUE &6from &eVICTIM&6's bounty!");
		map.put(Node.Option.Format.BOUNTY_TOTAL, "&6Total Bounty on &eVICTIM&6:&e $VALUE");
		map.put(Node.Option.Format.BOUNTY_PERSONAL, "&6Your Bounty on &eVICTIM&6:&e $VALUE");
		
		map.put(Node.World.IGNORE_WORLDS, false);
		map.put(Node.World.VALID_WORLDS, initList());
		
		map.put(Node.PluginSupport.FACTIONS_WILDERNESS, true);
		map.put(Node.PluginSupport.FACTIONS_WARZONE, false);
		map.put(Node.PluginSupport.FACTIONS_SAFEZONE, false);
		
		for(Entry<String, Object> entry : map.entrySet()) {
			if(!config.contains(entry.getKey()))
				config.set(entry.getKey(), entry.getValue());
		}
		plugin.saveConfig();
	}
}