package io.github.apickledwalrus.skriptplaceholders;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptPlaceholders extends JavaPlugin {

	private static SkriptPlaceholders instance;
	private static SkriptAddon addonInstance;

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
			addonInstance = Skript.registerAddon(this);
			addonInstance.loadClasses("io.github.apickledwalrus.skriptplaceholders.skript.elements");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SkriptPlaceholders getInstance() {
		return instance;
	}

	public static SkriptAddon getAddonInstance() {
		return addonInstance;
	}

}
