package io.github.apickledwalrus.placeholderaddon;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {
  private static Main instance;

  public static Main getInstance() {
    if (instance == null) {
      throw new IllegalStateException();
    }
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    try {
      Skript.registerAddon(this).loadClasses("com.apickledwalrus.papiaddon.skript", "events", "expressions");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
