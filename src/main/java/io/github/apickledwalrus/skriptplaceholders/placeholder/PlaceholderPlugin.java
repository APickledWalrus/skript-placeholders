package io.github.apickledwalrus.skriptplaceholders.placeholder;

import ch.njol.skript.Skript;
import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A utility enum for the placeholder plugins.
 */
public enum PlaceholderPlugin {

	PLACEHOLDER_API("PlaceholderAPI", Skript.classExists("me.clip.placeholderapi.expansion.PlaceholderExpansion")) {
		private final char[] illegalCharacters = new char[]{'%', '{', '}', '_'};

		@Override
		@Nullable
		public String isValidPrefix(String prefix) {
			if (StringUtils.isBlank(prefix)) {
				return "A prefix cannot be blank";
			}
			for (char character : prefix.toCharArray()) {
				for (char illegalCharacter : illegalCharacters) {
					if (character == illegalCharacter) {
						return getDisplayName() + " prefixes cannot contain the character '" + character + "'";
					}
				}
			}
			return null;
		}

		@Override
		public void registerPlaceholder(String placeholder) {
			new PlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
		}

		@Override
		@Nullable
		public String parsePlaceholder(String placeholder, @Nullable OfflinePlayer player) {
			if (placeholder.indexOf('%') == -1) // Try to add percentage signs manually
				placeholder = "%" + placeholder + "%";
			String value = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, placeholder);
			if (value.isEmpty() || value.equalsIgnoreCase(placeholder))
				return null;
			return value;
		}
	},
	MVDW_PLACEHOLDER_API("MVdWPlaceholderAPI", Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI")) {
		@Override
		@Nullable
		public String isValidPrefix(String prefix) {
			return StringUtils.isBlank(prefix) ? "A placeholder cannot be blank" : null;
		}

		@Override
		public void registerPlaceholder(String placeholder) {
			new MVdWPlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
		}

		@Override
		@Nullable
		public String parsePlaceholder(String placeholder, @Nullable OfflinePlayer player) {
			if (placeholder.charAt(0) == '{' && placeholder.charAt(placeholder.length() - 1) == '}') {
				String value = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, placeholder);
				if (value.isEmpty() || value.equalsIgnoreCase(placeholder))
					return null;
				return value;
			}
			return null;
		}
	};

	private final String displayName;
	private final boolean installed;

	PlaceholderPlugin(String displayName, boolean installed) {
		this.displayName = displayName;
		this.installed = installed;
	}

	/**
	 * @return A display name representing the placeholder plugin.
	 */
	public final String getDisplayName() {
		return displayName;
	}

	/**
	 * @return Whether the placeholder plugin is installed on the server.
	 */
	public final boolean isInstalled() {
		return installed;
	}

	/**
	 * @param prefix The prefix to validate.
	 * @return Null, or an error message detailing why the prefix is invalid.
	 */
	@Nullable
	public abstract String isValidPrefix(String prefix);

	/**
	 * Registers a new placeholder with this plugin.
	 * @param placeholder The name of the placeholder to register.
	 */
	public abstract void registerPlaceholder(String placeholder);

	/**
	 * @param placeholder The placeholder to obtain the value of.
	 * @param player The player to obtain the placeholder from. For some implementations, a player is not required.
	 * @return The value of the placeholder for the given player (if provided).
	 */
	@Nullable
	public abstract String parsePlaceholder(String placeholder, @Nullable OfflinePlayer player);

}
