package de.spacenerd.playerhider;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.spacenerd.playerhider.listeners.PlayerInteractPlayerhider;
import de.spacenerd.playerhider.listeners.PlayerJoin;
import de.spacenerd.playerhider.utils.SelectorItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Playerhider extends JavaPlugin {

    DatabaseManager databaseManager = new DatabaseManager(this);
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        databaseManager.connect();
        
        SelectorItem.init(this);

        getComponentLogger().info(Component.text("Playerhider loaded!").color(NamedTextColor.GREEN));

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerInteractPlayerhider(this), this);
    }
}
