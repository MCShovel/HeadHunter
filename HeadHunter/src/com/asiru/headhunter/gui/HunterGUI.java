package com.asiru.headhunter.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;

public class HunterGUI {
	public static boolean isEnabled() {
		return HeadHunter.getPlugin().getConfig().getBoolean(Node.O_G_ENABLED);
	}
	
	public static String getTitle() {
		String title = HeadHunter.getPlugin().getConfig().getString(Node.O_G_TITLE);
		return Manager.formatColor(title);
	}
	
	public static int getSize() {
		int size = HeadHunter.getPlugin().getConfig().getInt(Node.O_G_SIZE);
		if(size > 54)
			return 54;
		if(size <= 0)
			return 9;
		else
			return (size / 9) * 9;
	}
	
	public static int getLeft() {
		return getSize() - 2;
	}
	
	public static int getRight() {
		return getSize() - 1;
	}
	
	public static void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, getSize(), getTitle());
		inv.setItem(getLeft(), closeButton());
		inv.setItem(getRight(), sellButton(0));
		p.openInventory(inv);
	}
	
	public static boolean isHunterGUI(Inventory inv) {
		try {
			inv.getTitle(); inv.getSize(); inv.getHolder();
		} catch(Exception e) {
			return false;
		}
		if(inv.getTitle().equals(getTitle()) &&
				inv.getSize() == getSize() &&
				inv.getHolder() == null)
			return true;
		return false;
	}
	
	public static double getTotalValue(ArrayList<ItemStack> list) {
		double value = 0.0;
		for(ItemStack i : list)
			value += (HeadFunctions.getValueOnSkull(i) * i.getAmount());
		return value;
	}
	
	public static ArrayList<ItemStack> createHeadList(Inventory hunterInv) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for(ItemStack i : hunterInv.getContents()) {
			if(i != null && i.getType() == Material.SKULL_ITEM)
				list.add(i.clone());
		}
		return list;
	}
	
	public static ItemStack sellButton(double amount) {
		ItemStack i = new ItemStack(Material.EMERALD);
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		String price = HeadHunter.getPlugin().getConfig().getString(Node.O_G_TOTAL_VALUE);
		price = Manager.formatColor(price.replaceAll("VALUE", Manager.formatMoney(amount)));
		l.add(price);
		m.setLore(l);
		m.setDisplayName("§a§lSell");
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack confirmButton(double amount) {
		ItemStack i = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		String price = HeadHunter.getPlugin().getConfig().getString(Node.O_G_TOTAL_VALUE);
		price = Manager.formatColor(price.replaceAll("VALUE", Manager.formatMoney(amount)));
		l.add(price);
		l.add("§eSell these heads?");
		m.setDisplayName("§a§lConfirm");
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack cancelButton() {
		ItemStack i = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		l.add("§eGo back to head list?");
		m.setDisplayName("§c§lCancel");
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack closeButton() {
		ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		l.add("§eExit this menu?");
		m.setDisplayName("§c§lClose");
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}
}
