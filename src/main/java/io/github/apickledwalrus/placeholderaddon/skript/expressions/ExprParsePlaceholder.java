package io.github.apickledwalrus.placeholderaddon.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.placeholderaddon.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Value of Placeholder")
@Description("Returns the value of a PlaceholderAPI/MVdWPlaceholderAPI placeholder.")
@Examples({"command /ping <player>:",
	"\ttrigger:",
	"\t\tset {_ping} to placeholder \"player_ping\" from arg-1 # PlaceholderAPI",
	"\t\tset {_ping} to placeholder \"{ping}\" from arg-1 # MVdWPlaceholderAPI",
	"\t\tsend \"Ping of %arg-1%: %{_ping}%\" to player"})
@Since("1.0 - PAPI Placeholders, 1.2 - MVdW Placeholders, 1.3 - Updated Syntax")
public class ExprParsePlaceholder extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprParsePlaceholder.class, String.class, ExpressionType.SIMPLE,
				"[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [from %players%]",
				"parse placeholder[s] %strings% [(for|as) %players%]");
	}

	private Expression<String> placeholders;
	private Expression<Player> players;

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

	/*
	 * Type - Represents where the placeholder is from e.g. PAPI, MVdW
	 */
	private String getPlaceholder(String placeholder, Player player) {
		String value;
		if (Main.hasMVdW()) {
			if (placeholder.charAt(0) == '{' && placeholder.charAt(placeholder.length() - 1) == '}') {
				value = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, placeholder);
				if (value.equals(placeholder)) // MVdW placeholders return the input if no MVdW plugins are installed.
					return null;
				return value;
			}
		}
		if (Main.hasPapi()) {
			placeholder = formatPlaceholder(placeholder);
			if (PlaceholderAPI.containsPlaceholders(placeholder)) {
				value = PlaceholderAPI.setPlaceholders(player, placeholder);
				if (value.equals(placeholder) || "".equals(value))
					return null;
				return value;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		placeholders = (Expression<String>) exprs[0];
		players = (Expression<Player>) exprs[1];
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		String[] placeholders = this.placeholders.getArray(e);
		Player[] players = this.players.getArray(e);
		List<String> values = new ArrayList<>();
		if (players.length != 0) {
			for (String ph : placeholders) {
				for (Player p : players) {
					values.add(getPlaceholder(ph, p));
				}
			}
		} else {
			for (String ph : placeholders) {
				values.add(getPlaceholder(ph, null));
			}
		}
		return values.toArray(new String[0]);
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "the value of placeholder " + placeholders.toString(e, debug) + " from " + players.toString(e, debug);
	}

	@Override
	public boolean isSingle() {
		return placeholders.isSingle() && players.isSingle();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
