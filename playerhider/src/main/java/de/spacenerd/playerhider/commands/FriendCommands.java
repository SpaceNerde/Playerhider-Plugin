package de.spacenerd.playerhider.commands;

import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import de.spacenerd.playerhider.DatabaseManager;
import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.common.Messages.Error;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class FriendCommands {
    private static Playerhider plugin;

    public static LiteralArgumentBuilder<CommandSourceStack> createCommand(Playerhider plugin) {
        FriendCommands.plugin = plugin;

        return Commands.literal("friend")
            .then(Commands.literal("add").then(Commands.argument("player", StringArgumentType.string()).executes(FriendCommands::addPlayer)))
            .then(Commands.literal("remove").then(Commands.argument("player", StringArgumentType.string()).executes(FriendCommands::removePlayer)));
    }

    private static int addPlayer(CommandContext<CommandSourceStack> ctx) {
        String player_name = StringArgumentType.getString(ctx, "player");
        Player player = (Player) ctx.getSource().getSender();

        Player friend = plugin.getServer().getPlayer(player_name);

        if (friend == null) {
            player.sendMessage(plugin.getMessageManager().getMessage(Error.NO_PLAYER_FOUND));
        }

        DatabaseManager databaseManager = plugin.getDatabaseManager();

        databaseManager.addFriends(player.getUniqueId().toString(), friend.getUniqueId().toString());

        return Command.SINGLE_SUCCESS;
    }

    private static int removePlayer(CommandContext<CommandSourceStack> ctx) {
        String player_name = StringArgumentType.getString(ctx, "player");
        Player player = (Player) ctx.getSource().getSender();

        Player friend = plugin.getServer().getPlayer(player_name);

        if (friend == null) {
            player.sendMessage(plugin.getMessageManager().getMessage(Error.NO_PLAYER_FOUND));
        }

        DatabaseManager databaseManager = plugin.getDatabaseManager();

        databaseManager.removeFriends(player.getUniqueId().toString(), friend.getUniqueId().toString());

        return Command.SINGLE_SUCCESS;
    }
}
