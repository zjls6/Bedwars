package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinOrQuit implements Listener {

    private GameManager gameManager;

    public PlayerJoinOrQuit(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        gameManager.getScoreboard().addPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        gameManager.getScoreboard().removePlayer(e.getPlayer());
    }

}
