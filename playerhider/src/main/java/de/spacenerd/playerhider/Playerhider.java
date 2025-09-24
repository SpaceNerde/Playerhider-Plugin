package de.spacenerd.playerhider;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Playerhider extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getComponentLogger().info(Component.text("Playerhider loaded!").color(NamedTextColor.GREEN));
    }
}
