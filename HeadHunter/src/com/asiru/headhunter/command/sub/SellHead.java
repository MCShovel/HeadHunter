package com.asiru.headhunter.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.gui.HunterGUI;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.Manager;

public class SellHead {
	/**
	 * Runs the "/hunter sellhead" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(!HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.HOARD_MODE)) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(Manager.hasAnyPerms(sender, new String[]{"hunter.sell", "hunter.use"})) {
					if(HunterGUI.isEnabled())
						HunterGUI.openGUI(p);
					else
						HeadFunctions.sellSkull(p);
				}
				else
					p.sendMessage(Messages.NO_PERMS);
			}
			else
				sender.sendMessage(Messages.PLAYERS_ONLY);
		}
		else
			sender.sendMessage(Messages.HOARD_MODE);
	}
}