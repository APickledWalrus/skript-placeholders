package io.github.apickledwalrus.skriptplaceholders.skript.util;

import org.bukkit.OfflinePlayer;

import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class PlaceholderUtils {

	/**
	 * Formats a placeholder with percentage signs. Examples:
	 * '%player_name' -> '%player_name%'
	 * 'player_name%' -> '%player_name%'
	 * 'player_name' -> '%player_name%'
	 * 
	 * @param placeholder The placeholder to format
	 * @return The formatted placeholder
	 */
	public static String formatPlaceholder(String placeholder) {
		if (placeholder == null) {
			return null;
		}
		if (placeholder.charAt(0) == '%') {
			placeholder = placeholder.substring(1);
		}
		if (placeholder.charAt(placeholder.length() - 1) == '%') {
			placeholder = placeholder.substring(0, placeholder.length() - 1);
		}
		return "%" + placeholder + "%";
	}

	/**
	 * @param placeholder The placeholder to get the value of
	 * @param player The player to get the placeholder from. Can be null in some cases (e.g. PlaceholderAPI)
	 * @return The value of the placeholder for the given player (if one is given)
	 */
	public static String getPlaceholder(String placeholder, OfflinePlayer player, boolean colorize) {
		String value = "";

		if (SkriptPlaceholders.hasPapi()) {
			placeholder = formatPlaceholder(placeholder);
			if (PlaceholderAPI.containsPlaceholders(placeholder)) {
				if (player != null)
					value = PlaceholderAPI.setPlaceholders(player, placeholder);
				if (value == null || value.isEmpty() || value.equalsIgnoreCase(placeholder))
					return null;
				return colorize ? ChatColor.translateAlternateColorCodes('&', value) : value;
			}
		}

		if (SkriptPlaceholders.hasMVdW()) {
			if (placeholder.charAt(0) == '{' && placeholder.charAt(placeholder.length() - 1) == '}') {
				value = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, placeholder);
				return value.equals(placeholder) ? null : (colorize ? ChatColor.translateAlternateColorCodes('&', value) : value);
			}
		}

		return null;
	}

}
