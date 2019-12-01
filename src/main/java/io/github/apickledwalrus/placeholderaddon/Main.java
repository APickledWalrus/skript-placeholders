package io.github.apickledwalrus.placeholderaddon;

import ch.njol.skript.Skript;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
	private static Main instance;
	private static final boolean hasMVdW = Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI");
	private static final boolean hasPapi = Skript.classExists("me.clip.placeholderapi.expansion.PlaceholderExpansion");

	public static Main getInstance() {
		if (instance == null) {
			throw new IllegalStateException();
		}
		return instance;
	}

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
			Logger.getLogger("Minecraft").severe("[skript-placeholders] No placeholders plugin found! Disabling!");
			Logger.getLogger("Minecraft").severe("[skript-placeholders] Has PlaceholderAPI: " + hasPapi + " Has MVdWPlaceholderAPI: " + hasMVdW);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		try {
			Skript.registerAddon(this).loadClasses("io.github.apickledwalrus.placeholderaddon.skript", "events", "expressions");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
