package me.zjls.bedwars.gui;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.ItemBuilder;
import me.zjls.bedwars.worlds.IslandColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class SetupIslandGUI implements GUI {

    private GameManager gameManager;
    private Inventory inventory;

    public SetupIslandGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        inventory = Bukkit.createInventory(null, 27, getName());

        for (IslandColor color : IslandColor.values()) {
            inventory.addItem(
                    new ItemBuilder(color.getTeamWool()).setName(color.getName() + " 队").toItemStack()
            );
        }

        inventory.setItem(26, new ItemBuilder(Material.BARRIER, 1).setName("&c关闭").toItemStack());

    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return "队伍选择器";
    }

    @Override
    public GUI handleClick(Player p, ItemStack item, InventoryView inventoryView) {
        if (!gameManager.getSetupManager().isSetuping(p)) {
            return null;
        }

        IslandColor clickColor = null;
        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        for (IslandColor color : IslandColor.values()) {
            if (itemName.contains(ChatColor.stripColor(color.getName()))) {
                clickColor = color;
                break;
            }
        }

        if (clickColor != null) {
            gameManager.getSetupManager().teamSetup(p, clickColor);
        } else {
            gameManager.getSetupManager().worldSetup(p, gameManager.getSetupManager().getWorld(p));
        }
        return null;
    }

    @Override
    public boolean isInventory(InventoryView view) {
        return view.getTitle().equals(getName());
    }
}
