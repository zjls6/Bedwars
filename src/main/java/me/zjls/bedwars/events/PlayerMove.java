package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private GameManager gameManager;

    public PlayerMove(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Island island = gameManager.getGameWorld().getIsland(p);

        if (p.getLocation().getY() <= 0) {
            if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                gameManager.getPlayerManager().setSpectator(p);
            } else {
                p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
            }

        }
    }
}
