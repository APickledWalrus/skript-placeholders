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
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.pikachu.papiaddon.placeholderapi.PlaceholderAPIEvent;
import org.bukkit.event.Event;

@Name("Placeholder Prefix")
@Description("Represents the prefix in a placeholder request event")
@Examples("on placeholder request with the prefix \"example\":\n\tbroadcast the prefix # \"example\" will be broadcasted")
public class ExprPrefix extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPrefix.class, String.class, ExpressionType.SIMPLE,"[the] [(placeholder[api]|papi)] (prefix|placeholder)");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(PlaceholderAPIEvent.class)) {
            Skript.error("The PlaceholderAPI prefix can only be used in a placeholder request event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    @Override
    protected String[] get(final Event e) {
        return new String[] {((PlaceholderAPIEvent) e).getPrefix()};
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the placeholder prefix";
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
