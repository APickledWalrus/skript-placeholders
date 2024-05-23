package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Value of Placeholder")
@Description("An expression to obtain the value of a placeholder from a supported plugin.")
@Examples({
	"command /ping <player>:",
		"\ttrigger:",
			"\t\tset {_ping} to placeholder \"player_ping\" from arg-1 # PlaceholderAPI",
			"\t\tset {_ping} to placeholder \"{ping}\" from arg-1 # MVdWPlaceholderAPI",
			"\t\tsend \"Ping of %arg-1%: %{_ping}%\" to player"
})
@Since("1.0, 1.2 (MVdWPlaceholderAPI support)")
public class ExprParsePlaceholder extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprParsePlaceholder.class, String.class, ExpressionType.COMBINED,
				"[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [(from|of) %-players/offlineplayers%]",
				"parse placeholder[s] %strings% [(for|as) %-players/offlineplayers%]"
		);
	}

	private Expression<String> placeholders;
	@Nullable
	private Expression<OfflinePlayer> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
		placeholders = (Expression<String>) exprs[0];
		players = (Expression<OfflinePlayer>) exprs[1];
		return true;
	}

	@Override
	protected String @NotNull [] get(@NotNull Event event) {
		List<String> values = new ArrayList<>();
		String[] placeholders = this.placeholders.getArray(event);
		OfflinePlayer[] players = this.players != null ? this.players.getArray(event) : new OfflinePlayer[]{null};
		for (OfflinePlayer player : players) {
			for (String placeholder : placeholders) {
				for (PlaceholderPlugin plugin : PlaceholderPlugin.getInstalledPlugins()) {
					String value = plugin.parsePlaceholder(placeholder, player);
					if (value != null) {
						values.add(value);
						break;
					}
				}
			}
		}
		return values.toArray(new String[0]);
	}

	@Override
	public boolean isSingle() {
		return placeholders.isSingle() && (players == null || players.isSingle());
	}

	@Override
	public @NotNull Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
		return "the value of placeholder(s) " + placeholders.toString(event, debug)
				+ (players != null ? " from " + players.toString(event, debug) : "");
	}

}
