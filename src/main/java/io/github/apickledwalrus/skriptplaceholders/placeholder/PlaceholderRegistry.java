package io.github.apickledwalrus.skriptplaceholders.placeholder;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderRegistry {

	private final Plugin plugin;
	private final Map<PlaceholderPlugin, PlaceholderRegister> registers = new HashMap<>();

	public PlaceholderRegistry(Plugin plugin) {
		this.plugin = plugin;
	}

	public void registerPlaceholder(PlaceholderPlugin plugin, String placeholder, PlaceholderEvaluator evaluator) {
		PlaceholderRegister register = registers.computeIfAbsent(plugin, PlaceholderRegister::new);
		register.registerPlaceholder(placeholder, evaluator);
	}

	public void unregisterPlaceholder(PlaceholderPlugin plugin, String placeholder, PlaceholderEvaluator evaluator) {
		PlaceholderRegister register = registers.get(plugin);
		if (register != null) {
			register.unregisterPlaceholder(placeholder, evaluator);
		}
	}

	private final class PlaceholderRegister {

		private final PlaceholderPlugin plugin;
		private final Map<String, PlaceholderListener> listeners = new HashMap<>();

		public PlaceholderRegister(PlaceholderPlugin plugin) {
			this.plugin = plugin;
		}

		public void registerPlaceholder(String placeholder, PlaceholderEvaluator evaluator) {
			PlaceholderListener listener = listeners.computeIfAbsent(placeholder,
					key -> plugin.registerPlaceholder(PlaceholderRegistry.this.plugin, key)
			);
			listener.addEvaluator(evaluator);
		}

		public void unregisterPlaceholder(String placeholder, PlaceholderEvaluator evaluator) {
			PlaceholderListener listener = listeners.get(placeholder);
			if (listener == null) {
				return;
			}
			listener.removeEvaluator(evaluator);
			if (!listener.hasEvaluators()) { // if this was the last evaluator, unregister the listener
				listener.unregisterListener();
				listeners.remove(placeholder);
			}
		}

	}

}
