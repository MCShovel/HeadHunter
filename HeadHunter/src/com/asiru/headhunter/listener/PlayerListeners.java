package com.asiru.headhunter.listener;

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
import com.asiru.headhunter.function.PlayerFunctions;
import com.asiru.headhunter.util.config.Node;
import com.asiru.headhunter.util.pairing.PairedSkull;

public class PlayerListeners implements Listener {
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		try {
			DamageCause cause = e.getEntity().getLastDamageCause().getCause();
			if(cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.PROJECTILE)) {
				Player victim = e.getEntity();
				if(victim.getKiller() instanceof Player && !victim.equals(victim.getKiller())) {
					double rate = HeadHunter.getPlugin().getConfig().getDouble(Node.Option.Drop.RATE);
					if(HeadFunctions.luckDrop(rate)) {
						Location eventLoc = victim.getLocation();
						World eventWorld = victim.getWorld();
						if(LocationFunctions.validLocation(victim)) {
							if(!victim.hasPermission("hunter.nodrop") || PlayerFunctions.isPlayerListed(victim)) {
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
		} catch(NullPointerException npe) {}
	} 
}
