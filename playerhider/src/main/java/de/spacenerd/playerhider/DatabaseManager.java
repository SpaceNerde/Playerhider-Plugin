package de.spacenerd.playerhider;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    private final Plugin plugin;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        FileConfiguration file_config = plugin.getConfig();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/" + file_config.getString("db.database")); // Address of your running MySQL database
        config.setUsername(file_config.getString("db.username")); // Username
        config.setPassword(file_config.getString("db.password")); // Password
        config.setMaximumPoolSize(10); // Pool size defaults to 10

        config.addDataSourceProperty("", ""); // MISC settings to add
        HikariDataSource dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection()) {
            // Use a try-with-resources here to autoclose the connection.
            PreparedStatement sql = connection.prepareStatement("SQL");
            // Execute statement
        } catch (Exception e) {
            // Handle any exceptions that arise from getting / handing the exception.
        }
    }
}

