package com.asiru.headhunter.command.sub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Bounties;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.Manager;

public class BountyList {
	/**
	 * Runs the "/hunter bounty list" command for the specified CommandSender with the specified arguments.
	 * @param sender - The CommandSender executing the command.
	 * @param args - The arguments applied to this command.
	 */
	public static void run(CommandSender sender, String[] args) {
		if(Manager.hasAnyPerms(sender, new String[]{"hunter.list", "hunter.bounty", "hunter.bounty.list", "hunter.use"})) {
			ConfigAccessor accessor = Manager.getAccessor("offers.yml");
			FileConfiguration offers = accessor.getConfig();
			Map<OfflinePlayer, Double> map = new HashMap<OfflinePlayer, Double>();
			for(String uuid : offers.getKeys(false)) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				double value = Bounties.getTotalBounty(uuid);
				map.put(player, value);
			}
			List<Map.Entry<OfflinePlayer, Double>> list = new ArrayList<Map.Entry<OfflinePlayer, Double>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<OfflinePlayer, Double>>() {
				public int compare(Map.Entry<OfflinePlayer, Double> entry1, Map.Entry<OfflinePlayer, Double> entry2) {
					return entry1.getValue().compareTo(entry2.getValue());
				}
			});
			Collections.reverse(list);
			if(!list.isEmpty()) {
				sender.sendMessage("§6---=< §eBounty List §6>=---");
				String msg = HeadHunter.getPlugin().getConfig().getString(Node.O_F_BOUNTY_TOTAL);
				int count = HeadHunter.getPlugin().getConfig().getInt(Node.O_LIST_SIZE);
				if(count < 0) {
					for(Map.Entry<OfflinePlayer, Double> entry : list) {
						String sent = Manager.formatBaseRoles(msg, entry.getKey(), entry.getValue());
						sent = Manager.formatColor(sent);
						sender.sendMessage(sent);
					}
				}
				else if(count > 0) {
					for(int i = 0; i < count; i++) {
						if(list.get(i) != null) {
							Map.Entry<OfflinePlayer, Double> entry = list.get(i);
							String sent = Manager.formatBaseRoles(msg, entry.getKey(), entry.getValue());
							sent = Manager.formatColor(sent);
							sender.sendMessage(sent);
						}
						else
							break;
					}
				}
				else
					sender.sendMessage(Messages.BOUNTIES_INVISIBLE);
			}
			else
				sender.sendMessage(Messages.NO_BOUNTIES);
		}
	}
}