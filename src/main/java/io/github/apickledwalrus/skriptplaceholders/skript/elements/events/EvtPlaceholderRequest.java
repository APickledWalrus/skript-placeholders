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
@Examples({"on placeholderapi placeholder request for the prefix \"custom\":",
		"\tif the identifier is \"hello\": # Placeholder is \"%custom_hey%\"",
		"\t\tset the result to \"Hey there %player%!\"",
		"on mvdw placeholder request for the placeholder \"custom_hey\":",
		"\t# Placeholder is \"{custom_hey}\"",
		"\tset the result to \"Hey there %player%!\""
})
@Since("1.0 - PlaceholderAPI | 1.3 - MVdWPlaceholderAPI | 2.0 - New Event Features")
public class EvtPlaceholderRequest extends SkriptEvent {

	static {
		Skript.registerEvent("Placeholder Request", EvtPlaceholderRequest.class, PlaceholderEvent.class, 
				"(placeholder[ ]api|papi) [placeholder] request (for|with) [the] prefix[es] %strings%",
				"mvdw[ ](placeholder[ ]api [placeholder]|placeholder) request (for|with) [the] placeholder[s] %strings%"
		);
		EventValues.registerEventValue(PlaceholderEvent.class, Player.class, new Getter<Player, PlaceholderEvent>() {
			@Override
			public Player get(PlaceholderEvent e) {
				if (e.getPlayer().isOnline())
					return (Player) e.getPlayer();
				return null;
			}
		}, 0);
		EventValues.registerEventValue(PlaceholderEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlaceholderEvent>() {
			@Override
			public OfflinePlayer get(PlaceholderEvent e) {
				return e.getPlayer();
			}
		}, 0);
	}

	private String[] placeholders;

	private int pattern;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		switch (matchedPattern) { // Installation Check
			case 0: // PlaceholderAPI
				if (!SkriptPlaceholders.hasPapi()) {
					Skript.error("PlaceholderAPI is required to register PlaceholderAPI placeholders.", ErrorQuality.SEMANTIC_ERROR);
					return false;
				}
				break;
			case 1: // MVdWPlaceholderAPI
				if (!SkriptPlaceholders.hasPapi()) {
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
			case 0: // PlaceholderAPI
				for (String placeholder : placeholders)
					new PlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
				break;
			case 1: // MVdWPlaceholderAPI
				for (String placeholder : placeholders)
					new MVdWPlaceholderAPIListener(SkriptPlaceholders.getInstance(), placeholder).register();
				break;
		}
		this.placeholders = placeholders.toArray(new String[0]);
		this.pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		String eventPlaceholder = "";
		switch (pattern) {
			case 0: // PlaceholderAPI
				eventPlaceholder = ((PlaceholderEvent) e).getPrefix();
				break;
			case 1: // MVdWPlaceholderAPI
				eventPlaceholder = ((PlaceholderEvent) e).getPlaceholder();
				break;
		}
		if (eventPlaceholder.equals(""))
			return false;
		for (String placeholder : this.placeholders) {
			if (eventPlaceholder.equalsIgnoreCase(placeholder))
				return true;
		}
		return false;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		String placeholders = Arrays.toString(this.placeholders);
		// Trim off the ends
		placeholders = placeholders.substring(1, placeholders.length() - 1);
		switch (pattern) {
			case 0: // PlaceholderAPI
				return "placeholderapi placeholder request for the prefixes " + placeholders;
			case 1: // MVdWPlaceholderAPI
				return "mvdwplaceholderapi placeholder request for the placeholders " + placeholders;
			default:
				return "placeholder request event";
		}
	}

}
