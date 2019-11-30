package com.apickledwalrus.papiaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

public class Main extends JavaPlugin {
    private static Main instance;
    private static SkriptAddon addon;

    public Main() {
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onEnable() {
        try {
            getAddonInstance().loadClasses("com.apickledwalrus.papiaddon.skript", "events", "expressions");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        if (addon == null) {
            addon = Skript.registerAddon(getInstance());
        }
        return addon;
    }

}
