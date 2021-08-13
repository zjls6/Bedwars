package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.gui.types.TrapType;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerMove implements Listener {

    private GameManager gameManager;

    public PlayerMove(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Island playerIsland = gameManager.getGameWorld().getIsland(p);

        if (playerIsland == null) {
            return;
        }
        if (p.getLocation().getY() <= 1) {
            if (gameManager.getPlayerInGame().contains(p.getUniqueId())) {
                p.setHealth(1.0);
            } else {
                p.teleport(gameManager.getGameWorld().getWaitingLobbyLocation());
                return;
            }
        }
        if (!gameManager.getState().equals(GameState.ACTIVE)) {
            return;
        }

        List<Island> islands = gameManager.getGameWorld().getIslands();
        Optional<Island> inBaseIsland = islands.stream().filter(island -> island.isInBase(p)).findFirst();
        if (!inBaseIsland.isPresent()) {
            return;
        }
        Island island = inBaseIsland.get();

        List<TrapType> trapList = island.getTrapList();
        if (trapList.isEmpty()) {
            p.sendMessage("empty");
            return;
        }
        if (island == playerIsland) {
            p.sendMessage("ownbase");
            return;
        }
        p.sendMessage(">0");
        p.sendMessage("=" + trapList.size());
        TrapType trapType = trapList.get(0);
        p.sendMessage(trapType.name());

        switch (trapType) {
            case TRAP:
                if (p.hasPotionEffect(PotionEffectType.BLINDNESS) || p.hasPotionEffect(PotionEffectType.SLOW)) {
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8 * 20, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8 * 20, 0));
                break;
            case ATTACK:
                for (UUID uuid : island.getPlayers()) {
                    if (island.getPlayers().isEmpty()) {
                        return;
                    }
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED) || player.hasPotionEffect(PotionEffectType.JUMP)) {
                        return;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10 * 20, 1));
                }
                break;
            case WARN:
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                for (UUID uuid : island.getPlayers()) {
                    if (island.getPlayers().isEmpty()) {
                        return;
                    }
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        return;
                    }
                    player.sendMessage(Color.str(playerIsland.getColor().getChatColor() + playerIsland.getColor().getName() + "队 &6" + p.getName() + " &c闯入了基地！"));
                    BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(gameManager.getPlugin(), () ->
                            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1), 0, 2);
                    Bukkit.getScheduler().runTaskLater(gameManager.getPlugin(), bukkitTask::cancel, 20 * 5);
                }
                break;
            case MINING:
                if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                    return;
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0));
                break;
        }
        trapList.remove(0);


//        for (Island island : islands) {
//            if (island.isInBase(p)) {
//                p.sendMessage("base");
//                if (island == playerIsland) {
//                    p.sendMessage("ownbase");
//                    return;
//                }
//                List<TrapType> trapList = island.getTrapList();
//                if (trapList.isEmpty()) {
//                    p.sendMessage("empty");
//                    return;
//                }
//                p.sendMessage(">0");
//                if (trapList.size() == 1) {
//                    p.sendMessage("=1");
//                    TrapType trapType = trapList.get(0);
//                    p.sendMessage(trapType.name());
//                    switch (trapType) {
//                        case TRAP:
//                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8, 0));
//                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8, 0));
//                            trapList.remove(0);
//                            break;
//                        case ATTACK:
//                            break;
//                        case WARN:
//                            break;
//                        case MINING:
//                            break;
//                    }
//                    trapList.remove(0);
//                }
//            }
//        }
    }
}
