package de.spacenerd.playerhider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    private final Plugin plugin;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private HikariDataSource dataSource;

    public void connect() {
        FileConfiguration file_config = plugin.getConfig();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/" + file_config.getString("db.database")); // Address of your running MySQL database
        config.setUsername(file_config.getString("db.username")); // Username
        config.setPassword(file_config.getString("db.password")); // Password
        config.setMaximumPoolSize(10); // Pool size defaults to 10

        config.addDataSourceProperty("", ""); // MISC settings to add
        this.dataSource = new HikariDataSource(config);
    }

    public void close() {
        if (dataSource == null || dataSource.isClosed()) return;
        dataSource.close();
    }

    public void init() {
        if (this.dataSource == null) connect();

        try (Connection connection = this.dataSource.getConnection()) {
            connection.prepareStatement(
                """
                CREATE TABLE IF NOT EXISTS friends (
                    player_uuid         char(36) NOT NULL,
                    friend_uuid         char(36) NOT NULL,        
                    PRIMARY KEY (player_uuid, friend_uuid)
                );
                """
            ).executeUpdate();

            connection.close();

        } catch (Exception e) {
            plugin.getLogger().severe("Database error: " + e.getMessage());
        }
    }

    public boolean hasPlayer(String player_uuid) {
        if (this.dataSource == null) return false;

        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                """
                SELECT * 
                FROM friends 
                WHERE player_uuid = ?;
                """
            );

            statement.setString(1, player_uuid);

            ResultSet result = statement.executeQuery();
            boolean hasPlayer = result.next();

            connection.close();
            statement.close();

            return hasPlayer;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Database error: " + e.getMessage());

            return false;
        }
    }

    public List<Player> getFriends(String player_uuid) {
        if (this.dataSource == null) return Collections.emptyList();

        List<Player> friends = new ArrayList<>();

        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                """
                SELECT friend_uuid FROM friends WHERE player_uuid = ?;
                """
            );

            statement.setString(1, player_uuid);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String uuid = result.getString("friend_uuid");
                Player friend = plugin.getServer().getPlayer(UUID.fromString(uuid));

                if (friend == null) continue;

                friends.add(friend);
            }

            connection.close();
            statement.close();

            return friends;

        } catch (Exception e) {
            plugin.getLogger().severe("Database error: " + e.getMessage());

            return Collections.emptyList();
        }
    }

    public void addFriends(String player_uuid, String friend_uuid) {
        if (this.dataSource == null) return;

        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                """
                INSERT IGNORE INTO friends(player_uuid, friend_uuid) VALUES(?, ?);
                """
            );
            
            statement.setString(1, player_uuid);
            statement.setString(2, friend_uuid);

            statement.executeUpdate();

            connection.close();
            statement.close();

        } catch (Exception e) {
            plugin.getLogger().severe("Database error: " + e.getMessage());
        }
    }

    public void removeFriends(String player_uuid, String friend_uuid) {
        if (this.dataSource == null) return;

        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                """
                DELETE FROM friends
                WHERE (player_uuid = ? AND friend_uuid = ?)
                """
            );

            statement.setString(1, player_uuid);
            statement.setString(2, friend_uuid);
            
            statement.executeUpdate();

            connection.close();
            statement.close();

        } catch (Exception e) {
            plugin.getLogger().severe("Database error: " + e.getMessage());
        }
    }
}
