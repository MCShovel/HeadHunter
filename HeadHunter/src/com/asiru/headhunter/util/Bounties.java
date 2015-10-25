package com.asiru.headhunter.util;

import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.asiru.headhunter.HeadHunter;

public class Bounties {
	/**
	 * Applies a bounty to the config: creating, remove, adding, or subtracting as necessary.
	 * @param hunter - The player who changed the bounty.
	 * @param target - The player who was targeted in the bounty.
	 * @param amount - The amount of money the victim was targeted for.
	 */
	public static void applyBounty(Player hunter, OfflinePlayer target, double amount) {
		ConfigAccessor accessor = Manager.getAccessor("offers.yml");
		FileConfiguration config = accessor.getConfig();
		String targetName = target.getUniqueId().toString();
		String path = targetName + "." + hunter.getUniqueId().toString();
		if(config.contains(path)) {
			double prev = config.getDouble(path);
			double sum = prev + amount;
			if(sum <= 0)
				config.set(targetName, null);
			else
				config.set(path, Double.valueOf(sum));
		}
		else
			config.set(path, Double.valueOf(amount));
		HeadHunter.getPlugin().saveConfig();
		accessor.saveConfig();
	}
	
	public static double getTotalBounty(String targetUUID) {
		ConfigAccessor accessor = Manager.getAccessor("offers.yml");
		FileConfiguration readOnly = accessor.getConfig();
		double fullValue = 0.0;
		if(readOnly.contains(targetUUID) && !readOnly.getConfigurationSection(targetUUID).getKeys(false).isEmpty()) {
			Set<String> hunters = accessor.getConfig().getConfigurationSection(targetUUID).getKeys(false);
			String path = "";
			for(String s : hunters) {
				path = targetUUID + "." + s;
				if(readOnly.contains(path))
					fullValue += accessor.getConfig().getDouble(path);
			}
		}
		return fullValue;
	}
}