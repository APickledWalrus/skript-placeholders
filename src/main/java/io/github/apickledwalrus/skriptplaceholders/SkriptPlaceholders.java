package io.github.apickledwalrus.skriptplaceholders;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderRegistry;
import org.bukkit.plugin.Plugin;
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
		Plugin skript = getServer().getPluginManager().getPlugin("Skript");
		if (skript == null || !skript.isEnabled()) {
			getLogger().severe("Could not find Skript. Make sure that you have it installed and that it is properly loaded. Disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (Skript.getVersion().isSmallerThan(new Version(2, 7, 3))) {
			getLogger().severe("You are running an unsupported version of Skript. Please update to at least Skript 2.7.3. Disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		if (PlaceholderPlugin.getInstalledPlugins().isEmpty()) {
			getLogger().severe("No placeholder plugins were found. Do you have any installed? Disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		instance = this;
		registry = new PlaceholderRegistry(this);

		try {
			Skript.registerAddon(this).loadClasses("io.github.apickledwalrus.skriptplaceholders.skript.elements");
		} catch (IOException e) {
			getLogger().severe("A severe error occurred while trying to load the addon. Disabling...");
			getLogger().severe(e.toString());
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		instance = null;
		registry = null;
	}

	public PlaceholderRegistry getRegistry() {
		return registry;
	}

}
