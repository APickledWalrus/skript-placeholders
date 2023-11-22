package io.github.apickledwalrus.skriptplaceholders.placeholder;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class MVdWPlaceholderAPIListener implements PlaceholderListener {

	private final Plugin plugin;
	private final PlaceholderEvaluator evaluator;
	private final String placeholder;

	public MVdWPlaceholderAPIListener(Plugin plugin, PlaceholderEvaluator evaluator, String placeholder) {
		this.plugin = plugin;
		this.evaluator = evaluator;
		this.placeholder = placeholder;
	}

	@Override
	public void registerListener() {
		PlaceholderAPI.registerPlaceholder(plugin, placeholder, event -> {
			OfflinePlayer player = event.getPlayer();
			if (player == null) { // this is for an actual offline player
				player = event.getOfflinePlayer();
			}
			return evaluator.evaluate(placeholder, player);
		});
	}

	@Override
	public void unregisterListener() {
		// TODO determine if this is possible
	}

}
