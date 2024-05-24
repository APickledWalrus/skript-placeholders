package io.github.apickledwalrus.skriptplaceholders.skript.elements;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.github.apickledwalrus.skriptplaceholders.SkriptPlaceholders;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderEvaluator;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderRegistry;
import io.github.apickledwalrus.skriptplaceholders.skript.PlaceholderEvent;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import io.github.apickledwalrus.skriptplaceholders.skript.RelationalPlaceholderEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.script.Script;
import org.skriptlang.skript.lang.structure.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Custom Placeholder")
@Description({
	"A structure for creating custom placeholders.",
	"The code will be executed every time the placeholder plugin requests a value for the placeholder."
})
@Examples({
	"on placeholderapi placeholder request for the prefix \"skriptplaceholders\":",
		"\tif the identifier is \"author\": # Placeholder is \"%skriptplaceholders_author%\"",
			"\t\tset the result to \"APickledWalrus\"",
	"on placeholderapi placeholder request for the relational prefix \"skriptplaceholders\":",
		"\tif the identifier is \"longer_name\": # Placeholder is \"%rel_skriptplaceholders_longer_name%\"",
			"\t\tif the length of the name of the first player > the length of the name of the second player:",
				"\t\t\tset the result to the name of the first player",
			"\t\telse:",
				"\t\t\tset the result to the name of the second player",
	"on mvdw placeholder request for the placeholder \"skriptplaceholders_author\":",
		"\t# Placeholder is \"{skriptplaceholders_author}\"",
		"\tset the result to \"APickledWalrus\""
})
@Since("1.0.0, 1.3.0 (MVdWPlaceholderAPI support), 1.7.0 (relational placeholder support)")
public class StructPlaceholder extends Structure implements PlaceholderEvaluator {

	static {
		Skript.registerStructure(StructPlaceholder.class,
				"[on] (placeholder[ ]api|papi) [placeholder] request (for|with) [the] [:relational] prefix[es] %*strings%",
				"[on] (mvdw[ ]placeholder[ ]api|mvdw) [placeholder] request (for|with) [the] placeholder[s] %*strings%"
		);
		EventValues.registerEventValue(PlaceholderEvent.class, Player.class, new Getter<Player, PlaceholderEvent>() {
			@Override
			public Player get(PlaceholderEvent event) {
				OfflinePlayer player = event.getPlayer();
				return player != null ? player.getPlayer() : null;
			}
		}, EventValues.TIME_NOW);
		EventValues.registerEventValue(PlaceholderEvent.class, OfflinePlayer.class, new Getter<OfflinePlayer, PlaceholderEvent>() {
			@Override
			public OfflinePlayer get(PlaceholderEvent event) {
				return event.getPlayer();
			}
		}, EventValues.TIME_NOW);
	}

	private PlaceholderRegistry registry;
	private PlaceholderPlugin plugin;
	private String[] placeholders;

	private boolean isRelational;
	private Trigger trigger;

	@Override
	public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult, @NotNull EntryContainer entryContainer) {
		plugin = PlaceholderPlugin.values()[matchedPattern];
		if (!plugin.isInstalled()) {
			Skript.error(plugin.getDisplayName() + " placeholders can not be requested because the plugin is not installed.");
			return false;
		}

		List<String> placeholders = new ArrayList<>();
		//noinspection unchecked - Skript guarantees this will be a Literal<String>
		for (String placeholder : ((Literal<String>) args[0]).getAll()) {
			String error = plugin.validatePrefix(placeholder);
			if (error != null) {
				Skript.error(error);
				return false;
			}
			placeholders.add(placeholder);
		}

		this.registry = SkriptPlaceholders.getInstance().getRegistry();
		this.placeholders = placeholders.toArray(new String[0]);
		this.isRelational = parseResult.hasTag("relational");

		return true;
	}

	@Override
	public boolean load() {
		ParserInstance parser = getParser();
		Script script = parser.getCurrentScript();
		SectionNode source = getEntryContainer().getSource();

		parser.setCurrentEvent("placeholder request", isRelational ? RelationalPlaceholderEvent.class : PlaceholderEvent.class);

		// TODO better SkriptEvent?
		//noinspection ConstantConditions - getCurrentEventName will not be null as we set it right before
		trigger = new Trigger(script, parser.getCurrentEventName(), new SimpleEvent(), ScriptLoader.loadItems(source));
		int lineNumber = source.getLine();
		trigger.setLineNumber(lineNumber);
		trigger.setDebugLabel(script + ": line " + lineNumber);

		// see https://github.com/APickledWalrus/skript-placeholders/issues/40
		// ensure registration is on the main thread
		if (Bukkit.isPrimaryThread()) {
			for (String placeholder : placeholders) {
				registry.registerPlaceholder(plugin, placeholder, this);
			}
		} else {
			Bukkit.getScheduler().runTask(SkriptPlaceholders.getInstance(), () -> {
				for (String placeholder : placeholders) {
					registry.registerPlaceholder(plugin, placeholder, this);
				}
			});
		}

		return true;
	}

	@Override
	public void unload() {
		for (String placeholder : placeholders) {
			registry.unregisterPlaceholder(plugin, placeholder, this);
		}
	}

	@Override
	public @NotNull String toString(@Nullable Event event, boolean debug) {
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

	@Override
	@Nullable
	public String evaluate(String placeholder, @Nullable OfflinePlayer player) {
		if (isRelational) { // a relational placeholder structure cannot evaluate non-relational placeholders
			return null;
		}
		PlaceholderEvent event = new PlaceholderEvent(placeholder, player);
		trigger.execute(event);
		return event.getResult();
	}

	@Override
	public @Nullable String evaluateRelational(String placeholder, Player one, Player two) {
		if (!isRelational) { // a non-relational placeholder structure cannot evaluate relational placeholders
			return null;
		}
		RelationalPlaceholderEvent event = new RelationalPlaceholderEvent(placeholder, one, two);
		trigger.execute(event);
		return event.getResult();
	}

}
