package me.zjls.bedwars.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {

    private Map<Player, GUI> playerGUIMap = new HashMap<>();

    public GUI getOpenGUI(Player p) {
        return playerGUIMap.get(p);
    }

    public void setGUI(Player p, GUI gui) {
        if (gui == null) {
            p.closeInventory();
            return;
        }

        playerGUIMap.put(p, gui);
        p.closeInventory();
        p.openInventory(gui.getInventory());
    }

    public void clear(Player p) {

        playerGUIMap.put(p, null);
    }

}
