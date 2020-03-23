package io.github.apickledwalrus.placeholderaddon.mvdwapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MvdwAPIEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	private Player player;
	private String placeholder;
	private String result;

	public MvdwAPIEvent(Player player, String placeholder) {

		// Declare the event as sync or async.
		super(!Bukkit.getServer().isPrimaryThread());

		this.placeholder = placeholder;
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

	public String getPlaceholder() {
		return placeholder;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
