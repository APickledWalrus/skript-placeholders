package io.github.apickledwalrus.skriptplaceholders.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.eclipse.jdt.annotation.Nullable;

public class PlaceholderEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	private final String placeholder;
	private final String prefix;
	private final String identifier;
	private String result;

	private final OfflinePlayer player;

	public PlaceholderEvent(String placeholder, OfflinePlayer player) {
		// Declare the event as sync or async.
		super(!Bukkit.getServer().isPrimaryThread());

		this.placeholder = placeholder;
		String[] split = placeholder.split("_");
		prefix = split.length >= 1 ? split[0] : null;
		identifier = split.length >= 2 ? split[1] : null;

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

	@Nullable
	public String getPrefix() {
		return prefix;
	}

	@Nullable
	public String getIdentifier() {
		return identifier;
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
