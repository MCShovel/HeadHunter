package com.asiru.headhunter.command.sub;

import org.bukkit.command.CommandSender;
import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.Manager;

public class Reload {
	/**
	 * Runs the "/hunter reload" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, "hunter.reload")) {
			ConfigAccessor	offers = Manager.getAccessor("offers.yml"),
							signs = Manager.getAccessor("signs.yml"),
							properties = Manager.getAccessor("properties.yml"),
							messages = Manager.getAccessor("messages.yml");
			HeadHunter.getPlugin().reloadConfig();
			offers.reloadConfig(); offers.saveConfig();
			signs.reloadConfig(); signs.saveConfig();
			properties.reloadConfig(); properties.saveConfig();
			messages.reloadConfig(); messages.saveConfig();
			Messages.refresh();
			sender.sendMessage("§a" + HeadHunter.getTag() + " reloaded!");
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}