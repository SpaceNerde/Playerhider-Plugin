package de.spacenerd.playerhider.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.spacenerd.playerhider.DatabaseManager;
import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.utils.SelectorItem.Mode;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.matcher.NodeMatcher;
import net.luckperms.api.node.types.InheritanceNode;

public class HidePlayerHelper {
    // list of Players currently on the Server that are hidding all Players

    public static void hide(Playerhider plugin, Player player, Mode mode) {
        switch (mode) {
            case ALL -> hideAll(plugin, player);
            case NONE -> hideNone(plugin, player);
            case VIPS -> hideVips(plugin, player);
            case FRIENDS -> hideFriends(plugin, player);
        }
    } 

    public static void hideFriends(Playerhider plugin, Player player) {
        reset(plugin, player);

        List<Player> friends = new ArrayList<>();
        
        friends = plugin.getDatabaseManager().getFriends(player.getUniqueId().toString());

        for (Player p : friends) {
            player.hidePlayer(plugin, p);
        }
    }

    public static void hideAll(Playerhider plugin, Player player) {
        reset(plugin, player);

        plugin.getPlayersHiddingAll().add(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(plugin, p);
        }
    }

    // hides all players that are in the Luckperms group vip, just add staff and/or media rank players to the vip group
    public static void hideVips(Playerhider plugin, Player player) {
        reset(plugin, player);

        hideFriends(plugin, player);

        // TODO: Add Luckperms to hide all the players with Staff/Media rank 
        if (plugin.getProvider() == null) return;

        LuckPerms luckPerms = plugin.getProvider().getProvider();

        CompletableFuture<List<User>> vip =  getUsersInGroup("vip", luckPerms);

        vip.thenAcceptAsync(list -> {
            for (User user : list) {
                Player p = plugin.getServer().getPlayer(user.getUniqueId());

                player.hidePlayer(plugin, p);
            }
        });
    }

    public static void hideNone(Playerhider plugin, Player player) {
        reset(plugin, player);
    }

    public static void reset(Playerhider plugin, Player player) {
        plugin.getPlayersHiddingAll().remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, p);
        }
    }

    // Credit to lucko from LuckPerms Issue 2949 (thx mate)
    // Love Performance
    private static CompletableFuture<List<User>> getUsersInGroup(String groupName, LuckPerms luckPerms) {
        NodeMatcher<InheritanceNode> matcher = NodeMatcher.key(InheritanceNode.builder(groupName).build());

        return luckPerms.getUserManager().searchAll(matcher).thenComposeAsync(results -> {
            List<CompletableFuture<User>> users = new ArrayList<>();
            return CompletableFuture.allOf(
                results.keySet().stream()
                .map(uuid -> luckPerms.getUserManager().loadUser(uuid))
                .peek(users::add)
                .toArray(CompletableFuture[]::new)
            ).thenApply(x -> users.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
            );
        });
    }

}
