package io.github.apickledwalrus.skriptplaceholders.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * A placeholder listener for placeholders created using {@link PlaceholderPlugin#PLACEHOLDER_API}.
 */
public class PlaceholderAPIListener extends PlaceholderExpansion implements Relational, PlaceholderListener {

	private final Plugin plugin;
	private final String prefix;

	private final Set<PlaceholderEvaluator> evaluators = new HashSet<>();

	public PlaceholderAPIListener(Plugin plugin, String prefix) {
		this.plugin = plugin;
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
		for (PlaceholderEvaluator evaluator : evaluators) {
			String result = evaluator.evaluate(this.prefix + "_" + identifier, player);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public @Nullable String onPlaceholderRequest(Player one, Player two, String identifier) {
		for (PlaceholderEvaluator evaluator : evaluators) {
			String result = evaluator.evaluateRelational(this.prefix + "_" + identifier, one, two);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public void registerListener() {
		register();
	}

	@Override
	public void unregisterListener() {
		unregister();
	}

	@Override
	public void addEvaluator(PlaceholderEvaluator evaluator) {
		evaluators.add(evaluator);
	}

	@Override
	public void removeEvaluator(PlaceholderEvaluator evaluator) {
		evaluators.remove(evaluator);
	}

	@Override
	public boolean hasEvaluators() {
		return !evaluators.isEmpty();
	}

}
