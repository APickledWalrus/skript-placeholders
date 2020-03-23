package io.github.apickledwalrus.placeholderaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

	private static Main instance;
	private static SkriptAddon addonInstance;

	private static final boolean hasMVdW = Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI");
	private static final boolean hasPapi = Skript.classExists("me.clip.placeholderapi.expansion.PlaceholderExpansion");

	@Override
	public void onEnable() {
		instance = this;
		if (!hasMVdW && !hasPapi) {
			Logger.getLogger("Minecraft").severe("[skript-placeholders] No placeholders plugin found! Disabling!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		try {
			addonInstance = Skript.registerAddon(this);
			addonInstance.loadClasses("io.github.apickledwalrus.placeholderaddon.skript", "events", "expressions");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Main getInstance() {
		if (instance == null) {
			throw new IllegalStateException("The plugin's instance should not be null.");
		}
		return instance;
	}

	public static SkriptAddon getAddonInstance() {
		return addonInstance;
	}

	public static boolean hasMVdW() {
		return hasMVdW;
	}

	public static boolean hasPapi() {
		return hasPapi;
	}

}
