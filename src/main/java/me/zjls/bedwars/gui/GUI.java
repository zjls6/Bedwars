package me.zjls.bedwars.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public interface GUI {

    Inventory getInventory();

    String getName();

    GUI handleClick(Player p, ItemStack item, InventoryView inventoryView);

    boolean isInventory(InventoryView view);

}
