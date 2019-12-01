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
import io.github.apickledwalrus.placeholderaddon.placeholderapi.PlaceholderAPIEvent;
import io.github.apickledwalrus.placeholderaddon.placeholderapi.PlaceholderAPIListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("On PlaceholderAPI Placeholder Request")
@Description("Called whenever a placeholder is requested by PlaceholderAPI.")
@Examples("INSERT EXAMPLE")
@Since("1.0")
public class EvtPapiPlaceholderRequest extends SkriptEvent {

  static {
    if (!Main.hasPapi()) {
      Skript.registerEvent("Placeholder Request", EvtPapiPlaceholderRequest.class, PlaceholderAPIEvent.class, "(placeholder[api]|papi) request with [the] prefix %string%");
      EventValues.registerEventValue(PlaceholderAPIEvent.class, Player.class, new Getter<Player, PlaceholderAPIEvent>() {
        @Override
        public Player get(PlaceholderAPIEvent e) {
          return e.getPlayer();
        }
      }, 0);
    }
  }

  private String prefix;

  @SuppressWarnings("unchecked")
  @Override
  public boolean init(final Literal<?>[] args, final int matchedPattern, final SkriptParser.ParseResult parser) {
    prefix = ((Literal<String>) args[0]).getSingle();
    if ("".equals(prefix)) {
      Skript.error(prefix + " is not a valid placeholder", ErrorQuality.SEMANTIC_ERROR);
      return false;
    }
    new PlaceholderAPIListener(Main.getInstance(), prefix).register();
    return true;
  }

  @Override
  public boolean check(final Event e) {
    return ((PlaceholderAPIEvent) e).getPrefix().equalsIgnoreCase(prefix);
  }

  @Override
  public String toString(Event e, boolean debug) {
    return "placeholder request" + (prefix != null ? ("with prefix \"" + prefix + "\"") : "");
  }
}
