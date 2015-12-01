package com.asiru.headhunter.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Bounties;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.Manager;

public class BountyRemove {
	/**
	 * Runs the "/hunter bounty remove" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.bounty.remove", "hunter.bounty", "hunter.use"})) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(args.length == 3 || args.length == 4)  {
					OfflinePlayer target = Manager.getPlayerFromString(args[2]);
					String path = target.getUniqueId().toString() + "." + p.getUniqueId().toString();
					ConfigAccessor accessor = Manager.getAccessor("offers.yml");
					if(accessor.getConfig().contains(path)) {
						double amount = 0;
						if(args.length == 3)
							amount = -accessor.getConfig().getDouble(path);
						if(args.length == 4)
							amount = -Double.parseDouble(args[3]);
						Bounties.applyBounty(p, target, amount);
						HeadHunter.getEco().depositPlayer(p, Math.abs(amount));
						String msg = HeadHunter.getPlugin().getConfig().getString(Node.Option.Format.BOUNTY_REMOVE);
						msg = Manager.formatRoles(msg, p, target, Math.abs(amount));
						msg = Manager.formatColor(msg);
						if(HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.Message.BOUNTY_NT)) {
							if(HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.Message.BOUNTY_PB))
								Bukkit.broadcastMessage(msg);
							else
								p.sendMessage(msg);
						}
					}
					else
						p.sendMessage(Messages.BOUNTY_INVALID);
				}
				else
					p.sendMessage(Messages.CMD_BOUNTY_REMOVE);
			}
			else
				sender.sendMessage(Messages.PLAYERS_ONLY);
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}