package io.github.apickledwalrus.skriptplaceholders.skript;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * An event to be used by Skript for passing context during execution.
 */
public class PlaceholderEvent extends Event {

	@Nullable
	private final OfflinePlayer player;
	private final String placeholder;
	@Nullable
	private final String prefix;
	@Nullable
	private final String identifier;
	@Nullable
	private String result;

	public PlaceholderEvent(String placeholder, @Nullable OfflinePlayer player) {
		// Declare the event as sync or async.
		super(!Bukkit.getServer().isPrimaryThread());

		this.placeholder = placeholder;
		int underscorePos = placeholder.indexOf("_");
		if (underscorePos != -1) { // We can get some sort of prefix and identifier out of this placeholder
			prefix = placeholder.substring(0, underscorePos);
			identifier = placeholder.substring(underscorePos + 1);
		} else {
			prefix = null;
			identifier = null;
		}

		this.player = player;
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

	@Nullable
	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public void setResult(@Nullable String result) {
		this.result = result;
	}

	@Nullable
	public String getResult() {
		return result;
	}

	// Bukkit Stuff

	private static final HandlerList handlerList = new HandlerList();

	@Override
	@NonNull
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

}
