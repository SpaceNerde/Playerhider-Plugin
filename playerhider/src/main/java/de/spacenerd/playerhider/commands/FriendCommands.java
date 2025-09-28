package de.spacenerd.playerhider.commands;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import de.spacenerd.playerhider.DatabaseManager;
import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.common.Messages.Error;
import de.spacenerd.playerhider.common.Messages.Info;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class FriendCommands {
    private static Playerhider plugin;

    public static LiteralArgumentBuilder<CommandSourceStack> createCommand(Playerhider plugin) {
        FriendCommands.plugin = plugin;

        return Commands.literal("friend")
            .then(Commands.literal("add").then(Commands.argument("player", StringArgumentType.string())
                .suggests(FriendCommands::getPlayerSuggestions)
                .executes(FriendCommands::addPlayer)))
            .then(Commands.literal("remove").then(Commands.argument("player", StringArgumentType.string())
                .suggests(FriendCommands::getPlayerSuggestions)
                .executes(FriendCommands::removePlayer)));
    }

    private static int addPlayer(CommandContext<CommandSourceStack> ctx) {
        String player_name = StringArgumentType.getString(ctx, "player");
        Player player = (Player) ctx.getSource().getSender();

        Player friend = plugin.getServer().getPlayer(player_name);

        if (friend == null) {
            player.sendMessage(plugin.getMessageManager().getMessage(Error.NO_PLAYER_FOUND));

            return 0;
        }

        DatabaseManager databaseManager = plugin.getDatabaseManager();

        databaseManager.addFriends(player.getUniqueId().toString(), friend.getUniqueId().toString());

        player.sendMessage(plugin.getMessageManager().getMessage(Info.FRIEND_ADDED));

        return Command.SINGLE_SUCCESS;
    }

    private static int removePlayer(CommandContext<CommandSourceStack> ctx) {
        String player_name = StringArgumentType.getString(ctx, "player");
        Player player = (Player) ctx.getSource().getSender();

        Player friend = plugin.getServer().getPlayer(player_name);

        if (friend == null) {
            player.sendMessage(plugin.getMessageManager().getMessage(Error.NO_PLAYER_FOUND));

            return 0;
        }

        DatabaseManager databaseManager = plugin.getDatabaseManager();

        databaseManager.removeFriends(player.getUniqueId().toString(), friend.getUniqueId().toString());

        player.sendMessage(plugin.getMessageManager().getMessage(Info.FRIEND_REMOVED));

        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> getPlayerSuggestions(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
            .forEach(builder::suggest);

        return builder.buildFuture();
    }
}
