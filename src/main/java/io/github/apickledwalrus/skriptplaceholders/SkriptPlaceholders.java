package io.github.apickledwalrus.skriptplaceholders;

import ch.njol.skript.Skript;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptPlaceholders extends JavaPlugin {

	private static SkriptPlaceholders instance;
	private PlaceholderRegistry registry;

	public static SkriptPlaceholders getInstance() {
		if (instance == null) {
			throw new IllegalStateException("skript-placeholders has not been initialized yet.");
		}
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		if (PlaceholderPlugin.getInstalledPlugins().isEmpty()) {
			getLogger().severe("No placeholder plugins were found. Do you have any installed? Disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		try {
			Skript.registerAddon(this).loadClasses("io.github.apickledwalrus.skriptplaceholders.skript.elements");
		} catch (IOException e) {
			getLogger().severe("A severe error occurred while trying to load the addon. Disabling...");
			getLogger().severe(e.toString());
			getServer().getPluginManager().disablePlugin(this);
		}

		this.registry = new PlaceholderRegistry(this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public PlaceholderRegistry getRegistry() {
		return registry;
	}

}
