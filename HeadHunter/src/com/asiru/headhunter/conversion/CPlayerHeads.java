package com.asiru.headhunter.conversion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.config.Prop;
import com.asiru.headhunter.util.pairing.PairedSkull;

public class CPlayerHeads implements Listener {
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.convert", "hunter.convert.PlayerHeads"})) {
			if(!HeadHunter.getPlugin().getConfig().getBoolean(Node.O_HOARD_MODE)) {
				ConfigAccessor prop = Manager.getAccessor("properties.yml");
				prop.getConfig().set(Prop.CONVERT_ON_LOGIN, true);
				prop.saveConfig();
				List<String> uuidList = new ArrayList<String>();
				if(prop.getConfig().contains(Prop.CONVERTED_LIST))
					uuidList = prop.getConfig().getStringList(Prop.CONVERTED_LIST);
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(convertInventory(p)) {
						uuidList.add(p.getUniqueId().toString());
						p.sendMessage(Messages.CONVERT_SUCCESS);
					}
				}
				prop.getConfig().set(Prop.CONVERTED_LIST, uuidList);
				prop.saveConfig();
				sender.sendMessage("§aInventories of " + uuidList.size() + " player(s) were successfully converted!");
			}
			else
				sender.sendMessage(Messages.CMD_CONVERT_ERR);
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
	
	public static boolean convertInventory(Player p) {
		Inventory inv = p.getInventory();
		boolean converted = false;
		if(inv.contains(Material.SKULL_ITEM)) {
			for(ItemStack item : inv.getContents()) {
				Material type = null;
				try {
					type = item.getType();
				} catch(NullPointerException npe) {}
				if(type != null && type == Material.SKULL_ITEM) {
					SkullMeta meta = (SkullMeta) item.getItemMeta();
					if(!meta.hasLore()) {
						inv.remove(item);
						if(meta.hasOwner()) {
							int amt = item.getAmount();
							OfflinePlayer owner = Manager.getPlayerFromString(meta.getOwner());
							PairedSkull[] newHeads = new PairedSkull[amt];
							for(int j = 0; j < amt; j++) {
								if(HeadHunter.getEco().hasAccount(owner)) {
									newHeads[j] = HeadFunctions.getPairedSkull(p, owner);
									HeadHunter.getEco().withdrawPlayer(owner, newHeads[j].getEcoLoss());
								}
							}
							for(PairedSkull head : newHeads) {
								if(inv.firstEmpty() == -1)
									p.getWorld().dropItem(p.getLocation(), head.getSkull());
								else
									inv.addItem(head.getSkull());
							}
						}
						if(!converted) {
							converted = true;
							ConfigAccessor prop = Manager.getAccessor("properties.yml");
							List<String> uuidList = new ArrayList<String>();
							if(prop.getConfig().contains(Prop.CONVERTED_LIST))
								uuidList = prop.getConfig().getStringList(Prop.CONVERTED_LIST);
							if(!uuidList.contains(p.getUniqueId().toString()))
								uuidList.add(p.getUniqueId().toString());
							p.sendMessage(Messages.CONVERT_SUCCESS);
							prop.getConfig().set(Prop.CONVERTED_LIST, uuidList);
							prop.saveConfig();
						}
					}
				}
			}
		}
		return converted;
	}
	
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		convertInventory(e.getPlayer());
	}
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent e) {
		convertInventory(e.getPlayer());
	}
}
