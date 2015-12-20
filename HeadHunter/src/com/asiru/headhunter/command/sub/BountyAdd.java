
package com.asiru.headhunter.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Bounties;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.Manager;

public class BountyAdd {
	/**
	 * Runs the "/hunter bounty add" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.bounty.add", "hunter.bounty", "hunter.use"})) {
			if(!HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.HOARD_MODE)) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(args.length == 4) {
						if(Manager.isNumeric(args[3])) {
							if(HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.ValuePlacement.BOUNTY)) {
								OfflinePlayer target = Manager.getPlayerFromString(args[2]);
								if(!target.getUniqueId().toString().equals(p.getUniqueId().toString())) {
									double amount = Double.parseDouble(args[3]);
									if((amount > 0) && (amount < HeadHunter.getEco().getBalance(p))) {
										double min = HeadHunter.getPlugin().getConfig().getDouble(Node.Option.MIN_BOUNTY);
										if(amount >= min) {
											Bounties.applyBounty(p, target, amount);
											HeadHunter.getEco().withdrawPlayer(p, amount);
											String msg = HeadHunter.getPlugin().getConfig().getString(Node.Option.Format.BOUNTY_PLACE);
											msg = Manager.formatRoles(msg, p, target, amount);
											msg = Manager.formatColor(msg);
											if(HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.Message.BOUNTY_NT)) {
												if(HeadHunter.getPlugin().getConfig().getBoolean(Node.Option.Message.BOUNTY_PB))
													Bukkit.broadcastMessage(msg);
												else
													p.sendMessage(msg);
											}
										}
										else
											p.sendMessage(Messages.AMOUNT_LOW);
									}
									else
										p.sendMessage(Messages.AMOUNT_INVALID);
								}
								else
									p.sendMessage(Messages.SELF_TARGET);
							}
							else
								p.sendMessage(Messages.BOUNTY_DISABLED);
						}
						else
							p.sendMessage(Messages.CMD_BOUNTY_ADD);
					}
					else
						p.sendMessage(Messages.CMD_BOUNTY_ADD);
				}
				else
					sender.sendMessage(Messages.PLAYERS_ONLY);
			}
			else
				sender.sendMessage(Messages.HOARD_MODE);
		}
		else
			sender.sendMessage(Messages.NO_PERMS);
	}
}