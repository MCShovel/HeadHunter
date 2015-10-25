package com.asiru.headhunter.util;

public class Node {
	public static class Option {
		public static final String	HOARD_MODE =	"options.hoard-mode",
									SELL_RATE =		"options.sell-rate",
									USE_PERCENT =	"options.use-percentage",
									USE_PERMS =		"options.use-permissions",
									LIST_SIZE =		"options.bounty-list-size";
		
		public static class Drop {
			public static final String	RATE =		"options.drop.rate",
										BALANCE =	"options.drop.with-balance",
										BOUNTY =	"options.drop.with-bounty";
		}
		
		public static class Message {
			public static final String	SELL_NT =	"options.message.sell-notify",
										SELL_PB =	"options.message.sell-public",
										BOUNTY_NT =	"options.message.bounty-notify",
										BOUNTY_PB =	"options.message.bounty-public";
		}
		
		public static class ValuePlacement {
			public static final String	BALANCE =		"options.value-placement.balance",
										BOUNTY =		"options.value-placement.bounty",
										CUMULATIVE =	"options.value-placement.cumulative";
		}
		
		public static class Format {
			public static final String	SIGN =				"options.format.sign",
										SIGN_VALUE =		"options.format.sign-value",
										SKULL_VALUE =		"options.format.skull-value",
										SKULL_WORTHLESS =	"options.format.skull-worthless",
										SELL_NOTIFY =		"options.format.sell-notify",
										SELL_WORTHLESS =	"options.format.sell-worthless",
										BOUNTY_PLACE =		"options.format.place-bounty",
										BOUNTY_REMOVE = 	"options.format.remove-bounty",
										BOUNTY_TOTAL =		"options.format.total-bounty",
										BOUNTY_PERSONAL =	"options.format.personal-bounty";
		}
	}
	
	public static class World {
		public static final String	IGNORE_WORLDS =	"worlds.ignore-worlds",
									VALID_WORLDS =	"worlds.valid-worlds";
	}
	
	public static class PluginSupport {
		public static final String	FACTIONS_WILDERNESS =	"plugin-support.factions.enabled.wilderness",
									FACTIONS_WARZONE =		"plugin-support.factions.enabled.warzone",
									FACTIONS_SAFEZONE =		"plugin-support.factions.enabled.safezone";
	}
}
