package io.github.apickledwalrus.skriptplaceholders.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A placeholder listener for placeholders created using {@link PlaceholderPlugin#PLACEHOLDER_API}.
 */
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
	public @NotNull String getIdentifier() {
		return prefix;
	}

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String identifier) {
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
