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
import io.github.apickledwalrus.placeholderaddon.placeholderapi.PlaceholderAPIEvent;
import org.bukkit.event.Event;

@Name("PlaceholderAPI Identifier")
@Description("Returns the identifier of the placeholder in a PlaceholderAPI request event.")
@Examples({"on placeholderapi request with prefix \"hello\":",
	"\tif the identifier is \"world\": # Placeholder is hello_world", 
	"# The identifier is the part after the first underscore."})
@Since("1.0")
public class ExprPapiIdentifier extends SimpleExpression<String> {

	static {
		if (Main.hasPapi()) {
			Skript.registerExpression(ExprPapiIdentifier.class, String.class, ExpressionType.SIMPLE,
					"[the] [(placeholder[api]|papi)] identifier");
		}
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (!ScriptLoader.isCurrentEvent(PlaceholderAPIEvent.class)) {
			Skript.error("The PlaceholderAPI identifier can only be used in a PlaceholderAPI request event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		return new String[]{((PlaceholderAPIEvent) e).getIdentifier()};
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
		return "the placeholderapi identifier";
	}

}
