package me.zjls.bedwars.tasks;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTick extends BukkitRunnable {

    private GameManager gameManager;
    private int currentSecond = 0;

    public GameTick(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        gameManager.getScoreboard().updateScoreboard();

        currentSecond += 1;
        gameManager.getGameWorld().tick(currentSecond);

/*
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            System.out.println(Bukkit.getOnlinePlayers().size());
            System.out.println(onlinePlayer.getUniqueId());
        }
        for (Island island1 : gameManager.getGameWorld().getIslands()) {
            for (Player onlinePlayer1 : Bukkit.getOnlinePlayers()) {

                if (island1.getPlayers().contains(onlinePlayer1)) {

                    System.out.println(onlinePlayer1.getDisplayName() + "是" + island1.getColor().getChatColor() + island1.getColor().getName() + "  队 §r的成员");
                }


            }
            for (Player island1Player : island1.getPlayers()) {
                System.out.println(island1.getColor().getChatColor() + island1.getColor().getName() + "  " + island1Player.getDisplayName());
            }
        }
*/

    }
}
