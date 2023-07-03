package io.github.apickledwalrus.skriptplaceholders.placeholder;

import ch.njol.skript.Skript;
import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A utility enum for the placeholder plugins.
 */
public enum PlaceholderPlugin {

	PLACEHOLDER_API("PlaceholderAPI", Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI")) {

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

	},
	MVDW_PLACEHOLDER_API("MVdWPlaceholderAPI", Skript.classExists("me.clip.placeholderapi.expansion.PlaceholderExpansion")) {

		@Override
		@Nullable
		public String isValidPrefix(String prefix) {
			return StringUtils.isBlank(prefix) ? "A placeholder cannot be blank" : null;
		}

		@Override
		public void registerPlaceholder(String placeholder) {
			new MVdWPlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
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
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return Whether the placeholder plugin is installed on the server.
	 */
	public boolean isInstalled() {
		return installed;
	}

	/**
	 * @param prefix The prefix to validate.
	 * @return Null, or an error message detailing why the prefix is invalid.
	 */
	@Nullable
	public abstract String isValidPrefix(String prefix);

	public abstract void registerPlaceholder(String placeholder);

}
