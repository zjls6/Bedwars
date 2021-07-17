package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.gui.GUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    private GameManager gameManager;

    public InventoryClick(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
            if (p.getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
            }
        }

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        p.sendMessage("1");
        if (!item.hasItemMeta()) return;
        p.sendMessage("2");
        String name = item.getType().name();

        if (name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")) {
            e.setCancelled(true);
        }
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("队")) {
            e.setCancelled(true);
        }
        GUI gui = gameManager.getGuiManager().getOpenGUI(p);

        p.sendMessage("3");
        if (gui == null) {
            p.sendMessage("4");
            return;
        }

        e.setCancelled(true);
        p.sendMessage("5");
        GUI newGUI = gui.handleClick(p, item, e.getView());
        p.sendMessage("6");

        e.getView().close();
        p.sendMessage("7");

        gameManager.getGuiManager().setGUI(p, newGUI);

        p.sendMessage("8");
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        p.sendMessage("11");
        if (e.getInventory().getType().equals(InventoryType.CHEST)) {
            gameManager.getGuiManager().clear(p);
            p.sendMessage("chest");
        }
        p.sendMessage("12");
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent e) {
        if (gameManager.getState().equals(GameState.LOBBY) || gameManager.getState().equals(GameState.STARTING)) {
            ItemStack item = e.getItem();
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
//                    if (item.getType().toString().contains("WOOL")) {
                    e.setCancelled(true);
                    gameManager.getPlugin().getLogger().info("InventoryMoveItemEvent");
//                    }
                }
            }

        }
    }
}
