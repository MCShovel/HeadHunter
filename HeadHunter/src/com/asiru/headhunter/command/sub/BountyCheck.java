package com.asiru.headhunter.command.sub;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Bounties;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.Node;
import com.asiru.headhunter.util.Manager;

public class BountyCheck {
	/**
	 * Runs the "/hunter bounty check" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.bounty.check", "hunter.bounty", "hunter.use"})) {
			if(HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.ValuePlacement.BOUNTY)) {
				if(args.length == 3) {
					OfflinePlayer p = Manager.getPlayerFromString(args[2]);
					if(p != null) {
						String targetUUID = p.getUniqueId().toString();
						double fullValue = Bounties.getTotalBounty(targetUUID);
						if(fullValue > 0) {
							String totalMsg = HeadHunter.getPlugin().getConfig().getString(Node.Option.Format.BOUNTY_TOTAL);
							totalMsg = Manager.formatBaseRoles(totalMsg, p, fullValue);
							totalMsg = Manager.formatColor(totalMsg);
							sender.sendMessage(totalMsg);
							if(sender instanceof Player) {
								Player source = (Player) sender;
								String newPath = targetUUID + "." + source.getUniqueId().toString();
								ConfigAccessor accessor = Manager.getAccessor("offers.yml");
								if(accessor.getConfig().contains(newPath)) {
									double amount = accessor.getConfig().getDouble(newPath);
									FileConfiguration config = HeadHunter.getPlugin().getConfig();
									String personMsg = config.getString(Node.Option.Format.BOUNTY_PERSONAL);
									personMsg = Manager.formatRoles(personMsg, source, p, amount);
									personMsg = Manager.formatColor(personMsg);
									sender.sendMessage(personMsg);
								}
							}
						}
						else
							sender.sendMessage(Messages.BOUNTY_INVALID);
					}
					else
						sender.sendMessage(Messages.BOUNTY_INVALID);
				}
				else
					sender.sendMessage(Messages.CMD_BOUNTY_CHECK);
			}
			else
				sender.sendMessage(Messages.BOUNTY_DISABLED);
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}