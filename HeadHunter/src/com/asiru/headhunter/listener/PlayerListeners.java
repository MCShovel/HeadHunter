package com.asiru.headhunter.listener;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.function.LocationFunctions;
import com.asiru.headhunter.util.Node;
import com.asiru.headhunter.util.pairing.PairedSkull;

public class PlayerListeners implements Listener {
	Random random = new Random();
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		DamageCause cause = e.getEntity().getLastDamageCause().getCause();
		if(cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.PROJECTILE)) {
			Player victim = e.getEntity();
			if(victim.getKiller() instanceof Player && !victim.equals(victim.getKiller())) {
				double rate = HeadHunter.getPlugin().getConfig().getDouble(Node.Option.Drop.RATE);
				if(HeadFunctions.luckDrop(rate)) {
					Location eventLoc = victim.getLocation();
					World eventWorld = victim.getWorld();
					if(LocationFunctions.validLocation(victim) && !victim.hasPermission("hunter.nodrop")) {
						PairedSkull pair = HeadFunctions.getPairedSkull(victim.getKiller(), victim);
						if(HeadFunctions.dropWith(pair.getState())) {
							HeadHunter.getEco().withdrawPlayer(victim, pair.getEcoLoss());
							eventWorld.dropItem(eventLoc, pair.getSkull());
						}
					}
				}
			}
		}
	}
}
