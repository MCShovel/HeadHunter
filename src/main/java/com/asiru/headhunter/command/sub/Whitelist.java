package com.asiru.headhunter.command.sub;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.Messages;

public class Whitelist {
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, "hunter.whitelist")) {
			switch(args.length) {
			case 1:
				List<String> uuidList = Manager.getWhitelist();
				if(!uuidList.isEmpty()) {
					sender.sendMessage("§6-=< §eHeadHunter Whitelist §6>=-");
					for(String uuid : uuidList)
						sender.sendMessage("§6 - §e" + Bukkit.getPlayer(UUID.fromString(uuid)).getDisplayName());
				}
				else
					sender.sendMessage(Messages.WHITELIST_EMPTY);
				break;
			case 3:
				if(args[1].equalsIgnoreCase("add")) {
					List<String> list = Manager.getWhitelist();
					if(list != null)  {
						String uuid = Manager.getPlayerFromString(args[2]).getUniqueId().toString();
						if(!list.contains(uuid)) {
							list.add(uuid);
							ConfigAccessor whitelist = Manager.getAccessor("whitelist.yml");
							whitelist.getConfig().set("whitelist", list);
							whitelist.saveConfig();
							sender.sendMessage(Messages.WHITELIST_ADDED);
						}
						else
							sender.sendMessage(Messages.WHITELIST_A_ERR);
					}
					else
						sender.sendMessage(Messages.WHITELIST_ERR);
				}
				else if(args[1].equalsIgnoreCase("remove")) {
					List<String> list = Manager.getWhitelist();
					if(list != null)  {
						String uuid = Manager.getPlayerFromString(args[2]).getUniqueId().toString();
						if(list.contains(uuid)) {
							list.remove(list.indexOf(uuid));
							ConfigAccessor whitelist = Manager.getAccessor("whitelist.yml");
							whitelist.getConfig().set("whitelist", list);
							whitelist.saveConfig();
							sender.sendMessage(Messages.WHITELIST_REMOVED);
						}
						else
							sender.sendMessage(Messages.WHITELIST_R_ERR);
					}
					else
						sender.sendMessage(Messages.WHITELIST_ERR);
				}
				else
					sender.sendMessage(Messages.CMD_WHITELIST);
				break;
			default:
				sender.sendMessage(Messages.CMD_WHITELIST);
				break;
			}
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}
