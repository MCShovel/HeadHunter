package com.asiru.headhunter.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.asiru.headhunter.mobhunter.MobHunter;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;

public class SkullListeners implements Listener {
	@EventHandler
	public void onSkullPlace(BlockPlaceEvent e) {
		Block block = e.getBlockPlaced();
		if(block.getState() instanceof Skull) {
			ConfigAccessor skullAcc = Manager.getAccessor("skulls.yml");
			SkullMeta sm = (SkullMeta) e.getItemInHand().getItemMeta();
			if(sm != null && sm.hasLore()) {
				Location loc = block.getLocation();
				String setPath = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
				String lore = "" + sm.getLore().get(0) + "";
				String owner = sm.getOwner();
				skullAcc.getConfig().set(setPath + ".lore", lore);
				skullAcc.getConfig().set(setPath + ".owner", owner);
				skullAcc.saveConfig();
			}
		}
	}
	
	@EventHandler
	public void onSkullBreak(BlockBreakEvent e) {
		Block block = e.getBlock();
		if(block.getState() instanceof Skull) {
			ConfigAccessor skullAcc = Manager.getAccessor("skulls.yml");
			Location loc = block.getLocation();
			String getPath = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
			if(skullAcc.getConfig().contains(getPath)) {
				e.setCancelled(true);
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				String setLore = skullAcc.getConfig().getString(getPath + ".lore");
				setLore = Manager.formatColor(setLore, "\\" + "xa7");
				String setOwner = skullAcc.getConfig().getString(getPath + ".owner");
				if(setOwner.startsWith("MHF_"))
					sm.setDisplayName("§a" + MobHunter.getTagMap().get(setOwner.substring(4)) + " Head");
				List<String> lores = new ArrayList<String>();
				lores.add(0, setLore);
				sm.setLore(lores);
				sm.setOwner(setOwner);
				skull.setItemMeta(sm);
				loc.getWorld().dropItem(loc, skull);
				block.setType(Material.AIR);
				skullAcc.getConfig().set(getPath, null);
				skullAcc.saveConfig();
			}
		}
	}
}
