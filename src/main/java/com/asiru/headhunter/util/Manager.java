package com.asiru.headhunter.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.function.LocationFunctions;
import com.asiru.headhunter.function.PlayerFunctions;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.pairing.PairedSkull;

public class Manager {
	static DecimalFormat format = new DecimalFormat("0.00");
	static String[] lightningEvents = {"31-10"};
	
	/**
	 * Finds whether a string contains only numbers and decimals.
	 * @param s - The string to be tested.
	 * @return Whether the string is numeric or not.
	 */
	public static boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Replaces all color codes using '&' with standard color codes.
	 * @param s - The string to be formatted.
	 * @return The formatted string.
	 */
	public static String formatColor(String s) {
		s = s.replaceAll("&([a-zA-Z0-9])", "�$1");
		return s;
	}
	
	/**
	 * Replaces all color codes using a specified regex with standard color codes.
	 * @param s - The string to be formatted.
	 * @param codeRegex - The regex to call a non-standard color code.
	 * @return The formatted string.
	 */
	public static String formatColor(String s, String codeRegex) {
		s = s.replaceAll(codeRegex + "([a-zA-Z0-9])", "�$1");
		return s;
	}
	
	public static String formatMoney(double n) {
		return format.format(n);
	}
	
	/**
	 * Replaces text variables in a string with their respective roles.
	 * @param s - The string to be formatted.
	 * @param hunter - The player to replace "HUNTER".
	 * @param target - The player to replace "VICTIM".
	 * @param value - The number to replace "VALUE".
	 * @return The formatted string.
	 */
	public static String formatRoles(String s, Player hunter, OfflinePlayer target, double value) {
		s = s.replaceAll("HUNTER", hunter.getName());
		s = s.replaceAll("VICTIM", target.getName());
		s = s.replaceAll("VALUE", format.format(value));
		return s;
	}
	
	/**
	 * Replaces text variables in a string with their respective roles.
	 * @param s - The string to be formatted.
	 * @param hunter - The player to replace "HUNTER".
	 * @param targetName - The player's name to replace "VICTIM".
	 * @param value - The number to replace "VALUE".
	 * @return The formatted string.
	 */
	public static String formatRolesRaw(String s, Player hunter, String targetName, double value) {
		if(hunter != null)
			s = s.replaceAll("HUNTER", hunter.getName());
		s = s.replaceAll("VICTIM", targetName);
		s = s.replaceAll("VALUE", format.format(value));
		return s;
	}
	
	/**
	 * Replaces text variables in a string with their respective roles.
	 * @param s - The string to be formatted.
	 * @param target - The player to replace "VICTIM".
	 * @param value - The number to replace "VALUE".
	 * @return The formatted string.
	 */
	public static String formatBaseRoles(String s, OfflinePlayer target, double value) {
		s = s.replaceAll("VICTIM", target.getName());
		s = s.replaceAll("VALUE", format.format(value));
		return s;
	}
	
	/**
	 * Finds whether the specified CommandSender has any of the permissions in the specified array.
	 * @param cs - The CommandSender to be tested.
	 * @param perms - The string array to be compared with.
	 * @return Whether the CommandSender has any of the permissions in the array.
	 */
	public static boolean hasAnyPerms(CommandSender cs, String... perms) {
		FileConfiguration config = HeadHunter.getPlugin().getConfig();
		if(!config.getBoolean(Node.O_USE_PERMS))
			return true;
		if(cs.hasPermission("hunter.admin"))
			return true;
		for(String s : perms) {
			if(cs.hasPermission(s))
				return true;
		}
		return false;
	}
	
	/**
	 * Finds an OfflinePlayer by their name.
	 * @param s - The string value of the OfflinePlayer's name.
	 * @return The OfflinePlayer associated with that name.
	 */
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getPlayerFromString(String s) {
		if(Bukkit.getPlayer(s) != null)
			return Bukkit.getPlayer(s);
		if(Bukkit.getPlayerExact(s) != null)
			return Bukkit.getPlayerExact(s);
		return Bukkit.getOfflinePlayer(s);
	}

	/**
	 * Finds the list of UUIDs in the whitelist.yml.
	 * @return The List of player UUIDs
	 */
	public static List<String> getWhitelist() {
		List<String> list = new ArrayList<String>();
		ConfigAccessor whitelist = Manager.getAccessor("whitelist.yml");
		if(whitelist.getConfig().contains("whitelist"))
			list = whitelist.getConfig().getStringList("whitelist");
		return list;
	}
	
	/**
	 * Drops the head and deducts the price. Only use inside a PlayerDeathEvent!
	 * @param victim - The dead one.
	 */
	public static void doThings(Player victim, Player killer) {
		double rate = HeadHunter.getPlugin().getConfig().getDouble(Node.O_D_RATE);
		if(HeadFunctions.luckDrop(rate)) {
			Location eventLoc = victim.getLocation();
			World eventWorld = victim.getWorld();
			if(LocationFunctions.validLocation(victim)) {
				if(!victim.hasPermission("hunter.nodrop") || PlayerFunctions.isPlayerListed(victim)) {
					PairedSkull pair = HeadFunctions.getPairedSkull(killer, victim);
					if(HeadFunctions.dropWith(pair.getState())) {
						HeadHunter.getEco().withdrawPlayer(victim, pair.getEcoLoss());
						eventWorld.dropItemNaturally(eventLoc, pair.getSkull());
					}
				}
			}
		}
	}
	
	public static boolean isLightningEvent() {
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
		for(String event : lightningEvents) {
			if(event.equals(sdf.format(current)))
				return true;
		}
		return false;
	}
	
	public static String getCombatLogString() {
		if(Bukkit.getPluginManager().isPluginEnabled("CombatTagPlus"))
			return "CombatTagPlus";
		if(Bukkit.getPluginManager().isPluginEnabled("CombatTag"))
			return "CombatTag";
		if(Bukkit.getPluginManager().isPluginEnabled("PvPManager"))
			return "PvPManager";
		if(Bukkit.getPluginManager().isPluginEnabled("CombatLog"))
			return "CombatLog";
		return "";
	}
	
	public static ConfigAccessor getAccessor(String fileName) {
		return new ConfigAccessor(HeadHunter.getPlugin(), fileName);
	}
}