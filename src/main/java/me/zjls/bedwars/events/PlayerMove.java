package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.gui.types.TrapType;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerMove implements Listener {

    private GameManager gameManager;

    public PlayerMove(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Island playerIsland = gameManager.getGameWorld().getIsland(p);

        if (p.getLocation().getY() <= 1) {
            if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                p.setHealth(1.0);
            } else {
                p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
            }
        }
        if (gameManager.getState().equals(GameState.ACTIVE)) {
            for (Island island : gameManager.getGameWorld().getIslands()) {
                if (island.isInBase(p)) {
                    p.sendMessage("base");
                    if (island == playerIsland) {
                        p.sendMessage("ownbase");
                        return;
                    }
                    List<TrapType> trapList = island.getTrapList();
                    if (trapList.size() > 0) {
                        p.sendMessage(">0");
                        if (trapList.size() == 1) {
                            p.sendMessage("=1");
                            TrapType trapType = trapList.get(0);
                            p.sendMessage(trapType.name());
                            switch (trapType) {
                                case TRAP:
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8, 0));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8, 0));
                                    trapList.remove(0);
                                    break;
                                case ATTACK:
                                    break;
                                case WARN:
                                    break;
                                case MINING:
                                    break;
                            }
                            trapList.remove(0);
                        }
                    }
                }
            }
        }

    }
}
