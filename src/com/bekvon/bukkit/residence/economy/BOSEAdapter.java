package com.bekvon.bukkit.residence.economy;

import com.bekvon.bukkit.residence.Residence;

import cosine.boseconomy.BOSEconomy;

public class BOSEAdapter implements EconomyInterface {

    BOSEconomy plugin;

    public BOSEAdapter(BOSEconomy p) {
	plugin = p;
	String serverland = Residence.getInstance().getServerLandname();
	if (!plugin.playerRegistered(serverland, false)) {
	    plugin.registerPlayer(serverland);
	}
    }

    @Override
    public double getBalance(String playerName) {
	return plugin.getPlayerMoneyDouble(playerName);
    }

    @Override
    public boolean canAfford(String playerName, double amount) {
	double balance = plugin.getPlayerMoneyDouble(playerName);
	if (balance >= amount) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean add(String playerName, double amount) {
	return plugin.setPlayerMoney(playerName, plugin.getPlayerMoneyDouble(playerName) + amount, false);
    }

    @Override
    public boolean subtract(String playerName, double amount) {
	if (canAfford(playerName, amount)) {
	    return plugin.setPlayerMoney(playerName, plugin.getPlayerMoneyDouble(playerName) - amount, false);
	}
	return false;
    }

    @Override
    public boolean transfer(String playerFrom, String playerTo, double amount) {
	if (canAfford(playerFrom, amount)) {
	    if (!subtract(playerFrom, amount)) {
		return false;
	    }
	    if (!add(playerTo, amount)) {
		add(playerFrom, amount);
		return false;
	    }
	    return true;
	}
	return false;
    }

    @Override
    public String getName() {
	return "BOSEconomy";
    }

    @Override
    public String format(double amount) {
	return plugin.getMoneyFormatted(amount);
    }

}