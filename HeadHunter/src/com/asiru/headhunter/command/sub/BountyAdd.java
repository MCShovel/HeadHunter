
package com.asiru.headhunter.command.sub;

import java.util.UUID;

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
			if(!HeadHunter.getPlugin().getConfig().getBoolean(Node.O_HOARD_MODE)) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(args.length == 4) {
						if(Manager.isNumeric(args[3])) {
							if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_VP_BOUNTY)) {
								OfflinePlayer target = Manager.getPlayerFromString(args[2]);
								System.out.println(target);
								UUID	tUUID = target.getUniqueId(),
										pUUID = p.getUniqueId();
								if(!tUUID.equals(pUUID)) {
									if(target.hasPlayedBefore()) {
										double amount = Double.parseDouble(args[3]);
										if((amount > 0) && (amount < HeadHunter.getEco().getBalance(p))) {
											double min = HeadHunter.getPlugin().getConfig().getDouble(Node.O_MIN_BOUNTY);
											if(amount >= min) {
												Bounties.applyBounty(p, target, amount);
												HeadHunter.getEco().withdrawPlayer(p, amount);
												String msg = HeadHunter.getPlugin().getConfig().getString(Node.O_F_BOUNTY_PLACE);
												msg = Manager.formatRoles(msg, p, target, amount);
												msg = Manager.formatColor(msg);
												if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_M_BOUNTY_NT)) {
													if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_M_BOUNTY_PB))
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
										p.sendMessage(Messages.PLAYER_INVALID);
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