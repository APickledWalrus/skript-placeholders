package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.skriptplaceholders.skript.RelationalPlaceholderEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Relational Placeholder Players")
@Description("The two players involved in a relational placeholder request.")
@Examples({
	"on placeholderapi placeholder request for the relational prefix \"skriptplaceholders\":",
		"\tif the identifier is \"rel_longer_name\": # Placeholder is \"%skriptplaceholders_author%\"",
			"\t\tif the length of the name of the first player > the length of the name of the second player:",
				"\t\t\tset the result to the name of the first player",
			"\t\telse:",
				"\t\t\tset the result to the name of the second player"
})
@Since("1.7.0")
public class ExprRelationalPlayers extends SimpleExpression<Player> {

	static {
		Skript.registerExpression(ExprRelationalPlayers.class, Player.class, ExpressionType.SIMPLE,
				"[the] first player",
				"[the] second player"
		);
	}

	private boolean first;

	@Override
	public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean kleenean, @NotNull ParseResult parseResult) {
		first = matchedPattern == 0;
		if (!getParser().isCurrentEvent(RelationalPlaceholderEvent.class)) {
			Skript.error("'the " + (first ? "first" : "second") + " player' can only be used in custom relational placeholders.");
			return false;
		}
		return true;
	}

	@Override
	protected Player @NotNull [] get(@NotNull Event e) {
		if (!(e instanceof RelationalPlaceholderEvent)) {
			return new Player[0];
		}
		RelationalPlaceholderEvent event = (RelationalPlaceholderEvent) e;
		return new Player[]{first ? event.getPlayer() : event.getOther()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public @NotNull Class<? extends Player> getReturnType() {
		return Player.class;
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
		return "the " + (first ? "first" : "second") + " player";
	}

}
