package io.github.apickledwalrus.placeholderaddon.skript.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
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
import ch.njol.util.coll.CollectionUtils;
import io.github.apickledwalrus.placeholderaddon.Main;
import io.github.apickledwalrus.placeholderaddon.mvdwapi.MvdwAPIEvent;
import org.bukkit.event.Event;

@Name("MVdWPlaceholderAPI Result")
@Description("The result (placeholder value) in a MVdWPlaceholderAPI request event.")
@Examples("INSERT EXAMPLE")
@Since("1.3")
public class ExprMvdwResult extends SimpleExpression<String> {

	static {
		if (!Main.hasMVdW()) {
			Skript.registerExpression(ExprMvdwResult.class, String.class, ExpressionType.SIMPLE,
							"[the] [mvdw[ ][placeholder[api]]] result");
		}
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (!ScriptLoader.isCurrentEvent(MvdwAPIEvent.class)) {
			Skript.error("The MVdWPlaceholderAPI result can only be used in a MVdWPlaceholderAPI request event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		return new String[]{((MvdwAPIEvent) e).getResult()};
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "the mvdwplaceholderapi result";
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.SET || mode == ChangeMode.DELETE || mode == ChangeMode.RESET) {
			return CollectionUtils.array(String.class);
		}
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, ChangeMode mode) {
		switch (mode) {
			case SET:
				((MvdwAPIEvent) e).setResult((String) delta[0]);
				break;
			case RESET:
			case DELETE:
				((MvdwAPIEvent) e).setResult(null);
				break;
		}
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
}
