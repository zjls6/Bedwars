package me.zjls.bedwars.gui;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.teams.TeamColor;
import me.zjls.bedwars.utils.ItemBuilder;
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

        for (TeamColor color : TeamColor.values()) {
            inventory.addItem(
                    new ItemBuilder(color.getTeamWool()).setName(color.getName()).toItemStack()
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

        TeamColor clickColor = null;

        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        for (TeamColor color : TeamColor.values()) {
            if (itemName.equals(ChatColor.stripColor(color.getName()))) {
                clickColor = color;
                p.sendMessage(color.getName());
                break;
            }
        }

        if (clickColor != null) {
            gameManager.getSetupManager().teamSetup(p, clickColor);
            p.sendMessage("teamsetup");
        } else {
            gameManager.getSetupManager().worldSetup(p, gameManager.getSetupManager().getWorld(p));
            p.sendMessage("worldsetup");
        }

        return null;
    }

    @Override
    public boolean isInventory(InventoryView view) {
        return view.getTitle().equals(getName());
    }
}
