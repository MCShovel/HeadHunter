package com.asiru.headhunter.util;

import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
	public static String	NO_ARGS = 				"§6/hunter help",
							NO_PERMS = 				"§cYou don't have permission!",
							PLAYERS_ONLY = 			"§cThis command is for players only!",
							WORLD_ADDED = 			"§aHeadHunter world added!",
							WORLD_REMOVED = 		"§aHeadHunter world removed!",
							SIGN_CREATE =			"§aHeadHunter sign created!",
							SIGN_BREAK =			"§aHeadHunter sign removed!",
							SIGN_UPDATE =			"§6Sign updated.",
							WORLD_EXISTS = 			"§cThat world is already a HeadHunter world!",
							WORLD_INVALID = 		"§cThat world is not a HeadHunter world!",
							HEAD_INVALID = 			"§cThis skull is invalid!",
							NO_HEADS = 				"§cYou are not holding player heads!",
							NO_SKULLS = 			"§cYou are not holding any heads!",
							NO_BOUNTIES =			"§cThere are no bounties!",
							BOUNTY_INVALID = 		"§cThat bounty does not exist!",
							BOUNTY_LOW =			"§cThat bounty is too low!",
							BOUNTY_HIGH =			"§cThat bounty is too high!",
							BOUNTY_DISABLED = 		"§cBounties are disabled!",
							BOUNTIES_INVISIBLE =	"§cBounties cannot be displayed in a list!",
							WHITELIST_EMPTY =		"§cThe whitelist is empty!",
							WHITELIST_ADDED =		"§aPlayer added to HeadHunter whitelist!",
							WHITELIST_REMOVED =		"§aPlayer removed from HeadHunter whitelist!",
							WHITELIST_A_ERR =		"§cThat player is already on the whitelist!",
							WHITELIST_R_ERR =		"§cThat player is not on the whitelist!",
							WHITELIST_ERR =			"§cWhitelist could not be changed!",
							AMOUNT_INVALID = 		"§cThat is an invalid amount!",
							AMOUNT_LOW =			"§cThat bounty is too low!",
							SELF_TARGET = 			"§cYou cannot target yourself!",
							PLAYER_INVALID =		"§cThat's not a valid player!",
							HOARD_MODE =			"§cYou cannot do that in Hoard Mode!",
							CONVERT_SUCCESS =		"§aHeadHunter has successfully converted your inventory!",
							CMD_ADD = 				"§6Usage: /hunter addworld <world>",
							CMD_REMOVE = 			"§6Usage: /hunter removeworld <world>",
							CMD_BOUNTY = 			"§6Usage: /hunter bounty <add | remove | check | list>",
							CMD_BOUNTY_CHECK = 		"§6Usage: /hunter bounty check <target>",
							CMD_BOUNTY_REMOVE = 	"§6Usage: /hunter bounty remove <target> [amount]",
							CMD_BOUNTY_ADD = 		"§6Usage: /hunter bounty add <target> <amount>",
							CMD_WHITELIST =			"§6Usage: /hunter whitelist [<add | remove> <target>]",
							CMD_CONVERT =			"§6Usage: /hunter convert <PlayerHeads>",
							CMD_CONVERT_ERR =		"§cYou cannot convert HeadHunter in Hoard Mode!",
							CMD_RELOAD_ERR =		"§6You cannot reload that file!";
	
	public static final String[] 	HELP_NORMAL = { "&6---=< &eHeadHunter Help &6>=---", 
													"&6/hunter help", 
													"   &eOpen this help message", 
													"&6/hunter sellhead", 
													"   &eSell one head from your inventory", 
													"&6/hunter bounty <add | remove | check | list>", 
													"   &eSet a bounty on a player's head" },
													
									HELP_ADMIN	= {	"&6/hunter addworld <world>",
													"   &eAdd a HeadHunter world",
													"&6/hunter removeworld <world>",
													"   &eRemove a HeadHunter world",
													"&6/hunter convert <PlayerHeads>",
													"   &eConvert to HeadHunter from a plugin",
													"&6/hunter whitelist [<add | remove> <target>]",
													"   &eChange the list of players to drop heads",
													"&6/hunter reload",
													"   &eReload the HeadHunter files"};
	
	public static void refresh() {
		ConfigAccessor messages = Manager.getAccessor("messages.yml");
		FileConfiguration msgs = messages.getConfig();
		
		NO_ARGS = 				Manager.formatColor(msgs.getString("NO_ARGS"));
		NO_PERMS = 				Manager.formatColor(msgs.getString("NO_PERMS"));
		PLAYERS_ONLY = 			Manager.formatColor(msgs.getString("PLAYERS_ONLY"));
		WORLD_ADDED = 			Manager.formatColor(msgs.getString("WORLD_ADDED"));
		WORLD_REMOVED = 		Manager.formatColor(msgs.getString("WORLD_REMOVED"));
		SIGN_CREATE =			Manager.formatColor(msgs.getString("SIGN_CREATE"));
		SIGN_BREAK =			Manager.formatColor(msgs.getString("SIGN_BREAK"));
		SIGN_UPDATE =			Manager.formatColor(msgs.getString("SIGN_UPDATE"));
		WORLD_EXISTS = 			Manager.formatColor(msgs.getString("WORLD_EXISTS"));
		WORLD_INVALID = 		Manager.formatColor(msgs.getString("WORLD_INVALID"));
		HEAD_INVALID = 			Manager.formatColor(msgs.getString("HEAD_INVALID"));
		NO_HEADS = 				Manager.formatColor(msgs.getString("NO_HEADS"));
		NO_SKULLS = 			Manager.formatColor(msgs.getString("NO_SKULLS"));
		NO_BOUNTIES =			Manager.formatColor(msgs.getString("NO_BOUNTIES"));
		BOUNTY_INVALID = 		Manager.formatColor(msgs.getString("BOUNTY_INVALID"));
		BOUNTY_LOW =			Manager.formatColor(msgs.getString("BOUNTY_LOW"));
		BOUNTY_HIGH =			Manager.formatColor(msgs.getString("BOUNTY_HIGH"));
		BOUNTY_DISABLED = 		Manager.formatColor(msgs.getString("BOUNTY_DISABLED"));
		BOUNTIES_INVISIBLE =	Manager.formatColor(msgs.getString("BOUNTIES_INVISIBLE"));
		WHITELIST_EMPTY =		Manager.formatColor(msgs.getString("WHITELIST_EMPTY"));
		WHITELIST_ADDED =		Manager.formatColor(msgs.getString("WHITELIST_ADDED"));
		WHITELIST_REMOVED =		Manager.formatColor(msgs.getString("WHITELIST_REMOVED"));
		WHITELIST_A_ERR =		Manager.formatColor(msgs.getString("WHITELIST_A_ERR"));
		WHITELIST_R_ERR =		Manager.formatColor(msgs.getString("WHITELIST_R_ERR"));
		WHITELIST_ERR =			Manager.formatColor(msgs.getString("WHITELIST_ERR"));
		AMOUNT_INVALID = 		Manager.formatColor(msgs.getString("AMOUNT_INVALID"));
		AMOUNT_LOW =			Manager.formatColor(msgs.getString("AMOUNT_LOW"));
		SELF_TARGET = 			Manager.formatColor(msgs.getString("SELF_TARGET"));
		HOARD_MODE =			Manager.formatColor(msgs.getString("HOARD_MODE"));
		CONVERT_SUCCESS =		Manager.formatColor(msgs.getString("CONVERT_SUCCESS"));
		CMD_ADD = 				Manager.formatColor(msgs.getString("CMD_ADD"));
		CMD_REMOVE = 			Manager.formatColor(msgs.getString("CMD_REMOVE"));
		CMD_BOUNTY = 			Manager.formatColor(msgs.getString("CMD_BOUNTY"));
		CMD_BOUNTY_CHECK = 		Manager.formatColor(msgs.getString("CMD_BOUNTY_CHECK"));
		CMD_BOUNTY_REMOVE = 	Manager.formatColor(msgs.getString("CMD_BOUNTY_REMOVE"));
		CMD_BOUNTY_ADD = 		Manager.formatColor(msgs.getString("CMD_BOUNTY_ADD"));
		CMD_WHITELIST =			Manager.formatColor(msgs.getString("CMD_WHITELIST"));
		CMD_CONVERT =			Manager.formatColor(msgs.getString("CMD_CONVERT"));
		CMD_CONVERT_ERR =		Manager.formatColor(msgs.getString("CMD_CONVERT_ERR"));
		CMD_RELOAD_ERR =		Manager.formatColor(msgs.getString("CMD_RELOAD_ERR"));
	}
}