package com.asiru.headhunter.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.mobhunter.MobHunter;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.pairing.PairState;
import com.asiru.headhunter.util.pairing.PairedSkull;

public class HeadFunctions {
	/**
	 * Sells one of the heads that the player is holding.
	 * @param p - The player who is selling a head.
	 */
	public static void sellSkull(Player p) {
		if(!HeadHunter.getPlugin().getConfig().getBoolean(Node.O_HOARD_MODE)) {
			PlayerInventory pinv = p.getInventory();
			boolean	mainHand =	pinv.getItemInMainHand().getType() == Material.SKULL_ITEM;
			boolean	offHand =	pinv.getItemInOffHand().getType() == Material.SKULL_ITEM;
			if(mainHand || offHand) {
				ItemStack skull = ((mainHand) ? pinv.getItemInMainHand() : pinv.getItemInOffHand());
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				if(skull.getDurability() == 3) {
					if(sm.hasOwner() && sm.hasLore()) {
						String owner = null;
						String uuid = "";
						try {
							owner = sm.getOwner();
							if(owner.startsWith("MHF_")) {
								owner = owner.replaceFirst("MHF_", "");
								owner = MobHunter.mobList.get(owner).toLowerCase();
								String article = ((owner.matches("[aeiou].*")) ? "an" : "a");
								owner = article + " " + owner;
							}
							else
								uuid = Manager.getPlayerFromString(owner).getUniqueId().toString();
						} catch(NullPointerException npe) {}
						if(owner != null && !uuid.equals(p.getUniqueId().toString())) {
							double skullWorth = HeadFunctions.getValueOnSkull(skull);
							HeadHunter.getEco().depositPlayer(p, skullWorth);
							int amt = skull.getAmount();
							if(amt == 1)
								p.getInventory().remove(skull);
							else
								skull.setAmount(--amt);
							if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_M_SELL_NT)) {
								boolean pub = HeadHunter.getPlugin().getConfig().getBoolean(Node.O_M_SELL_PB);
								String notify = "";
								if(skullWorth > 0.0)
									notify = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SELL_NOTIFY);
								else {
									notify = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SELL_WORTHLESS);
									pub = false;
								}
								notify = Manager.formatRolesRaw(notify, p, owner, skullWorth);
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
			else
				p.sendMessage(Messages.NO_SKULLS);
		}
		else
			p.sendMessage(Messages.HOARD_MODE);
	}
	
	/**
	 * Finds a player's skull and adds the value of their skull to the lore.
	 * @param p - The owner of the skull to be found.
	 * @return The ItemStack of the specified player's skull.
	 */
	public static PairedSkull getPairedSkull(Player hunter, OfflinePlayer victim) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(victim.getName());
		PairState state = PairState.NEITHER;
		List<String> loreList = new ArrayList<String>();
		double	balance = HeadHunter.getEco().getBalance(victim),
				sellRate = PlayerFunctions.getSellRate(hunter),
				skullWorth = 0,
				ecoLoss = 0;
		if(!HeadHunter.getPlugin().getConfig().getBoolean(Node.O_HOARD_MODE)) {
			if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_VP_BALANCE)) {
				state = PairState.BALANCE;
				if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_USE_PERCENT))
					skullWorth = balance * (sellRate / 100.0);
				else
					skullWorth = sellRate;
				if(skullWorth > balance)
					skullWorth = balance;
				
				skullWorth = Math.round(skullWorth * 100.0) / 100.0;
				ecoLoss = skullWorth;
			}
			if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_VP_BOUNTY)) {
				if(state == PairState.BALANCE)
					state = PairState.BOTH;
				else
					state = PairState.BOUNTY;
				ConfigAccessor offers = Manager.getAccessor("offers.yml");
				String targetUUID = victim.getUniqueId().toString();
				if(offers.getConfig().contains(targetUUID)) {
					double fullValue = PlayerFunctions.getTotalBounty(targetUUID);
					if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_VP_CUMULATIVE))
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
				skullTag = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SKULL_VALUE);
			else
				skullTag = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SKULL_WORTHLESS);
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
		boolean	dropWithBalance = HeadHunter.getPlugin().getConfig().getBoolean(Node.O_D_BALANCE),
				dropWithBounty = HeadHunter.getPlugin().getConfig().getBoolean(Node.O_D_BOUNTY);
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
		double x = random.nextInt(10000);
		x /= 100.0;
		if(x < dropRate)
			return true;
		return false;
	}

	/**
	 * Gets the value on a skull's lore. 
	 * <p>
	 * If the lore is missing or invalid, this method will return 0.
	 * @param skull - The ItemStack of the skull.
	 * @return The value in the skull's lore.
	 */
	public static double getValueOnSkull(ItemStack skull) {
		String loreString = skull.getItemMeta().getLore().get(0);
		loreString = HeadFunctions.removeLoreFormat(loreString);
		double skullWorth = 0.0;
		if(!loreString.isEmpty())
			skullWorth = Double.parseDouble(loreString);
		return skullWorth;
	}
	
	/**
	 * Removes all characters from a string except numbers and decimals.
	 * <p>
	 * <i>This also works with the sellhead gui.</i>
	 * @param s - The string to be formatted.
	 * @return The formatted string.
	 */
	public static String removeLoreFormat(String s) {
		String crap = s.replaceAll("[0-9]*[,.]?[0-9]*", "");
		String notCrap = s.replaceAll("[" + crap + "]", "");
		while(notCrap.length() > 0 && !Character.isDigit(notCrap.charAt(0)))
			notCrap = notCrap.substring(1);
		while(notCrap.length() > 0 && !Character.isDigit(notCrap.charAt(notCrap.length()-1)))
			notCrap = notCrap.substring(0, notCrap.length()-1);
		return notCrap.replaceAll(",", ".");
	}
}
