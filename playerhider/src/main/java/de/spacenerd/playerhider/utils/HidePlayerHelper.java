package de.spacenerd.playerhider.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.spacenerd.playerhider.DatabaseManager;

public class HidePlayerHelper {
    // list of Players currently on the Server that are hidding all Players
    private List<Player> hiddingAll = new ArrayList<>();

    public void hideFriends(Plugin plugin, Player player, DatabaseManager databaseManager) {
        reset(plugin, player);

        List<Player> friends = new ArrayList<>();
        
        friends = databaseManager.getFriends(player.getUniqueId().toString());

        for (Player p : friends) {
            player.hidePlayer(plugin, p);
        }
    }

    public void hideAll(Plugin plugin, Player player) {
        reset(plugin, player);

        hiddingAll.add(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(plugin, p);
        }
    }

    public void hideVips(Plugin plugin, Player player, DatabaseManager databaseManager) {
        reset(plugin, player);

        hideFriends(plugin, player, databaseManager);

        // TODO: Add Luckperms to hide all the players with Staff/Media rank 
    }

    public void hideNone(Plugin plugin, Player player) {
        reset(plugin, player);
    }

    public void reset(Plugin plugin, Player player) {
        hiddingAll.remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, p);
        }
    }
}
