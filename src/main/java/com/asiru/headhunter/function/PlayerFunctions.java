package com.asiru.headhunter.function;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;

public class PlayerFunctions {
	public static double getSellRate(Player p) {
		double sellRate = HeadHunter.getPlugin().getConfig().getDouble(Node.O_SELL_RATE);
		if(p != null) {
			for(PermissionAttachmentInfo perm : p.getEffectivePermissions()) {
				if(perm.getPermission().startsWith("hunter.sellrate.")) {
					String num = perm.getPermission().replace("hunter.sellrate.", "");
					if(Manager.isNumeric(num))
						sellRate = Double.parseDouble(num);
				}
			}
		}
		return sellRate;
	}
	
	public static void updateSignAt(Player p, Location loc) {
		if(loc.getBlock().getState() instanceof Sign) {
			String top = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SIGN_TOP);
			String title = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SIGN_TITLE);
			String value = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SIGN_VALUE);
			String bottom = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SIGN_BOTTOM);
			String rate = getSellRate(p) + "";
			if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_USE_PERCENT))
				value = value + "%";
			else
				value = "$" + value;
			top = Manager.formatColor(top.replaceAll("VALUE", rate));
			title = Manager.formatColor(title.replaceAll("VALUE", rate));
			value = Manager.formatColor(value.replaceAll("VALUE", rate));
			bottom = Manager.formatColor(bottom.replaceAll("VALUE", rate));
			p.sendSignChange(loc, new String[]{
					top,
					title,
					value,
					bottom
			});
		}
	}
	
	public static void updateSigns(Player p) {
		for(World world : Bukkit.getWorlds())
			updateSigns(p, world);
	}
	
	public static void updateSigns(Player p, World w) {
		ConfigAccessor signs = Manager.getAccessor("signs.yml");
		for(String tag : signs.getConfig().getKeys(false)) {
			Location loc = LocationFunctions.buildLocation(tag);
			if(loc.getWorld().getName().equals(w.getName()))
				updateSignAt(p, loc);
		}
	}
	
	public static double getTotalBounty(String targetUUID) {
		ConfigAccessor accessor = Manager.getAccessor("offers.yml");
		double fullValue = 0;
		if(accessor.getConfig().contains(targetUUID)) {
			Set<String> keys = accessor.getConfig().getConfigurationSection(targetUUID).getKeys(false);
			String path = "";
			for(String s : keys) {
				path = targetUUID + "." + s;
				if(accessor.getConfig().contains(path))
					fullValue += accessor.getConfig().getDouble(path);
			}
		}
		return fullValue;
	}
	
	public static boolean isPlayerListed(Player p) {
		if(Manager.getWhitelist().contains(p.getUniqueId().toString()))
			return true;
		return false;
	}
}
