package io.github.apickledwalrus.skriptplaceholders.skript.elements;

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
import io.github.apickledwalrus.skriptplaceholders.skript.PlaceholderEvent;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Placeholder")
@Description("An expression to obtain the placeholder (or part of it) in a placeholder request event.")
@Examples({
	"on placeholderapi placeholder request for the prefix \"custom\":",
		"\tbroadcast \"Placeholder: %the placeholder%\"",
		"\tbroadcast \"Prefix: %the placeholder prefix%\"",
		"\tbroadcast \"Identifier: %the placeholder identifier%\"",
	"on mvdw placeholder request for the placeholder \"custom_hey\":",
		"\tbroadcast \"Placeholder: %the placeholder%\""
})
@Since("1.0, 1.3 (MVdWPlaceholderAPI support)")
@Events("Placeholder Request")
public class ExprPlaceholder extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprPlaceholder.class, String.class, ExpressionType.SIMPLE,
				"[the] placeholder",
				"[the] [placeholder] (1:prefix|2:identifier)"
		);
	}

	private enum PlaceholderPart {
		PLACEHOLDER,
		PREFIX,
		IDENTIFIER
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	private PlaceholderPart part;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(PlaceholderEvent.class)) {
			Skript.error("The placeholder can only be used in a placeholder request event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		this.part = PlaceholderPart.values()[parseResult.mark];
		return true;
	}

	@Override
	protected String[] get(Event event) {
		switch (part) {
			case PLACEHOLDER:
				return new String[]{((PlaceholderEvent) event).getPlaceholder()};
			case PREFIX:
				return new String[]{((PlaceholderEvent) event).getPrefix()};
			case IDENTIFIER:
				return new String[]{((PlaceholderEvent) event).getIdentifier()};
			default:
				throw new IllegalArgumentException("Unable to handle PlaceholderPart: " + part);
		}
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
	public String toString(@Nullable Event event, boolean debug) {
		switch (part) {
			case PLACEHOLDER:
				return "the placeholder";
			case PREFIX:
				return "the placeholder prefix";
			case IDENTIFIER:
				return "the placeholder identifier";
			default:
				throw new IllegalArgumentException("Unable to handle PlaceholderPart: " + part);
		}
	}

}
