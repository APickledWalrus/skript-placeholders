package io.github.apickledwalrus.skriptplaceholders.placeholder.mvdwplaceholderapi;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MVdWPlaceholderAPIListener {

	private final Plugin plugin;
	private final String placeholder;

	public MVdWPlaceholderAPIListener(Plugin plugin, String placeholder) {
		this.plugin = plugin;
		this.placeholder = placeholder;
	}

	public void register() {
		PlaceholderAPI.registerPlaceholder(plugin, placeholder,
				e -> {
					PlaceholderEvent event = new PlaceholderEvent(placeholder, e.getPlayer());
					Bukkit.getPluginManager().callEvent(event);
					return event.getResult();
				}
		);
	}

}
