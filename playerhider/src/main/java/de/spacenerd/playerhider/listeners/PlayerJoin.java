package de.spacenerd.playerhider.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.utils.SelectorItem;
import de.spacenerd.playerhider.utils.SelectorItem.Mode;

public class PlayerJoin implements Listener {
    private final Playerhider plugin;

    public PlayerJoin(Playerhider plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        p.getInventory().setItem(0, SelectorItem.buildItem(Mode.ALL));
    }
}
