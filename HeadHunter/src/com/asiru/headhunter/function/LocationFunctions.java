package com.asiru.headhunter.function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;

public class LocationFunctions {
	/**
	 * Returns whether the specified player is in a valid location to drop heads.
	 * @param p - The player to be tested.
	 * @return Whether the player is in a valid location or not.
	 */
	public static boolean validLocation(Player p) {
		boolean r = true;
		Plugin plugin = HeadHunter.getPlugin();
		FileConfiguration config = plugin.getConfig();
		if(!config.getBoolean(Node.World.IGNORE_WORLDS)) {
			if(!config.getStringList(Node.World.VALID_WORLDS).contains(p.getWorld().getName()))
				r = false;
		}
		if(Bukkit.getPluginManager().isPluginEnabled("Factions")) {
			Plugin factionsPlugin = Bukkit.getPluginManager().getPlugin("Factions");
			if(factionsPlugin.getDescription().getDepend().contains("MassiveCore")) {
				com.massivecraft.factions.entity.Faction d = FactionColl.get().getNone();
				com.massivecraft.factions.entity.Faction w = FactionColl.get().getWarzone();
				com.massivecraft.factions.entity.Faction s = FactionColl.get().getSafezone();
				com.massivecraft.factions.entity.Faction f = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
				if((!config.getBoolean(Node.PluginSupport.FACTIONS_WILDERNESS)) && (f.equals(d)))
					r = false;
				if((!config.getBoolean(Node.PluginSupport.FACTIONS_WARZONE)) && (f.equals(w)))
					r = false;
				if((!config.getBoolean(Node.PluginSupport.FACTIONS_SAFEZONE)) && (f.equals(s)))
					r = false;
			}
			/*
			else {
				com.massivecraft.factions.Faction d = Factions.getInstance().getNone();
				com.massivecraft.factions.Faction w = Factions.getInstance().getWarZone();
				com.massivecraft.factions.Faction s = Factions.getInstance().getSafeZone();
				com.massivecraft.factions.Faction f = Board.getInstance().getFactionAt(new FLocation(p));
				if((!config.getBoolean("plugin-support.factions.enabled.wilderness")) && (f.equals(d)))
					r = false;
				if((!config.getBoolean("plugin-support.factions.enabled.warzone")) && (f.equals(w)))
					r = false;
				if((!config.getBoolean("plugin-support.factions.enabled.safezone")) && (f.equals(s)))
					r = false;
			}
			*/
		}
		if(plugin.getServer().getPluginManager().isPluginEnabled("Minigames")) {
			/*
			if(!config.getBoolean("plugin-support.minigames.enabled")) {
				MinigamePlayer mp = new MinigamePlayer(p);
				if(mp.isInMinigame())
					r = false;
			}
			*/
		}
		return r;
	}

	/**
	 * Breaks down a Location object into a String readable by the plugin.
	 * @param loc - The Location to be parsed.
	 * @return The parsed configuration tag as a String.
	 */
	public static String parseLocation(Location loc) {
		String tag = "";
		tag += loc.getWorld().getName() + "%";
		tag += loc.getX() + "%";
		tag += loc.getY() + "%";
		tag += loc.getZ();
		tag = tag.replaceAll("(\\d+)\\.(\\d+)", "$1,$2");
		return tag;
	}

	/**
	 * Creates a new Location from a raw configuration tag.
	 * @param tag - The raw configuration tag taken from the arenas.yml file.
	 * @return The built Location object.
	 */
	public static Location buildLocation(String tag) {
		String[] arr = tag.split("%", 4);
		arr[0] = arr[0].replaceAll(",", ".");
		arr[1] = arr[1].replaceAll(",", ".");
		arr[2] = arr[2].replaceAll(",", ".");
		arr[3] = arr[3].replaceAll(",", ".");
		double x = 0, y = 0, z = 0;
		if(Manager.isNumeric(arr[1]))
			x = Double.parseDouble(arr[1]);
		if(Manager.isNumeric(arr[2]))
			y = Double.parseDouble(arr[2]);
		if(Manager.isNumeric(arr[2]))
			z = Double.parseDouble(arr[2]);
		Location loc = new Location(Bukkit.getWorld(arr[0]), x, y, z);
		return loc;
	}
	
	public static World findWorld(String tag) {
		Location loc = buildLocation(tag);
		return loc.getWorld();
	}
}