package io.github.apickledwalrus.placeholderaddon.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.github.apickledwalrus.placeholderaddon.Main;
import io.github.apickledwalrus.placeholderaddon.mvdwapi.MvdwAPIListener;
import io.github.apickledwalrus.placeholderaddon.mvdwapi.MvdwAPIEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("On MVdWPlaceholderAPI Placeholder Request")
@Description("Called whenever a placeholder is requested by MVdWPlaceholderAPI.")
@Examples("INSERT EXAMPLE")
@Since("1.3")
public class EvtMvdwPlaceholderRequest extends SkriptEvent {

	static {
		if (!Main.hasMVdW()) {
			Skript.registerEvent("Placeholder Request", EvtMvdwPlaceholderRequest.class, MvdwAPIEvent.class, "mvdw[ ][placeholder[api]] request with [the] placeholder %string%");
			EventValues.registerEventValue(MvdwAPIEvent.class, Player.class, new Getter<Player, MvdwAPIEvent>() {
				@Override
				public Player get(MvdwAPIEvent e) {
					return e.getPlayer();
				}
			}, 0);
		}
	}

	private String placeholder;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final SkriptParser.ParseResult parser) {
		placeholder = ((Literal<String>) args[0]).getSingle();
		if ("".equals(placeholder)) {
			Skript.error(placeholder + " is not a valid placeholder", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		MvdwAPIListener.registerPlaceholder(Main.getInstance(), placeholder);
		return true;
	}

	@Override
	public boolean check(final Event e) {
		return ((MvdwAPIEvent) e).getPlaceholder().equalsIgnoreCase(placeholder);
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "placeholder request" + (placeholder != null ? ("with placeholder \"" + placeholder + "\"") : "");
	}
}
