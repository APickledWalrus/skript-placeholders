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
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.skriptplaceholders.skript.PlaceholderEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Name("Placeholder")
@Description("An expression to obtain the placeholder (or part of it) in a placeholder request event.")
@Examples({
	"placeholderapi placeholder with the prefix \"skriptplaceholders\":",
		"\tbroadcast \"Placeholder: %the placeholder%\"",
		"\tbroadcast \"Prefix: %the placeholder prefix%\"",
		"\tbroadcast \"Identifier: %the placeholder identifier%\"",
	"mvdw placeholder named \"skriptplaceholders_test\":",
		"\tbroadcast \"Placeholder: %the placeholder%\""
})
@Since("1.0.0, 1.3.0 (MVdWPlaceholderAPI support)")
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

	private PlaceholderPart part;

	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
		this.part = PlaceholderPart.values()[parseResult.mark];
		if (!getParser().isCurrentEvent(PlaceholderEvent.class)) {
			Skript.error("'the " + part.name().toLowerCase(Locale.ENGLISH) + "' can only be used in custom placeholders");
			return false;
		}
		return true;
	}

	@Override
	protected String @NotNull [] get(@NotNull Event event) {
		if (!(event instanceof PlaceholderEvent)) {
			return new String[0];
		}
		PlaceholderEvent placeholderEvent = (PlaceholderEvent) event;
		switch (part) {
			case PLACEHOLDER:
				return new String[]{placeholderEvent.getPlaceholder()};
			case PREFIX:
				String prefix = placeholderEvent.getPrefix();
				return prefix != null ? new String[]{prefix} : new String[0];
			case IDENTIFIER:
				String identifier = placeholderEvent.getIdentifier();
				return identifier != null ? new String[]{identifier} : new String[0];
			default:
				throw new IllegalArgumentException("Unable to handle PlaceholderPart: " + part);
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
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
