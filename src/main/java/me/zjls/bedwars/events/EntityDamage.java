package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class EntityDamage implements Listener {

    private GameManager gameManager;

    public EntityDamage(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            //todo:handle
            return;
        }

        if (gameManager.getState() != GameState.ACTIVE) {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getEntity();

        Island island = gameManager.getGameWorld().getIsland(p);
        if (island == null || p.getGameMode() != GameMode.SURVIVAL) {
            e.setCancelled(true);
            return;
        }

        if (gameManager.getPlayerManager().getSpectatorList().contains(p.getUniqueId())) {
            e.setCancelled(true);
            return;
        }
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL && p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

//        if (e.getDamage() >= p.getHealth()) {
//            e.setCancelled(true);
//
//            if (!island.isBedPlaced()) {//final kill
//                p.getWorld().strikeLightningEffect(p.getLocation());
//            }
//
//            gameManager.getPlayerManager().setSpectator(p);
//
//        }
    }

    @EventHandler
    public void onDamages(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Silverfish && e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Silverfish silverfish = (Silverfish) e.getEntity();
            Island island = gameManager.getGameWorld().getIsland(p);

            if (island != null) {
                if (gameManager.getSilverfishTeamMap().get(silverfish).equals(island)) {
                    e.setCancelled(true);
                }
                if (p.getGameMode() != GameMode.SURVIVAL) {
                    e.setCancelled(true);
                }
                e.setDamage(3.0);
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Silverfish) {
            Player p = (Player) e.getEntity();

            Island island = gameManager.getGameWorld().getIsland(p);
            if (island != null && gameManager.getPlayerManager().getSpectatorList().contains(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
//        if (e.getEntity() instanceof Player || e.getDamager() instanceof Player) {
//            Player p = (Player) e.getEntity();
//            Player damager = (Player) e.getDamager();
//            BossBar bossBar = Bukkit.createBossBar("玩家" + p.getName() + "的血量为：" + p.getHealth(), BarColor.RED, BarStyle.SOLID);
//            bossBar.removeAll();
//            bossBar.setProgress(p.getHealth() / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
//            bossBar.addPlayer(damager);
//        }


    }


//
//        Player p = (Player) e.getEntity(); //被杀的人
//        Player d = (Player) e.getDamager(); //杀人的人
//
//        if (e.getDamage() > p.getHealth()) {
//            System.out.println(1);
//            d.playSound(d.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
//            d.sendMessage("§6你杀死了" + p.getDisplayName() + "！");
//        }
//
//
//    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        Island island = gameManager.getGameWorld().getIsland(p);
        if (island == null) {
            return;
        }

        if (!island.isBedPlaced()) {//final kill
            p.getWorld().strikeLightningEffect(p.getLocation());
        }

        gameManager.getPlayerManager().setSpectator(p);
        EntityDamageEvent lastDamageCause = p.getLastDamageCause();
        EntityDamageEvent.DamageCause cause = lastDamageCause.getCause();
        String deathsMessage = gameManager.getBedwars().getDeathsMessages(p, cause);
        Bedwars.bc(deathsMessage);

        p.spigot().respawn();

        e.setDeathMessage(null);
        e.setDroppedExp(0);
        e.getDrops().clear();
        Map<UUID, Integer> playerDeathCount = gameManager.getPlayerManager().getPlayerDeathCount();
        playerDeathCount.put(p.getUniqueId(), playerDeathCount.getOrDefault(p.getUniqueId(), 0) + 1);

    }


}
