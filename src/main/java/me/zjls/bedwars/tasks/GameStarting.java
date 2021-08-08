package me.zjls.bedwars.tasks;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStarting extends BukkitRunnable {

    private GameManager gameManager;
    private BossBar bossBar;

    private int time = 20;

    public GameStarting(GameManager gameManager) {
        this.gameManager = gameManager;
        this.bossBar = Bukkit.createBossBar(Color.str("&a游戏将在 &6&l" + time + " &a秒内开始"), BarColor.PURPLE, BarStyle.SEGMENTED_20);
        this.bossBar.setProgress(1.0);
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.bossBar.addPlayer(p);
            //p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
        }
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public int getTime() {
        return time;
    }

    @Override
    public void run() {
        gameManager.getScoreboard().updateScoreboard();

        if (time == 0) {
            gameManager.setState(GameState.ACTIVE);
            return;
        }

        this.bossBar.setProgress(time / 20.0f);
        this.bossBar.setTitle(Color.str("&a游戏将在 &6&l" + time + " &a秒内开始"));


        if (time <= 10 || time == 20) {
            if (time < 1) {
                time--;
                return;
            }
            Bedwars.bc(Color.str("&a游戏将在 &6&l" + time + " &a秒内开始"));
            if (time <= 5) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    if (time == 1) {
                        p.sendTitle("", Color.str("&c") + time, 20, 20, 0);
                    } else if (time <= 3) {
                        p.sendTitle("", Color.str("&c") + time, 20, 20, 0);
                    } else {
                        p.sendTitle("", Color.str("&e") + time, 20, 20, 20);
                    }

                }
            }
        }

        time--;
    }

    @Override
    public void cancel() {
        super.cancel();
        this.bossBar.removeAll();
    }
}
