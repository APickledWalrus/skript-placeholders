package io.github.apickledwalrus.skriptplaceholders.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaceholderEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	private String placeholder;
	private String result;

	private OfflinePlayer player;
	public PlaceholderEvent(String placeholder, OfflinePlayer player) {

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

	public String getPlaceholder() {
		return this.placeholder;
	}

	public String getPrefix() {
		return getPlaceholder().split("_")[0];
	}

	public String getIdentifier() {
		return getPlaceholder().split("_")[1];
	}

	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

}
