package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Events;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.github.apickledwalrus.skriptplaceholders.skript.PlaceholderEvent;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Placeholder Result")
@Description("The value of a placeholder in a placeholder event. Can be set, reset, or deleted.")
@Examples({
	"on placeholderapi placeholder request for the prefix \"skriptplaceholders\":",
		"\tif the identifier is \"author\": # Placeholder is \"%skriptplaceholders_author%\"",
			"\t\tset the result to \"APickledWalrus\"",
	"on mvdw placeholder request for the placeholder \"skriptplaceholders_author\":",
		"\t# Placeholder is \"{skriptplaceholders_author}\"",
		"\tset the result to \"APickledWalrus\""
})
@Since("1.0, 1.3 (MVdWPlaceholderAPI support), INSERT VERSION (object support)")
@Events("Placeholder Request")
public class ExprPlaceholderResult extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprPlaceholderResult.class, String.class, ExpressionType.SIMPLE,
				"[the] [placeholder] result"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(PlaceholderEvent.class)) {
			Skript.error("The placeholder result can only be used in a placeholder request event");
			return false;
		}
		return true;
	}

	@Override
	protected String[] get(Event event) {
		return new String[]{((PlaceholderEvent) event).getResult()};
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		switch (mode) {
			case SET:
			case DELETE:
			case RESET:
				return CollectionUtils.array(Object.class);
			default:
				return null;
		}
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		PlaceholderEvent placeholderEvent = ((PlaceholderEvent) event);
		switch (mode) {
			case SET:
				if (delta[0] instanceof String) {
					placeholderEvent.setResult((String) delta[0]);
				} else {
					placeholderEvent.setResult(Classes.toString(delta[0]));
				}
				break;
			case RESET:
			case DELETE:
				placeholderEvent.setResult(null);
				break;
			default:
				assert false;
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
		return "the placeholder result";
	}

}
