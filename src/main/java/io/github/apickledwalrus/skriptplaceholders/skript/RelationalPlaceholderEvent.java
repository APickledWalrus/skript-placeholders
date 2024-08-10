package io.github.apickledwalrus.skriptplaceholders.skript;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * An event to be used by Skript for passing context regarding relational placeholders during execution.
 */
public class RelationalPlaceholderEvent extends PlaceholderEvent {

	private final Player other;

	public RelationalPlaceholderEvent(String placeholder, Player player, Player other) {
		super(placeholder, player);
		this.other = other;
	}

	@Override
	public @NotNull Player getPlayer() {
		//noinspection ConstantConditions - player is passed up as NotNull
		return super.getPlayer().getPlayer();
	}

	public Player getOther() {
		return other;
	}

}
