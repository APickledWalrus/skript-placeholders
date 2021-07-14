package io.github.apickledwalrus.skriptplaceholders.skript.util;

import org.bukkit.OfflinePlayer;

import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.eclipse.jdt.annotation.Nullable;

public class PlaceholderUtils {

	/**
	 * @param placeholder The placeholder to get the value of
	 * @param player The player to get the placeholder from. Can be null in some cases (e.g. PlaceholderAPI)
	 * @return The value of the placeholder for the given player (if one is given)
	 */
	@Nullable
	public static String getPlaceholder(String placeholder, @Nullable OfflinePlayer player, boolean colorize) {
		String value;

		if (SkriptPlaceholders.hasMVdW()) {
			if (placeholder.charAt(0) == '{' && placeholder.charAt(placeholder.length() - 1) == '}') {
				value = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, placeholder);
				return value.equals(placeholder) ? null : (colorize ? ChatColor.translateAlternateColorCodes('&', value) : value);
			}
		}

		if (SkriptPlaceholders.hasPapi()) {
			if (placeholder.indexOf('%') == -1) // Try to add percentage signs manually
				placeholder = "%" + placeholder + "%";
			value = PlaceholderAPI.setPlaceholders(player, placeholder);
			if (value.isEmpty() || value.equalsIgnoreCase(placeholder))
				return null;
			return colorize ? ChatColor.translateAlternateColorCodes('&', value) : value;
		}

		return null;
	}

}
