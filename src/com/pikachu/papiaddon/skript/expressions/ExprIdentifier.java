package com.pikachu.papiaddon.skript.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import com.pikachu.papiaddon.placeholderapi.PlaceholderAPIEvent;
import org.bukkit.event.Event;
import ch.njol.skript.lang.util.SimpleExpression;

@Name("Placeholder Identifier")
@Description("Grabs the identifier from a placeholder request event")
@Examples("on placeholder request with prefix \"example\":\n\tif the identifier is \"name\": # example_name\n\t\tset the result to player's name\n\telse if the identifier is \"uuid\": # example_uuid\n\t\tset the result to the player's uuid\n\telse if the identifier is \"money\": # example_money\n\t\tset the result to \"$%{money::%player's uuid%}%\"")
public class ExprIdentifier extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprIdentifier.class, String.class, ExpressionType.SIMPLE,"[the] [(placeholder[api]|papi)] identifier");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(PlaceholderAPIEvent.class)) {
            Skript.error("The PlaceholderAPI identifier can only be used in a placeholder request event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    @Override
    protected String[] get(final Event e) {
        return new String[] {((PlaceholderAPIEvent) e).getIdentifier()};
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the placeholder identifier";
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
