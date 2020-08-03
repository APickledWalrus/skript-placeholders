package io.github.apickledwalrus.skriptplaceholders.skript.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Events;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;

import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;

@Name("Placeholder")
@Description("Returns the placeholder in a placeholder request event.")
@Examples({"on placeholderapi placeholder request for the prefix \"custom\":",
	"\tbroadcast \"Placeholder: %the placeholder%\"",
	"\tbroadcast \"Prefix: %the placeholder prefix%\"",
	"\tbroadcast \"Identifier: %the placeholder identifier%\"",
	"on mvdw placeholder request for the placeholder \"custom_hey\":",
	"\tbroadcast \"Placeholder: %the placeholder%\"",
})
@Since("1.0 - PlaceholderAPI | 1.3 - MVdWPlaceholderAPI")
@Events("Placeholder Request")
public class ExprPlaceholder extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprPlaceholder.class, String.class, ExpressionType.SIMPLE,
				"[the] [event(-| )]placeholder",
				"[the] [[event(-| )]placeholder] (1¦prefix|2¦identifier)"
		);
	}

	/**
	 * 0 = full placeholder
	 * 1 = part BEFORE first underscore
	 * 2 = part AFTER first underscore
	 */
	private int placeholderPart;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!ScriptLoader.isCurrentEvent(PlaceholderEvent.class)) {
			Skript.error("The placeholder can only be used in a placeholder request event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		this.placeholderPart = parseResult.mark;
		return true;
	}

	@Override
	protected String[] get(Event e) {
		switch (placeholderPart) {
			case 0: // Full placeholder
				return new String[]{((PlaceholderEvent) e).getPlaceholder()};
			case 1: // Part BEFORE first underscore
				return new String[]{((PlaceholderEvent) e).getPrefix()};
			case 2: // Part AFTER first underscore
				return new String[]{((PlaceholderEvent) e).getIdentifier()};
		}
		return new String[]{};
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
		switch (placeholderPart) {
			case 0: // Full placeholder
				return "the placeholder";
			case 1: // Part BEFORE first underscore
				return "the placeholder prefix";
			case 2: // Part AFTER first underscore
				return "the placeholder identifier";
			default:
				return "placeholder";
		}
	}

}
