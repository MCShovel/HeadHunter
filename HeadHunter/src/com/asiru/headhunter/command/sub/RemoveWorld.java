package com.asiru.headhunter.command.sub;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.Node;
import com.asiru.headhunter.util.Manager;

public class RemoveWorld {
	/**
	 * Runs the "/hunter removeworld" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.removeworld"})) {
			List<String> list = HeadHunter.getPlugin().getConfig().getStringList(Node.World.VALID_WORLDS);
			switch(args.length) {
			case 1:
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(list.contains(p.getWorld().getName())) {
						list.remove(list.indexOf(p.getWorld().getName()));
						HeadHunter.getPlugin().getConfig().set(Node.World.VALID_WORLDS, list);
						HeadHunter.getPlugin().saveConfig();
						sender.sendMessage(Messages.WORLD_REMOVED);
					}
					else
						sender.sendMessage(Messages.WORLD_INVALID);
				}
				else
					sender.sendMessage(Messages.CMD_REMOVE);
				break;
			case 2:
				if(list.contains(args[1])) {
					list.remove(list.indexOf(args[1]));
					HeadHunter.getPlugin().getConfig().set(Node.World.VALID_WORLDS, list);
					HeadHunter.getPlugin().saveConfig();
					sender.sendMessage(Messages.WORLD_REMOVED);
				}
				else
					sender.sendMessage(Messages.WORLD_INVALID);
				break;
			default:
				sender.sendMessage(Messages.CMD_REMOVE);
				break;
			}
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}