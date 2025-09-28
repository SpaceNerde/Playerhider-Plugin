package de.spacenerd.playerhider.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.spacenerd.playerhider.common.Messages.Message;
import de.spacenerd.playerhider.utils.SelectorItem.Mode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

// TODO: Fix this mess of a class

public class MessageManager {
    private final Plugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public Component getMessage(Message message) {
        String raw_message = plugin.getConfig().getString(message.getPath());

        Component parsed_message = miniMessage.deserialize(raw_message);

        return parsed_message;
    }

    // <mode>
    public Component getMessage(Message message, Mode mode) {
        String raw_message = plugin.getConfig().getString(message.getPath());

        Component parsed_message = miniMessage.deserialize(raw_message, Placeholder.parsed("mode", mode.description));

        return parsed_message;
    }

    // <player>
    public Component getMessage(Message message, Player player) {
        String raw_message = plugin.getConfig().getString(message.getPath());

        Component parsed_message = miniMessage.deserialize(raw_message, Placeholder.parsed("player", player.getName()));

        return parsed_message;
    }

    // <player> <friend>
    public Component getMessage(Message message, Player player, Player friend) {
        String raw_message = plugin.getConfig().getString(message.getPath());

        Component parsed_message = miniMessage.deserialize(raw_message);

        return parsed_message;
    }

    // <player>
    public Component getMessage(Message message, String player_name) {
        String raw_message = plugin.getConfig().getString(message.getPath());

        Component parsed_message = miniMessage.deserialize(raw_message);

        return parsed_message;
    }

    // <player_name> <friend_name>
    public Component getMessage(Message message, String player_name, String friend_name) {
        String raw_message = plugin.getConfig().getString(message.getPath());

        Component parsed_message = miniMessage.deserialize(raw_message);

        return parsed_message;
    }
}
