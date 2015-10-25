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
import com.asiru.headhunter.util.Node;

public class PlayerFunctions {
	public static double getSellRate(Player p) {
		double sellRate = HeadHunter.getCon().getDouble(Node.Option.SELL_RATE);
		for(PermissionAttachmentInfo perm : p.getEffectivePermissions()) {
			if(perm.getPermission().startsWith("hunter.sellrate.")) {
				String num = perm.getPermission().replace("hunter.sellrate.", "");
				if(Manager.isNumeric(num))
					sellRate = Double.parseDouble(num);
			}
		}
		return sellRate;
	}
	
	public static void updateSignAt(Player p, Location loc) {
		if(loc.getBlock().getState() instanceof Sign) {
			String value = HeadHunter.getCon().getString(Node.Option.Format.SIGN_VALUE);
			if(HeadHunter.getCon().getBoolean(Node.Option.USE_PERCENT))
				value = value + "%";
			else
				value = "$" + value;
			value = Manager.formatColor(value.replaceAll("VALUE", getSellRate(p) + ""));
			String title = Manager.formatColor(HeadHunter.getCon().getString(Node.Option.Format.SIGN));
			p.sendSignChange(loc, new String[]{
					"---------------",
					title,
					value,
					"---------------"
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
}
