package io.github.apickledwalrus.skriptplaceholders.placeholder;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

/**
 * A placeholder evaluator is responsible for evaluating a placeholder and returning its value.
 */
public interface PlaceholderEvaluator {

	/**
	 * A method to evaluate the value of a placeholder with the ability to provide a player as context.
	 * @param placeholder The placeholder to evaluate.
	 * @param player The player to use for evaluating this placeholder.
	 *  Can be null for placeholders that do not require such context.
	 * @return The value of the placeholder.
	 */
	@Nullable
	String evaluate(String placeholder, @Nullable OfflinePlayer player);

}
