package io.github.apickledwalrus.skriptplaceholders.skript.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.ScriptLoader;
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
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;
import org.eclipse.jdt.annotation.Nullable;

@Name("Placeholder Result")
@Description("The value of a placeholder in a placeholder event. Can be set, reset, or deleted.")
@Examples({
		"on placeholderapi placeholder request for the prefix \"custom\":",
		"\tif the identifier is \"hello\": # Placeholder is \"%custom_hey%\"",
		"\t\tset the result to \"Hey there %player%!\"",
		"on mvdw placeholder request for the placeholder \"custom_hey\":",
		"\t# Placeholder is \"{custom_hey}\"",
		"\tset the result to \"Hey there %player%!\""
})
@Since("1.0, 1.3 (MVdWPlaceholderAPI support)")
@Events("Placeholder Request")
public class ExprPlaceholderResult extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprPlaceholderResult.class, String.class, ExpressionType.SIMPLE,
				"[the] [[event(-| )]placeholder] result"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!ScriptLoader.isCurrentEvent(PlaceholderEvent.class)) {
			Skript.error("The placeholder result can only be used in a placeholder request event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	protected String[] get(Event e) {
		return new String[]{((PlaceholderEvent) e).getResult()};
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET || mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
			return CollectionUtils.array(String.class);
		}
		return null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		if (delta == null && mode == ChangeMode.SET)
			return;
		switch (mode) {
			case SET:
				((PlaceholderEvent) e).setResult((String) delta[0]);
				break;
			case RESET:
			case DELETE:
				((PlaceholderEvent) e).setResult(null);
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
	public String toString(@Nullable Event e, boolean debug) {
		return "the placeholder result";
	}

}
