package com.asiru.headhunter.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;

public class GUIListeners implements Listener {
	/*
	 * NOTE: The getCursor() and getCurrentItem() methods 
	 * return values from BEFORE the ItemStacks are changed.
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(HunterGUI.isHunterGUI(e.getView().getTopInventory())) {
			int sellingSlot = HunterGUI.getRight();
			Inventory hunterInv = e.getView().getTopInventory(), clickdInv = e.getClickedInventory();
			ItemStack cursor = e.getCursor(), current = e.getCurrentItem();
			ItemStack	leftSlotItem = hunterInv.getItem(HunterGUI.getLeft()),
						rightSlotItem = hunterInv.getItem(HunterGUI.getRight());
			HumanEntity p = e.getWhoClicked();
			InventoryAction action = e.getAction();
			ArrayList<ItemStack> iList = HunterGUI.createHeadList(hunterInv);
			//If the player clicked on the HunterGUI:
			if(clickdInv.equals(hunterInv)) {
				Material mat = current.getType(), cmat = cursor.getType();
				if(cmat != Material.AIR && cmat != Material.SKULL_ITEM)
					e.setCancelled(true);
				if(leftSlotItem != null && leftSlotItem.getType() == Material.EMERALD_BLOCK) {
					if(mat != Material.EMERALD_BLOCK && mat != Material.REDSTONE_BLOCK) {
						e.setCancelled(true);
						return;
					}
				}
				double value = 0.0;
				if(cmat == Material.SKULL_ITEM)
					iList.add(cursor);
				if(mat == Material.EMERALD) {
					//If the player clicked the sell button:
					value = HeadFunctions.getValueOnSkull(rightSlotItem);
					hunterInv.setItem(HunterGUI.getLeft(), HunterGUI.confirmButton(value));
					hunterInv.setItem(HunterGUI.getRight(), HunterGUI.cancelButton());
					sellingSlot = HunterGUI.getLeft();
					e.setCancelled(true);
				}
				else if(mat == Material.STAINED_GLASS_PANE) {
					//If the player clicked the close button:
					p.closeInventory();
					e.setCancelled(true);
					return;
				}
				else if(mat == Material.EMERALD_BLOCK) {
					//If the player clicked the confirm sell button:
					value = HeadFunctions.getValueOnSkull(leftSlotItem);
					HeadHunter.getEco().depositPlayer(Manager.getPlayerFromString(p.getName()), value);
					//TODO: HunterGUI sell message
					hunterInv.clear();
					p.closeInventory();
					int amt = 0;
					for(ItemStack i : iList)
						amt += i.getAmount();
					String msg = HeadHunter.getPlugin().getConfig().getString(Node.O_G_SELL_MESSAGE);
					msg = msg.replaceAll("VALUE", Manager.formatMoney(value));
					msg = msg.replaceAll("HUNTER", p.getName());
					msg = msg.replaceAll("AMOUNT", amt + "");
					msg = Manager.formatColor(msg);
					boolean nt = HeadHunter.getPlugin().getConfig().getBoolean(Node.O_M_SELL_NT),
							pb = HeadHunter.getPlugin().getConfig().getBoolean(Node.O_M_SELL_PB);
					if(nt && amt > 0) {
						if(pb)
							p.sendMessage(msg);
						else
							Bukkit.broadcastMessage(msg);
					}
					e.setCancelled(true);
					return;
				}
				else if(mat == Material.REDSTONE_BLOCK) {
					//If the player clicked the cancel sell button:
					value = HeadFunctions.getValueOnSkull(leftSlotItem);
					hunterInv.setItem(HunterGUI.getLeft(), HunterGUI.closeButton());
					hunterInv.setItem(HunterGUI.getRight(), HunterGUI.sellButton(value));
					sellingSlot = HunterGUI.getRight();
					e.setCancelled(true);
				}
			}
			//If the player clicked on their own inventory:
			else {
				if(action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
					if(current.getType() != Material.SKULL_ITEM) {
						e.setCancelled(true);
						return;
					}
				}
			}
			double total = HunterGUI.getTotalValue(iList);
			if(sellingSlot == HunterGUI.getLeft()) {
				hunterInv.setItem(HunterGUI.getLeft(), HunterGUI.confirmButton(total));
				hunterInv.setItem(HunterGUI.getRight(), HunterGUI.cancelButton());
			}
			else {
				hunterInv.setItem(HunterGUI.getLeft(), HunterGUI.closeButton());
				hunterInv.setItem(HunterGUI.getRight(), HunterGUI.sellButton(total));
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(HunterGUI.isHunterGUI(e.getInventory())) {
			Inventory hunterInv = e.getInventory();
			HumanEntity p = e.getPlayer();
			for(ItemStack i : hunterInv.getContents()) {
				if(i != null && i.getType() == Material.SKULL_ITEM) {
					if(p.getInventory().firstEmpty() != -1)
						p.getInventory().addItem(i);
					else
						p.getWorld().dropItem(p.getLocation(), i);
				}
			}
		}
	}
}
