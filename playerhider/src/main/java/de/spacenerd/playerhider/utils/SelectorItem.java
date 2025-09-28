package de.spacenerd.playerhider.utils;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import de.spacenerd.playerhider.common.Messages.Item;

public class SelectorItem {
    public enum Mode {
        ALL(0, "All"),
        VIPS(1, "VIPs and Friends"),
        FRIENDS(2, "Friends"),
        NONE(3, "Nobody");

        private static final HashMap<Integer, Mode> BY_QUEUE_VALUE = new HashMap<>();

        static {
            for (Mode m : values()) {
                BY_QUEUE_VALUE.put(m.queue_value, m);
            }
        }

        public final int queue_value;
        public final String description;

        private Mode(int queue_value, String description) {
            this.queue_value = queue_value;
            this.description = description;
        }

        public static Mode valueOfQueue(int queue_value) {
            return BY_QUEUE_VALUE.get(queue_value);
        }

        public Mode next() {
            return valueOfQueue((queue_value + 1) % values().length);
        }
    }

    private static NamespacedKey KEY;

    public static void init(JavaPlugin plugin) {
        KEY = new NamespacedKey(plugin, "playerhider_item");
    }

    public static Mode getMode(ItemStack item) {
        if (!isSelector(item)) return Mode.ALL;

        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        return Mode.valueOfQueue(data.getOrDefault(KEY, PersistentDataType.INTEGER, 0));
    }

    private static Material getMaterial(Mode mode) {
        Material material = switch (mode) {
            case NONE -> material = Material.RED_BUNDLE;
            case ALL -> material = Material.LIME_BUNDLE;
            case FRIENDS -> material = Material.CYAN_BUNDLE;
            case VIPS -> material = Material.PURPLE_BUNDLE;
        };

        return material;
    }

    public static ItemStack buildItem(Mode mode, MessageManager messageManager) {
        ItemStack item = new ItemStack(getMaterial(mode));
        ItemMeta meta = item.getItemMeta();

        meta.customName(messageManager.getMessage(Item.SELECTOR, mode));

        // making the item unique so that every playter has his own item
        // even tho sharing is caring :P
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(KEY, PersistentDataType.INTEGER, mode.queue_value);

        item.setItemMeta(meta);

        return item;
    }

    public static boolean isSelector(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        return data.has(KEY, PersistentDataType.INTEGER);
    }

    public static ItemStack progress(ItemStack item, MessageManager messageManager) {
        if (!isSelector(item)) return item;

        Mode current = getMode(item);
        return buildItem(current.next(), messageManager);
    }

    public static ItemStack create(ItemStack item, MessageManager messageManager) {
        return buildItem(Mode.ALL, messageManager);
    }
}
