package com.pikachu.papiaddon.skript.expressions;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
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
@Description("Grabs the value of a PlaceholderAPI prefix")
@Examples("on first join:\n\tset {_uniqueJoins} to the value of placeholder \"server_unique_joins\"\n\tbroadcast \"%{_uniqueJoins}% unique players have joined our server!\"")
public class ExprPlaceholder extends SimpleExpression<String> {

    private Expression<String> placeholders;
    private Expression<Player> players;

    static {
        Skript.registerExpression(ExprPlaceholder.class, String.class, ExpressionType.SIMPLE,"[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [from %players%]");
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

    private String getPlaceholder(String placeholder, Player player) {
        placeholder = formatPlaceholder(placeholder);
        if (PlaceholderAPI.containsPlaceholders(placeholder)) {
            String value = PlaceholderAPI.setPlaceholders(player, placeholder);
            if (value.equals(placeholder) || "".equals(value)) {
                return null;
            }
            return value;
        } else {
            return null;
        }
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
        if (players.length !=  0) {
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
