package de.spacenerd.playerhider;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.spacenerd.playerhider.commands.FriendCommands;
import de.spacenerd.playerhider.listeners.PlayerInteractPlayerhider;
import de.spacenerd.playerhider.listeners.PlayerJoin;
import de.spacenerd.playerhider.listeners.PlayerLeave;
import de.spacenerd.playerhider.utils.HidePlayerHelper;
import de.spacenerd.playerhider.utils.SelectorItem;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Playerhider extends JavaPlugin {
    private DatabaseManager databaseManager = new DatabaseManager(this);

    private List<Player> playersHiddingAll = new ArrayList<>();
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        databaseManager.init();
        
        SelectorItem.init(this);

        getComponentLogger().info(Component.text("Playerhider loaded!").color(NamedTextColor.GREEN));

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerLeave(this), this);
        pm.registerEvents(new PlayerInteractPlayerhider(this), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(FriendCommands.createCommand(this).build());
        });
    }

    @Override
    public void onDisable() {
        databaseManager.close();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public List<Player> getPlayersHiddingAll() {
        return playersHiddingAll;
    }
}
