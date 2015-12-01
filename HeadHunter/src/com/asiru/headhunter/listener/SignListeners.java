package com.asiru.headhunter.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import com.asiru.headhunter.HeadHunter;
import com.asiru.headhunter.function.HeadFunctions;
import com.asiru.headhunter.function.LocationFunctions;
import com.asiru.headhunter.function.PlayerFunctions;
import com.asiru.headhunter.util.ConfigAccessor;
import com.asiru.headhunter.util.Manager;
import com.asiru.headhunter.util.Messages;
import com.asiru.headhunter.util.config.Node;

public class SignListeners implements Listener {
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[SellHead]")) {
			if(Manager.hasAnyPerms(e.getPlayer(), new String[]{"hunter.sign.create", "hunter.sign"})) {
				ConfigAccessor signs = Manager.getAccessor("signs.yml");
				String tag = LocationFunctions.parseLocation(e.getBlock().getLocation());
				signs.getConfig().set(tag, e.getPlayer().getUniqueId().toString());
				signs.saveConfig();
				e.setLine(0, Manager.formatColor(HeadHunter.getPlugin().getConfig().getString(Node.Option.Format.SIGN_TOP)));
				e.setLine(1, Manager.formatColor(HeadHunter.getPlugin().getConfig().getString(Node.Option.Format.SIGN_TITLE)));
				e.setLine(2, "Click to Refresh");
				e.setLine(3, Manager.formatColor(HeadHunter.getPlugin().getConfig().getString(Node.Option.Format.SIGN_BOTTOM)));
				final Location loc = e.getBlock().getLocation();
				Bukkit.getScheduler().runTaskLater(HeadHunter.getPlugin(), new Runnable() {
					@Override
					public void run() {
						for(Player p : Bukkit.getOnlinePlayers())
							PlayerFunctions.updateSignAt(p, loc);
					}
				}, 2);
				e.getPlayer().sendMessage(Messages.SIGN_CREATE);
			}
			else {
				e.getPlayer().sendMessage(Messages.NO_PERMS);
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		if(e.getBlock().getState() instanceof Sign) {
			Location loc = e.getBlock().getState().getLocation();
			ConfigAccessor signs = Manager.getAccessor("signs.yml");
			String tag = LocationFunctions.parseLocation(loc);
			if(signs.getConfig().contains(tag)) {
				Player p = e.getPlayer();
				if(Manager.hasAnyPerms(p, new String[]{"hunter.sign.break", "hunter.sign"})
					|| p.getUniqueId().equals(UUID.fromString(signs.getConfig().getString(tag)))) {
					signs.getConfig().set(tag, null);
					signs.saveConfig();
					p.sendMessage(Messages.SIGN_BREAK);
				}
				else {
					e.setCancelled(true);
					p.sendMessage(Messages.NO_PERMS);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(e.getClickedBlock().getState() instanceof Sign) {
			Player p = e.getPlayer();
			Location loc = e.getClickedBlock().getLocation();
			ConfigAccessor signs = Manager.getAccessor("signs.yml");
			if(signs.getConfig().contains(LocationFunctions.parseLocation(loc))) {
				if(Manager.hasAnyPerms(p, new String[]{"hunter.sell", "hunter.use"}))
					HeadFunctions.sellSkull(p);
				else
					p.sendMessage(Messages.NO_PERMS);
			}
			PlayerFunctions.updateSignAt(p, loc);
			p.sendMessage(Messages.SIGN_UPDATE);
		}
	}
}
