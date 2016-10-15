package com.asiru.headhunter.mobhunter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.asiru.headhunter.function.HeadFunctions;

public class MHListeners implements Listener {
	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		Entity ent = e.getEntity();
		Location loc = ent.getLocation();
		EntityType type = MHManager.getHunterType(ent);
		if(MobHunter.typeList.containsKey(type)) {
			if(HeadFunctions.luckDrop(MHManager.getDropRate(ent)))
				loc.getWorld().dropItem(loc, MHManager.getMobHead(ent));				
		}
	}
}
