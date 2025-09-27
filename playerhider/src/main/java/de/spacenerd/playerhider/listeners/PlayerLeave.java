package de.spacenerd.playerhider.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.utils.SelectorItem;
import de.spacenerd.playerhider.utils.SelectorItem.Mode;

public class PlayerLeave implements Listener {
    private final Playerhider plugin;

    public PlayerLeave(Playerhider plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        plugin.getHelper().reset(plugin, p);
    }
}
