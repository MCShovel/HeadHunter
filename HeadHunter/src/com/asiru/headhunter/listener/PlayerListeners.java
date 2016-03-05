package com.asiru.headhunter.listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.config.Node;

public class PlayerListeners implements Listener {
	public static HashMap<String, String> FIRE_TICKS = new HashMap<String, String>();
	// public static HashMap<String, String> LAST_DAMAGE = new HashMap<String, String>();
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		try {
			Player victim = e.getEntity();
			String key = victim.getUniqueId().toString();
			DamageCause cause = e.getEntity().getLastDamageCause().getCause();
			if(cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.PROJECTILE) {
				if(victim.getKiller() instanceof Player && !victim.equals(victim.getKiller()))
					Manager.doThings(victim, victim.getKiller());
			}
			else if(cause == DamageCause.FIRE_TICK) {
				boolean firetick = HeadHunter.getPlugin().getConfig().getBoolean(Node.O_D_FIRE_TICK);
				System.out.println(firetick);
				if(firetick && FIRE_TICKS.containsKey(key)) {
					String uuid = FIRE_TICKS.get(key);
					if(uuid != null) {
						Player killer = Bukkit.getPlayer(UUID.fromString(uuid));
						Manager.doThings(victim, killer);
					}
				}
			}
			else if(HeadHunter.getPlugin().getConfig().getBoolean(Node.O_D_ANY_CAUSE))
				Manager.doThings(victim, null);
			FIRE_TICKS.remove(key);
		} catch(NullPointerException npe) {}
	}
	
	@EventHandler
	public void onEntityCombustByEntity(EntityCombustByEntityEvent e) {
		Entity	combuster = e.getCombuster(), 
				combustee = e.getEntity();
		if(combuster instanceof Player && combustee instanceof Player) {
			Player	er = (Player) combuster,
					ee = (Player) combustee;
			FIRE_TICKS.put(ee.getUniqueId().toString(), er.getUniqueId().toString());
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		// CombatTag ctp = (CombatTag) Bukkit.getPluginManager().getPlugin("CombatTag");
		Entity ent = e.getEntity();
		if(ent instanceof Player /*|| ctp.npcm.isNPC(ent)*/) {
			final Player p = (Player) ent;
			if(e.getCause().equals(DamageCause.FIRE_TICK)) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(HeadHunter.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if(p.getFireTicks() == 0) {
							FIRE_TICKS.remove(p.getUniqueId().toString());
							return;
						}
						if(p.getFireTicks() < 20) {
							FIRE_TICKS.remove(p.getUniqueId().toString());
						}
					}
				}, 20);
			}
		}
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(e.getProjectile() instanceof Arrow) {
				Arrow a = (Arrow) e.getProjectile();
				ItemStack b = e.getBow();
				if(b.getEnchantmentLevel(Enchantment.ARROW_FIRE) > 0) {
					FixedMetadataValue	fmv1 = new FixedMetadataValue(HeadHunter.getPlugin(), true),
										fmv2 = new FixedMetadataValue(HeadHunter.getPlugin(), 
												p.getUniqueId().toString());
					a.setMetadata("flame", fmv1);
					a.setMetadata("shooter", fmv2);
					e.setProjectile(a);
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		// CombatTag ctp = (CombatTag) Bukkit.getPluginManager().getPlugin("CombatTag");
		Entity ent = e.getEntity();
		String hitID = "";
		
		if(ent instanceof Player)
			hitID = ((Player) ent).getUniqueId().toString();
		/*
		else if(ctp != null && ctp.npcm.isNPC(ent))
			hitID = ctp.npcm.getNPCIdFromEntity(ent).toString();
		*/
		if(e.getDamager() instanceof Arrow) {
			Arrow a = (Arrow) e.getDamager();
			if(a.getShooter() instanceof Player) {
				List<MetadataValue>	flameVal = a.getMetadata("flame"),
									shootVal = a.getMetadata("shooter");
				if(!flameVal.isEmpty() && !shootVal.isEmpty()) {
					if(flameVal.get(0).asBoolean()) {
						String	shotID = shootVal.get(0).asString();
						FIRE_TICKS.put(hitID, shotID);
					}
				}
			}
		}
		/*
		else if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
		}
		*/
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		/*
		 * TODO Combat Log Quit
		 * When the player leaves, check the combat log plugins to see if the player was in combat.
		 * If they player was in combat, add them to the LEFT_IN_COMBAT ArrayList.
		 * Drop the player's head with money anyway.
		if(!HeadHunter.combatLogString.equals("")) {
			Plugin clp = Bukkit.getPluginManager().getPlugin(HeadHunter.combatLogString);
			Player victim = e.getPlayer();
			if(HeadHunter.combatLogString.equals("CombatLog")) {
				CombatLog plugin = (CombatLog) clp;
				if(plugin.taggedPlayers.containsKey(victim.getName())) {
					if(HeadHunter.getPlugin().getConfig().getBoolean(Node.PluginSupport.COMBATLOG_ON_LOGOUT)) {
						long victimTime = plugin.taggedPlayers.get(victim.getName());
						for(Entry<String, Long> entry : plugin.taggedPlayers.entrySet()) {
							if(entry.getValue() == victimTime) {
								Player hunter = Manager.getPlayerFromString(entry.getKey());
								Manager.doThings(victim, hunter);
								break;
							}
						}
					}
				}
			}
			else if(HeadHunter.combatLogString.equals("CombatTagPlus")) {
				
			}
			else if(HeadHunter.combatLogString.equals("PvPManager")) {
				
			}
		}
		*/
	}
}
