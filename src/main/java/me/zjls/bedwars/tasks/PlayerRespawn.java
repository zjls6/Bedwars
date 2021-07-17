package me.zjls.bedwars.tasks;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerRespawn implements Runnable {

    private GameManager gameManager;
    private Island island;
    private Player p;
    private int tick = 0;

    public PlayerRespawn(GameManager gameManager, Player p, Island island) {
        this.gameManager = gameManager;
        this.p = p;
        this.island = island;

        island.getAbsolutelyAlive().add(p.getUniqueId());
    }

    @Override
    public void run() {
        if (tick == 5) {
            //todo:respawn
            p.sendTitle(Color.str("&a已重生！"), "", 0, 20, 10);
            island.getAbsolutelyAlive().remove(p.getUniqueId());
            gameManager.getPlayerManager().setPlaying(p);

            return;
        }

        p.sendTitle(Color.str("&c你死了！"), Color.str("&a在 " + (5 - tick) + " 秒内重生"), 0, 20, 0);

        tick += 1;

    }
}
