package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerJoinOrQuit implements Listener {

    private GameManager gameManager;

    public PlayerJoinOrQuit(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent e) {
        if (gameManager.getState().equals(GameState.RESET)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "游戏正在重置");
            return;
        }
        UUID uuid = e.getUniqueId();
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);

        if (!p.isOp() && gameManager.getState().equals(GameState.PRELOBBY)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "游戏还未开始");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        gameManager.getScoreboard().addPlayer(p);

        GameWorld world = gameManager.getGameWorld();
        GameState state = gameManager.getState();
        if (state.equals(GameState.ACTIVE)) {
            //旁观者模式
            Island island = world.getIsland(p);
            if (island != null) {
                e.setJoinMessage(Color.str("&f&l" + p.getDisplayName() + " &e回来了"));
                p.sendMessage(Color.str("&a你重新连接到了这场游戏，因为你的床还在，所以你能复活"));
                gameManager.getPlayerManager().setSpectator(p);
            }
        } else if (state.equals(GameState.LOBBY) || state.equals(GameState.STARTING)) {
            if (state.equals(GameState.STARTING)) {
                BossBar bossBar = gameManager.getGameStartingTask().getBossBar();
                if (!bossBar.getPlayers().contains(p)) {
                    bossBar.addPlayer(p);
                }
            }
            e.setJoinMessage(Color.str("&f&l" + p.getDisplayName() + " &e加入了游戏！ &7(&6"
                    + Bukkit.getOnlinePlayers().size() + "&7/&a"
                    + (gameManager.getGameWorld().getMaxTeamSize() * gameManager.getGameWorld().getIslands().size()) + "&7)"));

            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFoodLevel(20);
            p.getInventory().clear();
            p.getEnderChest().clear();
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(world.getWaitingLobbyLocation());
            p.setLevel(0);
            p.setExp(0);
            gameManager.getPlayerManager().giveTeamSelector(p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        gameManager.getScoreboard().removePlayer(e.getPlayer());
    }

}
