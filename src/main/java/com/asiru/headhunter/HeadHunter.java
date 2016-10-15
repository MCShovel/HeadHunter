package com.asiru.headhunter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.asiru.headhunter.command.MainExecutor;
import com.asiru.headhunter.conversion.CPlayerHeads;
import com.asiru.headhunter.gui.GUIListeners;
import com.asiru.headhunter.listener.PlayerListeners;
import com.asiru.headhunter.listener.SignListeners;
import com.asiru.headhunter.listener.SkullListeners;
import com.asiru.headhunter.mobhunter.MHListeners;
import com.asiru.headhunter.mobhunter.MobHunter;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.config.Prop;

public class HeadHunter extends JavaPlugin {
	private static Plugin plugin;
	
	public static String combatLogString = "";
	
	@Override
	public void onEnable() {
		plugin = this;
		
		ConfigAccessor offers = new ConfigAccessor(this, "offers.yml");
		ConfigAccessor skulls = new ConfigAccessor(this, "skulls.yml");
		ConfigAccessor signs = new ConfigAccessor(this, "signs.yml");
		ConfigAccessor properties = new ConfigAccessor(this, "properties.yml");
		ConfigAccessor whitelist = new ConfigAccessor(this, "whitelist.yml");
		ConfigAccessor messages = new ConfigAccessor(this, "messages.yml");
		ConfigAccessor mobhunter = new ConfigAccessor(this, "mobhunter.yml");
		
		getCommand("hunter").setExecutor(new MainExecutor());
		getCommand("sellhead").setExecutor(new MainExecutor());
		getCommand("bounty").setExecutor(new MainExecutor());
		
		getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
		getServer().getPluginManager().registerEvents(new SignListeners(), this);
		getServer().getPluginManager().registerEvents(new SkullListeners(), this);
		getServer().getPluginManager().registerEvents(new CPlayerHeads(), this);
		getServer().getPluginManager().registerEvents(new GUIListeners(), this);
		getServer().getPluginManager().registerEvents(new MHListeners(), this);
		
		saveDefaultConfig();
		refreshConfig();
		MobHunter.refreshDefaults();
		reloadConfig();
		
		offers.saveDefaultConfig();
		skulls.saveDefaultConfig();
		signs.saveDefaultConfig();
		properties.saveDefaultConfig();
		whitelist.saveDefaultConfig();
		messages.saveDefaultConfig();
		mobhunter.saveDefaultConfig();
		
		combatLogString = Manager.getCombatLogString();
		
		Bukkit.getConsoleSender().sendMessage("§a" + getTag() + " has been Enabled!");
	}
	
	@Override
	public void onDisable() {
		saveConfig();
		
		Bukkit.getConsoleSender().sendMessage("§a" + getTag() + " has been Disabled!");
		
		plugin = null;
	}
	
	/**
	 * @return The main instance of this plugin.
	 */
	public static Plugin getPlugin() {
		return plugin;
	}
	
	/**
	 * @return This server's economy manager hooked to Vault.
	 */
	public static Economy getEco() {
		return Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
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
	public void refreshConfig() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put(Node.O_HOARD_MODE, false);
		map.put(Node.O_SELL_RATE, 10.0);
		map.put(Node.O_USE_PERCENT, true);
		map.put(Node.O_USE_PERMS, true);
		map.put(Node.O_LIST_SIZE, -1);
		map.put(Node.O_MIN_BOUNTY, 20.0);
		
		map.put(Node.O_G_ENABLED, false);
		map.put(Node.O_G_TITLE, "&4&lSell Heads");
		map.put(Node.O_G_SIZE, 54);
		map.put(Node.O_G_TOTAL_VALUE, "&eTotal Sell Price:&a $VALUE");
		map.put(Node.O_G_SELL_MESSAGE, "&6HUNTER sold AMOUNT head(s) for $VALUE!");
		
		map.put(Node.O_D_RATE, 100.0);
		map.put(Node.O_D_BALANCE, true);
		map.put(Node.O_D_BOUNTY, true);
		map.put(Node.O_D_FIRE_TICK, false);
		map.put(Node.O_D_ANY_CAUSE, false);
		
		map.put(Node.O_M_SELL_NT, true);
		map.put(Node.O_M_SELL_PB, true);
		map.put(Node.O_M_BOUNTY_NT, true);
		map.put(Node.O_M_BOUNTY_PB, true);
		
		map.put(Node.O_VP_BALANCE, true);
		map.put(Node.O_VP_BOUNTY, true);
		map.put(Node.O_VP_CUMULATIVE, false);
		
		map.put(Node.O_F_SIGN_TOP, "---------------");
		map.put(Node.O_F_SIGN_TITLE, "&4&l[SellHead]");
		map.put(Node.O_F_SIGN_VALUE, "VALUE");
		map.put(Node.O_F_SIGN_BOTTOM, "---------------");
		map.put(Node.O_F_SKULL_VALUE, "&eSell Price:&a $VALUE");
		map.put(Node.O_F_SKULL_WORTHLESS, "&eSell Price: &cWorthless");
		map.put(Node.O_F_SELL_NOTIFY, "&6HUNTER sold VICTIM\'s head for $VALUE!");
		map.put(Node.O_F_SELL_WORTHLESS, "&cThis skull is worthless! It has been removed from your inventory.");
		map.put(Node.O_F_BOUNTY_PLACE, "&6Player &eHUNTER &6has added&e $VALUE &6to &eVICTIM&6\'s bounty!");
		map.put(Node.O_F_BOUNTY_REMOVE, "&6Player &eHUNTER &6has removed&e $VALUE &6from &eVICTIM&6\'s bounty!");
		map.put(Node.O_F_BOUNTY_TOTAL, "&6Total Bounty on &eVICTIM&6:&e $VALUE");
		map.put(Node.O_F_BOUNTY_PERSONAL, "&6Your Bounty on &eVICTIM&6:&e $VALUE");
		
		map.put(Node.W_IGNORE_WORLDS, false);
		map.put(Node.W_VALID_WORLDS, initList());
		
		map.put(Node.PS_FACTIONS_WILDERNESS, true);
		map.put(Node.PS_FACTIONS_WARZONE, false);
		map.put(Node.PS_FACTIONS_SAFEZONE, false);
		
		//map.put(Node.PS_COMBATLOG_ON_LOGOUT, true);
		
		//map.put(Node.PS_COMBATTAG_NPC_KILL, true);
		
		for(Entry<String, Object> entry : map.entrySet()) {
			if(!getConfig().contains(entry.getKey()))
				getConfig().set(entry.getKey(), entry.getValue());
		}
		saveConfig();
	}
	
	public static void refreshProperties() {
		ConfigAccessor prop = Manager.getAccessor("properties.yml");
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put(Prop.CONVERT_ON_LOGIN, false);
		
		for(Entry<String, Object> entry : map.entrySet()) {
			if(!prop.getConfig().contains(entry.getKey()))
				prop.getConfig().set(entry.getKey(), entry.getValue());
		}
		prop.saveConfig();
	}
}