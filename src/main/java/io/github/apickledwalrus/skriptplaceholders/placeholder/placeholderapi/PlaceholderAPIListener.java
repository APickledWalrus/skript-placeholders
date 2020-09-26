package io.github.apickledwalrus.skriptplaceholders.placeholder.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;

public class PlaceholderAPIListener extends PlaceholderExpansion {

	private Plugin plugin;
	private String prefix;

	public PlaceholderAPIListener(Plugin plugin, String prefix) {
		this.plugin = plugin;
		this.prefix = prefix;
	}

	@Override
	public String getIdentifier() {
		return prefix;
	}

	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		PlaceholderEvent event = new PlaceholderEvent(this.prefix + "_" + identifier, player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getResult();
	}

}
