package io.github.apickledwalrus.placeholderaddon.mvdwapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import io.github.apickledwalrus.placeholderaddon.mvdwapi.MvdwAPIEvent;

public class MvdwAPIListener {

	public static void registerPlaceholder(Plugin plugin, String placeholder) {
		PlaceholderAPI.registerPlaceholder(plugin, placeholder,
			new PlaceholderReplacer() {

				@Override
				public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
					MvdwAPIEvent e = new MvdwAPIEvent(event.getPlayer(), event.getPlaceholder());
					Bukkit.getServer().getPluginManager().callEvent(e);
					return e.getResult();
				}

			});
	}

}
