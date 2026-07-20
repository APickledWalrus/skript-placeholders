package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.apickledwalrus.skriptplaceholders.skript.RelationalPlaceholderEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Relational Placeholder Players")
@Description("The two players involved in a relational placeholder request.")
@Example("""
	placeholderapi relational placeholder with the prefix "skriptplaceholders":
		if the identifier is "longer_name": # Placeholder is "%rel_skriptplaceholders_longer_name%"
			if the length of the name of the first player > the length of the name of the second player:
				set the result to the name of the first player
			else:
				set the result to the name of the second player
	""")
@Since("1.7.0")
public class ExprRelationalPlaceholderPlayers extends SimpleExpression<Player> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EXPRESSION, SyntaxInfo.Expression.builder(ExprRelationalPlaceholderPlayers.class, Player.class)
			.supplier(ExprRelationalPlaceholderPlayers::new)
			.addPatterns("[the] first player",
				"[the] second player")
			.build());
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
	protected Player @NotNull [] get(@NotNull Event event) {
		RelationalPlaceholderEvent placeholderEvent = (RelationalPlaceholderEvent) event;
		return new Player[]{first ? placeholderEvent.getPlayer() : placeholderEvent.getOther()};
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
