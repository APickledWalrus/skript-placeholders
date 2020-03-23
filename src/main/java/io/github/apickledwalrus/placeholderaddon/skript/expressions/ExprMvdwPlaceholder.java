package io.github.apickledwalrus.placeholderaddon.skript.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.placeholderaddon.Main;
import io.github.apickledwalrus.placeholderaddon.mvdwapi.MvdwAPIEvent;
import org.bukkit.event.Event;

@Name("MVdWPlaceholderAPI Placeholder")
@Description("Returns the placeholder in a MVdWPlaceholderAPI request event.")
@Examples({"on mvdw placeholder request for placeholder \"doublehealth\":",
			"\tsend \"MVdWPlaceholderAPI requested the placeholder %placeholder%!\" to console"})
@Since("1.3")
public class ExprMvdwPlaceholder extends SimpleExpression<String> {

	static {
		if (Main.hasMVdW()) {
			Skript.registerExpression(ExprMvdwPlaceholder.class, String.class, ExpressionType.SIMPLE,
					"[the] [mvdw[ ][placeholder[api]]] placeholder");
		}
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (!ScriptLoader.isCurrentEvent(MvdwAPIEvent.class)) {
			Skript.error("The MVdWPlaceholderAPI placeholder can only be used in a MVdWPlaceholderAPI request event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		return new String[]{((MvdwAPIEvent) e).getPlaceholder()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "the mvdwplaceholderapi prefix";
	}

}
