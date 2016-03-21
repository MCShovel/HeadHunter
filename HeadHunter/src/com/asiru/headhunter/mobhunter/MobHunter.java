package com.asiru.headhunter.mobhunter;

import java.util.LinkedHashMap;

import org.bukkit.entity.EntityType;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;

public class MobHunter {
	public static final LinkedHashMap<String, String> mobList = getTagMap();
	public static final LinkedHashMap<EntityType, String> typeList = getTypeMap();
	
	public static void refreshDefaults() {
		String pricePath, dropRatePath;
		ConfigAccessor mobhunter = Manager.getAccessor("mobhunter.yml");
		for(String s : mobList.keySet()) {
			pricePath = s + ".value";
			dropRatePath = s + ".drop-rate";
			if(!mobhunter.getConfig().contains(pricePath))
				mobhunter.getConfig().set(pricePath, 20.0);
			if(!mobhunter.getConfig().contains(dropRatePath))
				mobhunter.getConfig().set(dropRatePath, 100.0);
		}
		mobhunter.saveConfig();
	}
	
	public static LinkedHashMap<String, String> getTagMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("Blaze", "Blaze");
		map.put("CaveSpider", "Cave Spider");
		map.put("Chicken", "Chicken");
		map.put("Cow", "Cow");
		map.put("Creeper", "Creeper");
		map.put("Enderman", "Enderman");
		map.put("Ghast", "Ghast");
		map.put("Golem", "Iron Golem");
		map.put("LavaSlime", "Magma Cube");
		map.put("MushroomCow", "Mooshroom");
		map.put("Ocelot", "Ocelot");
		map.put("Pig", "Pig");
		map.put("PigZombie", "Zombie Pigman");
		map.put("Sheep", "Sheep");
		map.put("Skeleton", "Skeleton");
		map.put("Slime", "Slime");
		map.put("Spider", "Spider");
		map.put("Squid", "Squid");
		map.put("Villager", "Villager");
		map.put("WSkeleton", "Wither Skeleton");
		map.put("Zombie", "Zombie");
		return map;
	}
	
	public static LinkedHashMap<EntityType, String> getTypeMap() {
		LinkedHashMap<EntityType, String> map = new LinkedHashMap<EntityType, String>();
		map.put(EntityType.BLAZE, "Blaze");
		map.put(EntityType.CAVE_SPIDER, "CaveSpider");
		map.put(EntityType.CHICKEN, "Chicken");
		map.put(EntityType.COW, "Cow");
		map.put(EntityType.CREEPER, "Creeper");
		map.put(EntityType.ENDERMAN, "Enderman");
		map.put(EntityType.GHAST, "Ghast");
		map.put(EntityType.IRON_GOLEM, "Golem");
		map.put(EntityType.MAGMA_CUBE, "LavaSlime");
		map.put(EntityType.MUSHROOM_COW, "MushroomCow");
		map.put(EntityType.OCELOT, "Ocelot");
		map.put(EntityType.PIG, "Pig");
		map.put(EntityType.PIG_ZOMBIE, "PigZombie");
		map.put(EntityType.SHEEP, "Sheep");
		map.put(EntityType.SKELETON, "Skeleton");
		map.put(EntityType.SLIME, "Slime");
		map.put(EntityType.SPIDER, "Spider");
		map.put(EntityType.SQUID, "Squid");
		map.put(EntityType.VILLAGER, "Villager");
		map.put(EntityType.WITHER, "WSkeleton");
		map.put(EntityType.ZOMBIE, "Zombie");
		return map;
	}
}