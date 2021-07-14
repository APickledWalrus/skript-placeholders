package io.github.apickledwalrus.skriptplaceholders.skript.elements.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;
import io.github.apickledwalrus.skriptplaceholders.placeholder.mvdwplaceholderapi.MVdWPlaceholderAPIListener;
import io.github.apickledwalrus.skriptplaceholders.placeholder.placeholderapi.PlaceholderAPIListener;

@Name("Placeholder Request Event")
@Description("Called whenever a placeholder is requested by a supported placeholder plugin.")
@Examples({
		"on placeholderapi placeholder request for the prefix \"custom\":",
		"\tif the identifier is \"hello\": # Placeholder is \"%custom_hey%\"",
		"\t\tset the result to \"Hey there %player%!\"",
		"on mvdw placeholder request for the placeholder \"custom_hey\":",
		"\t# Placeholder is \"{custom_hey}\"",
		"\tset the result to \"Hey there %player%!\""
})
@Since("1.0, 1.3 (MVdWPlaceholderAPI support), 2.0 (new event features)")
public class EvtPlaceholderRequest extends SkriptEvent {

	static {
		Skript.registerEvent("Placeholder Request", EvtPlaceholderRequest.class, PlaceholderEvent.class, 
				"(placeholder[ ]api|papi) [placeholder] request (for|with) [the] prefix[es] %strings%",
				"mvdw[ ](placeholder[ ]api [placeholder]|placeholder) request (for|with) [the] placeholder[s] %strings%"
		);
		EventValues.registerEventValue(PlaceholderEvent.class, Player.class, new Getter<Player, PlaceholderEvent>() {
			@Override
			@Nullable
			public Player get(PlaceholderEvent e) {
				if (e.getPlayer() != null && e.getPlayer().isOnline())
					return (Player) e.getPlayer();
				return null;
			}
		}, 0);
		EventValues.registerEventValue(PlaceholderEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlaceholderEvent>() {
			@Override
			@Nullable
			public OfflinePlayer get(PlaceholderEvent e) {
				return e.getPlayer();
			}
		}, 0);
	}

	private static final int PLACEHOLDERAPI = 0, MVDWPLACEHOLDERAPI = 1;

	@SuppressWarnings("NotNullFieldNotInitialized")
	private String[] placeholders;
	private int source;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		switch (matchedPattern) { // Installation Check
			case PLACEHOLDERAPI:
				if (!SkriptPlaceholders.hasPapi()) {
					Skript.error("PlaceholderAPI is required to register PlaceholderAPI placeholders.", ErrorQuality.SEMANTIC_ERROR);
					return false;
				}
				break;
			case MVDWPLACEHOLDERAPI:
				if (!SkriptPlaceholders.hasMVdW()) {
					Skript.error("MVdWPlaceholderAPI is required to register MVdWPlaceholderAPI placeholders.", ErrorQuality.SEMANTIC_ERROR);
					return false;
				}
				break;
		}

		List<String> placeholders = new ArrayList<>();
		for (Literal<?> literal : args) {
			String placeholder = (String) literal.getSingle();
			if (StringUtils.isBlank(placeholder)) {
				Skript.error(placeholder + " is not a valid placeholder", ErrorQuality.SEMANTIC_ERROR);
				return false;
			}
			placeholders.add(placeholder);
		}
		if (placeholders.isEmpty())
			return false;

		switch (matchedPattern) {
			case PLACEHOLDERAPI:
				for (String placeholder : placeholders)
					new PlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
				break;
			case MVDWPLACEHOLDERAPI:
				for (String placeholder : placeholders)
					new MVdWPlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
				break;
		}

		this.placeholders = placeholders.toArray(new String[0]);
		this.source = matchedPattern;

		return true;
	}

	@Override
	public boolean check(Event e) {
		String eventPlaceholder = null;
		switch (source) {
			case PLACEHOLDERAPI:
				eventPlaceholder = ((PlaceholderEvent) e).getPrefix();
				break;
			case MVDWPLACEHOLDERAPI:
				eventPlaceholder = ((PlaceholderEvent) e).getPlaceholder();
				break;
			default:
				assert false;
		}
		if (eventPlaceholder == null)
			return false;

		for (String placeholder : this.placeholders) {
			if (placeholder.equalsIgnoreCase(eventPlaceholder))
				return true;
		}

		return false;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		String placeholders = Arrays.toString(this.placeholders);
		placeholders = placeholders.substring(1, placeholders.length() - 1); // Trim off the ends
		switch (source) {
			case PLACEHOLDERAPI:
				return "placeholderapi placeholder request for the prefixes " + placeholders;
			case MVDWPLACEHOLDERAPI:
				return "mvdwplaceholderapi placeholder request for the placeholders " + placeholders;
			default:
				assert false;
				return "placeholder request event";
		}
	}

}
