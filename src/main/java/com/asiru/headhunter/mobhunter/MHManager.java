package com.asiru.headhunter.mobhunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;

public class MHManager {
	Random random = new Random();
	
	public static double getPrice(Entity e) {
		String path = "";
		EntityType type = getHunterType(e);
		String ent = MobHunter.typeList.get(type);
		path = ent + ".value";
		return Manager.getAccessor("mobhunter.yml").getConfig().getDouble(path);
	}
	
	public static double getDropRate(Entity e) {
		String path = "";
		EntityType type = getHunterType(e);
		String ent = MobHunter.typeList.get(type);
		path = ent + ".drop-rate";
		return Manager.getAccessor("mobhunter.yml").getConfig().getDouble(path);
	}
	
	public static ItemStack getMobHead(Entity e) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		String owner = getMobOwnerName(e);
		if(!owner.equals("UNKNOWN")) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner("MHF_" + owner);
			String name = MobHunter.mobList.get(owner);
			meta.setDisplayName("§a" + name + " Head");
			String valueTag = HeadHunter.getPlugin().getConfig().getString(Node.O_F_SKULL_VALUE);
			valueTag = valueTag.replaceAll("VALUE", Manager.formatMoney(getPrice(e)) + "");
			valueTag = Manager.formatColor(valueTag);
			List<String> lore = new ArrayList<String>();
			lore.add(valueTag);
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		else
			item.setType(Material.AIR);
		return item;
	}
	
	public static String getMobOwnerName(Entity e) {
		EntityType type = getHunterType(e);
		if(type != EntityType.UNKNOWN)
			return MobHunter.typeList.get(type);
		return "UNKNOWN";
	}
	
	public static EntityType getHunterType(Entity e) {
		EntityType type = e.getType();
		if(type == EntityType.SKELETON) {
			Skeleton skel = (Skeleton) e;
			if(skel.getSkeletonType() == SkeletonType.WITHER)
				type = EntityType.WITHER;
		}
		if(MobHunter.typeList.containsKey(type))
			return type;
		else
			return EntityType.UNKNOWN;
	}
}
