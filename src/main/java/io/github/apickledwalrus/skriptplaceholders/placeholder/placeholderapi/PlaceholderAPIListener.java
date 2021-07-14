package io.github.apickledwalrus.skriptplaceholders.placeholder.placeholderapi;

import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class PlaceholderAPIListener extends PlaceholderExpansion {

	private final Plugin plugin;
	private final String prefix;

	public PlaceholderAPIListener(Plugin plugin, String prefix) {
		this.plugin = plugin;
		this.prefix = prefix;
	}

	@Override
	@NonNull
	public String getIdentifier() {
		return prefix;
	}

	@Override
	@NonNull
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	@NonNull
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(@Nullable OfflinePlayer player, @NonNull String identifier) {
		PlaceholderEvent event = new PlaceholderEvent(this.prefix + "_" + identifier, player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getResult();
	}

}
