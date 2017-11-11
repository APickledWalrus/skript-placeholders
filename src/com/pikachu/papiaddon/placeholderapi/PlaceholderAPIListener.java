package com.pikachu.papiaddon.placeholderapi;

import com.pikachu.papiaddon.Main;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIListener extends EZPlaceholderHook {

    private String prefix;

    public PlaceholderAPIListener(Main plugin, String prefix) {
        super(plugin, prefix);
        this.prefix = prefix;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        PlaceholderAPIEvent event = new PlaceholderAPIEvent(identifier, player, prefix);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.getResult();
    }
}
