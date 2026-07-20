package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Events;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.skriptplaceholders.skript.PlaceholderEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

@Name("Placeholder")
@Description("An expression to obtain the placeholder (or part of it) in a placeholder request event.")
@Example("""
	placeholderapi placeholder with the prefix "skriptplaceholders":
		broadcast "Placeholder: %the placeholder%"
		broadcast "Prefix: %the placeholder prefix%"
		broadcast "Identifier: %the placeholder identifier%"
	""")
@Since("1.0.0")
@Events("Placeholder Request")
public class ExprPlaceholder extends SimpleExpression<String> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EXPRESSION, SyntaxInfo.Expression.builder(ExprPlaceholder.class, String.class)
			.supplier(ExprPlaceholder::new)
			.addPatterns("[the] placeholder",
				"[the] [placeholder] (1:prefix|2:identifier)")
			.build());
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
		PlaceholderEvent placeholderEvent = (PlaceholderEvent) event;
		return switch (part) {
			case PLACEHOLDER -> new String[]{placeholderEvent.getPlaceholder()};
			case PREFIX -> {
				String prefix = placeholderEvent.getPrefix();
				yield prefix != null ? new String[]{prefix} : new String[0];
			}
			case IDENTIFIER -> {
				String identifier = placeholderEvent.getIdentifier();
				yield identifier != null ? new String[]{identifier} : new String[0];
			}
		};
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
		return switch (part) {
			case PLACEHOLDER -> "the placeholder";
			case PREFIX -> "the placeholder prefix";
			case IDENTIFIER -> "the placeholder identifier";
		};
	}

}
