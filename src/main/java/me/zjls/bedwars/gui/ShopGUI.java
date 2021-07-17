package me.zjls.bedwars.gui;

import me.zjls.bedwars.games.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ShopGUI implements GUI {

    private GameManager gameManager;
    private Inventory inventory;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public GUI handleClick(Player p, ItemStack item, InventoryView inventoryView) {
        return null;
    }

    @Override
    public boolean isInventory(InventoryView view) {
        return false;
    }
}
