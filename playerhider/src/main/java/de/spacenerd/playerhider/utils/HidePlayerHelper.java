package de.spacenerd.playerhider.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.spacenerd.playerhider.DatabaseManager;
import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.utils.SelectorItem.Mode;

public class HidePlayerHelper {
    // list of Players currently on the Server that are hidding all Players

    public static void hide(Plugin plugin, Player player, DatabaseManager databaseManager, List<Player> player_list, Mode mode) {
        switch (mode) {
            case ALL -> hideAll(plugin, player, player_list);
            case NONE -> hideNone(plugin, player, player_list);
            case VIPS -> hideVips(plugin, player, databaseManager, player_list);
            case FRIENDS -> hideFriends(plugin, player, databaseManager, player_list);
        }
    } 

    public static void hideFriends(Plugin plugin, Player player, DatabaseManager databaseManager, List<Player> player_list) {
        reset(plugin, player, player_list);

        List<Player> friends = new ArrayList<>();
        
        friends = databaseManager.getFriends(player.getUniqueId().toString());

        for (Player p : friends) {
            player.hidePlayer(plugin, p);
        }
    }

    public static void hideAll(Plugin plugin, Player player, List<Player> player_list) {
        reset(plugin, player, player_list);

        player_list.add(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(plugin, p);
        }
    }

    public static void hideVips(Plugin plugin, Player player, DatabaseManager databaseManager, List<Player> player_list) {
        reset(plugin, player, player_list);

        hideFriends(plugin, player, databaseManager, player_list);

        // TODO: Add Luckperms to hide all the players with Staff/Media rank 
    }

    public static void hideNone(Plugin plugin, Player player, List<Player> player_list) {
        reset(plugin, player, player_list);
    }

    public static void reset(Plugin plugin, Player player, List<Player> player_list) {
        player_list.remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, p);
        }
    }
}
