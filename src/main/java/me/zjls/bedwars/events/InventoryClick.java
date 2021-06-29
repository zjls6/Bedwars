package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClick implements Listener {

    private GameManager gameManager;

    public InventoryClick(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        Player p = (Player) e.getWhoClicked();

        GUI gui = gameManager.getGuiManager().getOpenGUI(p);
        if (gui == null) return;

        e.setCancelled(true);

        GUI newGUI = gui.handleClick(p, e.getCurrentItem(), e.getView());

        e.getView().close();

        gameManager.getGuiManager().setGUI(p, newGUI);

    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        gameManager.getGuiManager().clear(p);
    }
}
