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
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.reload"})) {
			ConfigAccessor	offers = Manager.getAccessor("offers.yml"),
							signs = Manager.getAccessor("signs.yml"),
							properties = Manager.getAccessor("properties.yml");
			HeadHunter.getPlugin().reloadConfig();
			offers.reloadConfig(); offers.saveConfig();
			signs.reloadConfig(); signs.saveConfig();
			properties.reloadConfig(); properties.saveConfig();
			sender.sendMessage("§a" + HeadHunter.getTag() + " reloaded!");
			sender.sendMessage("§6Remember, right-clicking the signs will update them to display the correct prices.");
			sender.sendMessage("§6In the future, this will be automatic.");
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}