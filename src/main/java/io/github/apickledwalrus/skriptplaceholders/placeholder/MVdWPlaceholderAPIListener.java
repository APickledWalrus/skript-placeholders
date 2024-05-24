package io.github.apickledwalrus.skriptplaceholders.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

/**
 * A placeholder listener for placeholders created using {@link PlaceholderPlugin#MVDW_PLACEHOLDER_API}.
 */
public class MVdWPlaceholderAPIListener implements PlaceholderListener {

	private final Plugin plugin;
	private final String placeholder;

	private final Set<PlaceholderEvaluator> evaluators = new HashSet<>();
	private boolean isInvalid;

	public MVdWPlaceholderAPIListener(Plugin plugin, String placeholder) {
		this.plugin = plugin;
		this.placeholder = placeholder;
	}

	@Override
	public void registerListener() {
		PlaceholderAPI.registerPlaceholder(plugin, placeholder, event -> {
			if (isInvalid) { // true when the listener has been unregistered
				return null;
			}
			OfflinePlayer player = event.getPlayer();
			if (player == null) { // this is for an actual offline player
				player = event.getOfflinePlayer();
			}
			for (PlaceholderEvaluator evaluator : evaluators) {
				String result = evaluator.evaluate(placeholder, player);;
				if (result != null) {
					return result;
				}
			}
			return null;
		});
	}

	@Override
	public void unregisterListener() {
		isInvalid = true;
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
