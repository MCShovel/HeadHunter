package com.asiru.headhunter.util.pairing;

import org.bukkit.inventory.ItemStack;

public final class PairedSkull {
	private final ItemStack skull;
	private final double ecoLoss;
	private final PairState state;
	
	public PairedSkull(ItemStack skull, double ecoLoss, PairState state) {
		this.skull = skull;
		this.ecoLoss = ecoLoss;
		this.state = state;
	}
	
	public ItemStack getSkull() {
		return skull;
	}
	
	public double getEcoLoss() {
		return ecoLoss;
	}
	
	public PairState getState() {
		return state;
	}
}
