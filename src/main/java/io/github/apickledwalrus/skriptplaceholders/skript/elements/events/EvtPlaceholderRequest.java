package io.github.apickledwalrus.skriptplaceholders.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvent;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Placeholder Request Event")
@Description({
	"Triggers whenever the value of a placeholder is requested by a supported placeholder plugin.",
})
@Examples({
	"on placeholderapi placeholder request for the prefix \"skriptplaceholders\":",
		"\tif the identifier is \"author\": # Placeholder is \"%skriptplaceholders_author%\"",
		"\t\tset the result to \"APickledWalrus\"",
	"on mvdw placeholder request for the placeholder \"skriptplaceholders_author\":",
		"\t# Placeholder is \"{skriptplaceholders_author}\"",
		"\tset the result to \"APickledWalrus\""
})
@Since("1.0, 1.3 (MVdWPlaceholderAPI support)")
public class EvtPlaceholderRequest extends SkriptEvent {

	static {
		Skript.registerEvent("Placeholder Request", EvtPlaceholderRequest.class, PlaceholderEvent.class, 
				"(placeholder[ ]api|papi) [placeholder] request (for|with) [the] prefix[es] %strings%",
				"(mvdw[ ]placeholder[ ]api|mvdw) [placeholder] request (for|with) [the] placeholder[s] %strings%"
		);
		EventValues.registerEventValue(PlaceholderEvent.class, Player.class, new Getter<Player, PlaceholderEvent>() {
			@Override
			@Nullable
			public Player get(PlaceholderEvent event) {
				if (event.getPlayer() != null && event.getPlayer().isOnline())
					return (Player) event.getPlayer();
				return null;
			}
		}, EventValues.TIME_NOW);
		EventValues.registerEventValue(PlaceholderEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlaceholderEvent>() {
			@Override
			@Nullable
			public OfflinePlayer get(PlaceholderEvent event) {
				return event.getPlayer();
			}
		}, EventValues.TIME_NOW);
	}

	@SuppressWarnings("NotNullFieldNotInitialized")
	private PlaceholderPlugin plugin;

	@SuppressWarnings("NotNullFieldNotInitialized")
	private String[] placeholders;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		plugin = PlaceholderPlugin.values()[matchedPattern];
		if (!plugin.isInstalled()) {
			Skript.error(plugin.getDisplayName() + " placeholders can not be requested as the plugin is not installed.");
			return false;
		}

		List<String> placeholders = new ArrayList<>();
		for (Literal<?> literal : args) {
			String placeholder = (String) literal.getSingle();

			String error = plugin.isValidPrefix(placeholder);
			if (error != null) {
				Skript.error(error);
				return false;
			}

			placeholders.add(placeholder);
		}

		this.placeholders = placeholders.toArray(new String[0]);

		// see https://github.com/APickledWalrus/skript-placeholders/issues/40
		if (Bukkit.isPrimaryThread()) {
			for (String placeholder : placeholders) {
				plugin.registerPlaceholder(placeholder);
			}
		} else {
			Bukkit.getScheduler().runTask(SkriptPlaceholders.getInstance(), () -> {
				for (String placeholder : placeholders) {
					plugin.registerPlaceholder(placeholder);
				}
			});
		}

		return true;
	}

	@Override
	public boolean check(Event event) {
		String eventPlaceholder;
		switch (plugin) {
			case PLACEHOLDER_API:
				eventPlaceholder = ((PlaceholderEvent) event).getPrefix();
				break;
			case MVDW_PLACEHOLDER_API:
				eventPlaceholder = ((PlaceholderEvent) event).getPlaceholder();
				break;
			default:
				throw new IllegalArgumentException("Unable to handle PlaceholderPlugin: " + plugin);
		}

		for (String placeholder : this.placeholders) {
			if (placeholder.equalsIgnoreCase(eventPlaceholder)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		// TODO better array->string handling
		String placeholders = Arrays.toString(this.placeholders);
		placeholders = placeholders.substring(1, placeholders.length() - 1); // Trim off the ends
		switch (plugin) {
			case PLACEHOLDER_API:
				return "placeholderapi request for the prefixes " + placeholders;
			case MVDW_PLACEHOLDER_API:
				return "mvdwplaceholderapi request for the placeholders " + placeholders;
			default:
				throw new IllegalArgumentException("Unable to handle PlaceholderPlugin: " + plugin);
		}
	}

}
