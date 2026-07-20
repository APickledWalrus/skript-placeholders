package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Events;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.github.apickledwalrus.skriptplaceholders.skript.PlaceholderEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Placeholder Result")
@Description("The value of a placeholder in a placeholder event. Can be set, reset, or deleted.")
@Example("""
	placeholderapi placeholder with the prefix "skriptplaceholders":
		if the identifier is "author": # Placeholder is "%skriptplaceholders_author%"
			set the result to "APickledWalrus"
	""")
@Example("""
	mvdw placeholder named "skriptplaceholders_author":
		# Placeholder is "{skriptplaceholders_author}"
		set the result to "APickledWalrus"
	""")
@Since("1.0.0, 1.3.0 (MVdWPlaceholderAPI support), 1.6.0 (using any type support)")
@Events("Placeholder Request")
public class ExprPlaceholderResult extends SimpleExpression<String> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EXPRESSION, SyntaxInfo.Expression.builder(ExprPlaceholderResult.class, String.class)
			.supplier(ExprPlaceholderResult::new)
			.addPattern("[the] [placeholder] result")
			.build());
	}

	@Override
	public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
		if (!getParser().isCurrentEvent(PlaceholderEvent.class)) {
			Skript.error("'the placeholder result' can only be used in custom placeholders");
			return false;
		}
		return true;
	}

	@Override
	protected String @NotNull [] get(@NotNull Event event) {
		return new String[]{((PlaceholderEvent) event).getResult()};
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, DELETE, RESET -> CollectionUtils.array(Object.class);
			default -> null;
		};
	}

	@Override
	public void change(@NotNull Event event, Object @Nullable [] delta, ChangeMode mode) {
		String result = null;
		if (delta != null) {
			if (delta[0] instanceof String string) {
				result = string;
			} else {
				result = Classes.toString(delta[0]);
			}
		}
		((PlaceholderEvent) event).setResult(result);
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
		return "the placeholder result";
	}

}
