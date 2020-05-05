package io.github.apickledwalrus.placeholderaddon.skript.expressions;

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
import io.github.apickledwalrus.placeholderaddon.Main;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
@Since("1.0 - PAPI Placeholders, 1.2 - MVdW Placeholders, 1.3 - Updated Syntax, 1.4 - Colorize Option")
public class ExprParsePlaceholder extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprParsePlaceholder.class, String.class, ExpressionType.SIMPLE,
				"[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [from %-players/offlineplayers%] [(1¦without color)]",
				"parse placeholder[s] %strings% [(for|as) %-players/offlineplayers%] [(1¦without color)]");
	}

	private Expression<String> placeholders;
	private Expression<OfflinePlayer> players;

	private boolean colorize;

	private String formatPlaceholder(String placeholder) {
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

	private String getPlaceholder(String placeholder, OfflinePlayer player) {
		String value;

		if (Main.hasMVdW()) {
			if (placeholder.charAt(0) == '{' && placeholder.charAt(placeholder.length() - 1) == '}') {
				value = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, placeholder);
				return value.equals(placeholder) ? null : value;
			}
		}

		if (Main.hasPapi()) {
			placeholder = formatPlaceholder(placeholder);
			if (PlaceholderAPI.containsPlaceholders(placeholder)) {

				if (player != null && player.isOnline()) {
					value = PlaceholderAPI.setPlaceholders((Player) player, placeholder, !colorize);
				} else {
					value = PlaceholderAPI.setPlaceholders(player, placeholder, !colorize);
				}

				return value.equals(placeholder) ? null : value;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		placeholders = (Expression<String>) exprs[0];
		players = (Expression<OfflinePlayer>) exprs[1];
		colorize = parseResult.mark == 1;
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		String[] placeholders = this.placeholders.getArray(e);
		OfflinePlayer[] players = this.players.getArray(e);
		List<String> values = new ArrayList<>();
		if (players.length != 0) {
			for (String pl : placeholders) {
				for (OfflinePlayer p : players) {
					values.add(getPlaceholder(pl, p));
				}
			}
		} else {
			for (String pl : placeholders) {
				values.add(getPlaceholder(pl, null));
			}
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
		return "the value of placeholder(s) " + placeholders.toString(e, debug) + " from " + players.toString(e, debug);
	}

}
