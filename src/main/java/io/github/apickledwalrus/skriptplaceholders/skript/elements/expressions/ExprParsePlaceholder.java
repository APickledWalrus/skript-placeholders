package io.github.apickledwalrus.skriptplaceholders.skript.elements.expressions;

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

import io.github.apickledwalrus.skriptplaceholders.skript.util.PlaceholderUtils;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Value of Placeholder")
@Description({"Returns the value of a PlaceholderAPI/MVdWPlaceholderAPI placeholder.",
				"Note: The 'without color' option is only applicable for PlaceholderAPI placeholders."})
@Examples({"command /ping <player>:",
	"\ttrigger:",
	"\t\tset {_ping} to placeholder \"player_ping\" from arg-1 # PlaceholderAPI",
	"\t\tset {_ping} to placeholder \"{ping}\" from arg-1 # MVdWPlaceholderAPI",
	"\t\tsend \"Ping of %arg-1%: %{_ping}%\" to player"})
@Since("1.0 - PAPI Placeholders | 1.2 - MVdW Placeholders | 1.3 - Updated Syntax | 1.4 - Colorize Option")
public class ExprParsePlaceholder extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprParsePlaceholder.class, String.class, ExpressionType.SIMPLE,
				"[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [(from|of) %-players/offlineplayers%] [(1¦without color)]",
				"parse placeholder[s] %strings% [(for|as) %-players/offlineplayers%] [(1¦without color)]");
	}

	private Expression<String> placeholders;
	private Expression<OfflinePlayer> players;

	private boolean colorize;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		placeholders = (Expression<String>) exprs[0];
		players = (Expression<OfflinePlayer>) exprs[1];
		colorize = parseResult.mark != 1;
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		List<String> values = new ArrayList<>();
		String[] placeholders = this.placeholders.getArray(e);
		if (this.players != null) {
			Object[] players = this.players.getArray(e);
			for (String placeholder : placeholders) {
				for (Object player : players) {
					if (player instanceof OfflinePlayer)
						values.add(PlaceholderUtils.getPlaceholder(placeholder, (OfflinePlayer) player, colorize));
				}
			}
		} else {
			for (String placeholder : placeholders)
				values.add(PlaceholderUtils.getPlaceholder(placeholder, null, colorize));
		}
		return values.toArray(new String[0]);
	}

	@Override
	public boolean isSingle() {
		return players != null ? (placeholders.isSingle() && players.isSingle()) : placeholders.isSingle();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		if (players != null)
			return "the value of placeholder(s) " + placeholders.toString(e, debug) + " from " + players.toString(e, debug);
		return "the value of placeholder(s) " + placeholders.toString(e, debug);
	}

}
