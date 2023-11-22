package io.github.apickledwalrus.skriptplaceholders.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class PlaceholderAPIListener extends PlaceholderExpansion implements PlaceholderListener {

	private final Plugin plugin;
	private final PlaceholderEvaluator evaluator;
	private final String prefix;

	public PlaceholderAPIListener(Plugin plugin, PlaceholderEvaluator evaluator, String prefix) {
		this.plugin = plugin;
		this.evaluator = evaluator;
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
	@Nullable
	public String onRequest(@Nullable OfflinePlayer player, @NonNull String identifier) {
		return evaluator.evaluate(this.prefix + "_" + identifier, player);
	}

	@Override
	public void registerListener() {
		register();
	}

	@Override
	public void unregisterListener() {
		unregister();
	}

}
