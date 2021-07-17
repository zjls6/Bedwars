package me.zjls.bedwars.worlds.generators;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GeneratorType {

    IRON,
    GOLD,
    DIAMOND,
    EMERALD;

    public String getName() {
        switch (this) {
            case DIAMOND:
                return "钻石";
            case EMERALD:
                return "绿宝石";
        }
        return "";
    }

    public ChatColor getChatColor() {
        switch (this) {
            case DIAMOND:
                return ChatColor.AQUA;
            case EMERALD:
                return ChatColor.GREEN;
        }
        return ChatColor.BLACK;
    }

    public ItemStack getItem() {
        switch (this) {
            case DIAMOND:
                return new ItemStack(Material.DIAMOND_BLOCK);
            case EMERALD:
                return new ItemStack(Material.EMERALD_BLOCK);
        }
        return null;
    }
}