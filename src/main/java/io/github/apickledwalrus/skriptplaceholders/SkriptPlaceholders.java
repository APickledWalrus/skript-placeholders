package io.github.apickledwalrus.skriptplaceholders;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderPlugin;
import io.github.apickledwalrus.skriptplaceholders.placeholder.PlaceholderRegistry;
import io.github.apickledwalrus.skriptplaceholders.skript.elements.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.Consumer;

public class SkriptPlaceholders extends JavaPlugin implements AddonModule {

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
		Version minimumSupportedVersion = new Version(2, 14, 3);
		if (skript == null) {
			// Skript doesn't exist within the server plugins folder
			getLogger().severe("Could not find Skript! Make sure you have it installed. Disabling...");
			getLogger().severe("skript-placeholders requires Skript " + minimumSupportedVersion + " or newer! Download Skript releases at https://github.com/SkriptLang/Skript/releases");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else if (!skript.isEnabled()) {
			// Skript is disabled on the server
			getLogger().severe("Skript failed to properly enable and is disabled on the server. Disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else if (Skript.getVersion().isSmallerThan(minimumSupportedVersion)) {
			// Current Skript version is below minimum required version
			getLogger().severe("You're running an unsupported Skript version (" + Skript.getVersion() + ")! Disabling...");
			getLogger().severe("skript-placeholders requires Skript " + minimumSupportedVersion + " or newer! Download Skript releases at https://github.com/SkriptLang/Skript/releases");
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

		SkriptAddon addon = Skript.instance().registerAddon(SkriptPlaceholders.class, "skript-placeholders");
		addon.loadModules(this);
	}

	@Override
	public void onDisable() {
		instance = null;
		registry = null;
	}

	public PlaceholderRegistry getRegistry() {
		return registry;
	}

	@Override
	public void load(SkriptAddon addon) {
		register(addon,
			ExprPlaceholder::register,
			ExprPlaceholderResult::register,
			ExprPlaceholderValue::register,
			ExprRelationalPlaceholderPlayers::register,
			StructCustomPlaceholder::register
		);
	}

	@SafeVarargs
	private static void register(SkriptAddon addon, Consumer<SyntaxRegistry>... consumers) {
		SyntaxRegistry syntaxRegistry = addon.syntaxRegistry();
		for (Consumer<SyntaxRegistry> consumer : consumers) {
			consumer.accept(syntaxRegistry);
		}
	}

	@Override
	public String name() {
		return "skript-placeholders";
	}

}
