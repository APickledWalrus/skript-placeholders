package io.github.apickledwalrus.placeholderaddon.placeholderapi;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaceholderAPIEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	private String identifier;
	private Player player;
	private String prefix;
	private String result;

	public PlaceholderAPIEvent(String identifier, Player player, String prefix, boolean isAsynchronous) {
		super(isAsynchronous);
		this.prefix = prefix;
		this.identifier = identifier;
		this.player = player;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public Player getPlayer() {
		return player;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
