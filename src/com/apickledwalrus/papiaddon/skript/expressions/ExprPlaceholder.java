package com.apickledwalrus.papiaddon.skript.expressions;
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
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Value of Placeholder")
@Description("Returns the value of a PlaceholderAPI/MVdW placeholder.")
@Examples("on first join:\n\tset {_uniqueJoins} to the value of placeholder \"server_unique_joins\"\n\tbroadcast \"%{_uniqueJoins}% unique players have joined our server!\"")
@Since("1.0 - PAPI Placeholders, 1.2 - MVdW Placeholders")
public class ExprPlaceholder extends SimpleExpression<String> {

	private Expression<String> placeholders;
	private Expression<Player> players;

	static {
		Skript.registerExpression(ExprPlaceholder.class, String.class, ExpressionType.SIMPLE,
				"[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [from %players%]");
	}

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
	private String getPlaceholder(String placeholder, Player player, boolean mvdw) {
		String value;
		if (mvdw) {
			if (placeholder.charAt(0) == '{' && placeholder.charAt(placeholder.length() - 1) == '}') {
				value = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(player, placeholder);
				if (value.equals(placeholder)) // MVdW placeholders return the input if no MVdW plugins are installed.
					return null;
				return value;
			}
		} else {
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
		boolean mvdw = Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI");
		if (players.length !=  0) {
			for (String ph : placeholders) {
				for (Player p : players) {
					values.add(getPlaceholder(ph, p, mvdw));
				}
			}
		} else {
			for (String ph : placeholders) {
				values.add(getPlaceholder(ph, null, mvdw));
			}
		}
		return values.toArray(new String[values.size()]);
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
