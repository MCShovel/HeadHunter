package com.asiru.headhunter.command.sub;

import org.bukkit.command.CommandSender;

import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.Manager;

public class Help {
	/**
	 * Runs the "/hunter help" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, "hunter.help", "hunter.use")) {
			for(String s : Messages.HELP_NORMAL) {
				s = Manager.formatColor(s);
				sender.sendMessage(s);
			}
			if(sender.hasPermission("hunter.admin")) {
				for(String s : Messages.HELP_ADMIN) {
					s = Manager.formatColor(s);
					sender.sendMessage(s);
				}
			}
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}