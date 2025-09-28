package de.spacenerd.playerhider;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.spacenerd.playerhider.commands.FriendCommands;
import de.spacenerd.playerhider.listeners.PlayerInteractPlayerhider;
import de.spacenerd.playerhider.listeners.PlayerJoin;
import de.spacenerd.playerhider.listeners.PlayerLeave;
import de.spacenerd.playerhider.utils.MessageManager;
import de.spacenerd.playerhider.utils.SelectorItem;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;

public class Playerhider extends JavaPlugin {
    private DatabaseManager databaseManager = new DatabaseManager(this);
    private MessageManager messageManager = new MessageManager(this);

    private RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

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

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public List<Player> getPlayersHiddingAll() {
        return playersHiddingAll;
    }

    public RegisteredServiceProvider<LuckPerms> getProvider() {
        return provider;
    }
}
