package io.github.apickledwalrus.skriptplaceholders;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptPlaceholders extends JavaPlugin {

	private static SkriptPlaceholders instance;

	private static final boolean hasMVdW = Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI");
	private static final boolean hasPapi = Skript.classExists("me.clip.placeholderapi.expansion.PlaceholderExpansion");

	public static boolean hasMVdW() {
		return hasMVdW;
	}

	public static boolean hasPapi() {
		return hasPapi;
	}

	@Override
	public void onEnable() {
		instance = this;
		if (!hasMVdW && !hasPapi) {
			getLogger().severe("[skript-placeholders] No placeholders plugin found! Disabling!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		try {
			Skript.registerAddon(this).loadClasses("io.github.apickledwalrus.skriptplaceholders.skript.elements");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SkriptPlaceholders getInstance() {
		return instance;
	}

}
