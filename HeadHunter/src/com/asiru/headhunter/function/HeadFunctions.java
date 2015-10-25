package com.asiru.headhunter.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.Node;
import com.asiru.headhunter.util.pairing.PairState;
import com.asiru.headhunter.util.pairing.PairedSkull;

public class HeadFunctions {
	/**
	 * Sells one of the heads that the player is holding.
	 * @param p - The player who is selling a head.
	 */
	public static void sellSkull(Player p) {
		if(!HeadHunter.getCon().getBoolean(Node.Option.HOARD_MODE)) {
			if(p.getItemInHand().getType() == Material.SKULL_ITEM) {
				ItemStack skull = p.getItemInHand();
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				if(skull.getDurability() == 3) {
					if(sm.hasOwner() && sm.hasLore()) {
						if(Manager.getPlayerFromString(sm.getOwner()).getPlayer() != p) {
							double skullWorth = HeadFunctions.getValueOnSkull(skull);
							HeadHunter.getEco().depositPlayer(p, skullWorth);
							int amt = skull.getAmount();
							if(amt == 1)
								p.getInventory().remove(skull);
							else
								skull.setAmount(--amt);
							if(HeadHunter.getCon().getBoolean(Node.Option.Message.SELL_NT)) {
								boolean pub = HeadHunter.getCon().getBoolean(Node.Option.Message.SELL_PB);
								String notify = "";
								if(skullWorth > 0.0)
									notify = HeadHunter.getCon().getString(Node.Option.Format.SELL_NOTIFY);
								else {
									notify = HeadHunter.getCon().getString(Node.Option.Format.SELL_WORTHLESS);
									pub = false;
								}
								notify = Manager.formatRolesRaw(notify, p, sm.getOwner(), skullWorth);
								notify = Manager.formatColor(notify);
								if(pub)
									Bukkit.broadcastMessage(notify);
								else
									p.sendMessage(notify);
							}
						}
						else
							p.sendMessage(Messages.SELF_TARGET);
					}
					else
						p.sendMessage(Messages.HEAD_INVALID);
				}
				else
					p.sendMessage(Messages.NO_HEADS);
			}
			else {
				p.sendMessage(Messages.NO_SKULLS);
				Set<Material> set = new HashSet<Material>();
				set.add(Material.AIR);
				PlayerFunctions.updateSignAt(p, p.getTargetBlock(set, 10).getLocation());
				p.sendMessage(Messages.SIGN_UPDATE);
			}
		}
		else
			p.sendMessage(Messages.HOARD_MODE);
	}
	
	/**
	 * Finds a player's skull and adds the value of their skull to the lore.
	 * @param p - The owner of the skull to be found.
	 * @return The ItemStack of the specified player's skull.
	 */
	public static PairedSkull getPairedSkull(Player hunter, Player victim) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(victim.getName());
		PairState state = PairState.NEITHER;
		List<String> loreList = new ArrayList<String>();
		double	balance = HeadHunter.getEco().getBalance(victim),
				sellRate = PlayerFunctions.getSellRate(hunter),
				skullWorth = 0,
				ecoLoss = 0;
		if(!HeadHunter.getCon().getBoolean(Node.Option.HOARD_MODE)) {
			if(HeadHunter.getCon().getBoolean(Node.Option.ValuePlacement.BALANCE)) {
				state = PairState.BALANCE;
				if(HeadHunter.getCon().getBoolean(Node.Option.USE_PERCENT))
					skullWorth = balance * (sellRate / 100.0);
				else
					skullWorth = sellRate;
				if(skullWorth > balance)
					skullWorth = balance;
				
				skullWorth = Math.round(skullWorth * 100.0) / 100.0;
				ecoLoss = skullWorth;
			}
			if(HeadHunter.getCon().getBoolean(Node.Option.ValuePlacement.BOUNTY)) {
				if(state == PairState.BALANCE)
					state = PairState.BOTH;
				else
					state = PairState.BOUNTY;
				ConfigAccessor offers = Manager.getAccessor("offers.yml");
				String targetUUID = victim.getUniqueId().toString();
				if(offers.getConfig().contains(targetUUID)) {
					double fullValue = PlayerFunctions.getTotalBounty(targetUUID);
					if(HeadHunter.getCon().getBoolean(Node.Option.ValuePlacement.CUMULATIVE))
						skullWorth += fullValue;
					else {
						state = PairState.BOUNTY;
						ecoLoss = 0;
						skullWorth = fullValue;
					}
					if(HeadFunctions.dropWith(state)) {
						offers.getConfig().set(targetUUID, null);
						offers.saveConfig();
					}
				}
			}
			String skullTag = "";
			if(skullWorth > 0)
				skullTag = HeadHunter.getCon().getString(Node.Option.Format.SKULL_VALUE);
			else
				skullTag = HeadHunter.getCon().getString(Node.Option.Format.SKULL_WORTHLESS);
			skullTag = Manager.formatBaseRoles(skullTag, victim, skullWorth);
			skullTag = Manager.formatColor(skullTag);
			loreList.add(skullTag);
			meta.setLore(loreList);
		}
		skull.setItemMeta(meta);
		return new PairedSkull(skull, ecoLoss, state);
	}
	
	/**
	 * Determines whether a skull will be dropped with the specified drop rate and configuration conditions.
	 * @param skullState - The PairState of the skull.
	 * @return True if the drop will be a success, false otherwise.
	 */
	public static boolean dropWith(PairState skullState) {
		boolean	dropWithBalance = HeadHunter.getCon().getBoolean(Node.Option.Drop.BALANCE),
				dropWithBounty = HeadHunter.getCon().getBoolean(Node.Option.Drop.BOUNTY);
		if(dropWithBalance) {
			if(dropWithBounty) {
				//If balance and bounty are both true.
				if(skullState == PairState.BALANCE || skullState == PairState.BOTH)
					return true;
			}
			else {
				//If balance is true and bounty is false.
				if(skullState == PairState.BALANCE)
					return true;
			}
		}
		else {
			if(dropWithBounty) {
				//If balance is false and bounty is true.
				if(skullState == PairState.BOUNTY)
					return true;
			}
			else {
				//If balance and bounty are both false.
				if(skullState == PairState.NEITHER)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates whether a skull will be dropped with the specified drop rate.
	 * @param dropRate - The skull drop rate defined in configuration.
	 * @return True if the drop will be a success, false otherwise.
	 */
	public static boolean luckDrop(double dropRate) {
		Random random = new Random();
		int x = random.nextInt(100) + 1;
		if(x <= dropRate)
			return true;
		return false;
	}

	/**
	 * Gets the value on a skull's lore. If the lore is missing or invalid, this method will return 0.
	 * @param skull - The ItemStack of the skull.
	 * @return The value in the skull's lore.
	 */
	public static double getValueOnSkull(ItemStack skull) {
		String loreString = (String)skull.getItemMeta().getLore().get(0);
		loreString = HeadFunctions.removeLoreFormat(loreString);
		double skullWorth = 0.0;
		if(!loreString.isEmpty() && loreString.matches("[0-9]*[.][0-9]*"))
			skullWorth = Double.parseDouble(loreString);
		return skullWorth;
	}
	
	/**
	 * Removes all characters from a string except numbers and decimals.
	 * @param s - The string to be formatted.
	 * @return The formatted string.
	 */
	public static String removeLoreFormat(String s) {
		s = s.replaceAll("[^0-9.]", "");
		return s;
	}
}
