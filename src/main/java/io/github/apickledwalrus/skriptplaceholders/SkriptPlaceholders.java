package io.github.apickledwalrus.skriptplaceholders;

import ch.njol.skript.Skript;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptPlaceholders extends JavaPlugin {

	private static SkriptPlaceholders instance;

	@Override
	public void onEnable() {
		instance = this;
		for (PlaceholderPlugin plugin : PlaceholderPlugin.values()) {
			if (plugin.isInstalled()) {
				break;
			}
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
