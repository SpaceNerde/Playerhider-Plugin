package de.spacenerd.playerhider.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import de.spacenerd.playerhider.Playerhider;
import de.spacenerd.playerhider.utils.SelectorItem;

public class PlayerInteractPlayerhider implements Listener {
    private final Playerhider plugin;

    public PlayerInteractPlayerhider(Playerhider plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (!SelectorItem.isSelector(item)) return;

        e.setUseItemInHand(Result.DENY);
        e.setCancelled(true);

        Bukkit.getScheduler().runTaskLater(plugin, ()  -> {
            player.getInventory().setItemInMainHand(SelectorItem.progress(item));
        }, 1);
    }
}
