package io.github.apickledwalrus.skriptplaceholders.placeholder.mvdwplaceholderapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;

public class MVdWPlaceholderAPIListener {

	private Plugin plugin;
	private String placeholder;

	public MVdWPlaceholderAPIListener(Plugin plugin, String placeholder) {
		this.plugin = plugin;
		this.placeholder = placeholder;
	}

	public void register() {
		PlaceholderAPI.registerPlaceholder(plugin, placeholder,
			new PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
					PlaceholderEvent event = new PlaceholderEvent(placeholder, e.getPlayer());
					Bukkit.getPluginManager().callEvent(event);
					return event.getResult();
				}
			}
		);
	}

}
