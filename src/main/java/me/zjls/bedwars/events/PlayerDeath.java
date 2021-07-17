package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDeath implements Listener {

    private GameManager gameManager;

    public PlayerDeath(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            //todo:handle
            if (e.getEntity() instanceof Villager || e.getEntity() instanceof Skeleton) {
                e.setCancelled(true);
            }

            return;
        }
        if (gameManager.getState() != (GameState.ACTIVE)) {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getEntity();
        Island island = gameManager.getGameWorld().getIsland(p);
        if (island == null || p.getGameMode() != GameMode.SURVIVAL) {
            e.setCancelled(true);
            return;
        }

        if (e.getFinalDamage() >= p.getHealth()) {
            e.setCancelled(true);

            if (!island.isBedPlaced()){
                p.getWorld().strikeLightningEffect(p.getLocation());
            }

            gameManager.getPlayerManager().setSpectator(p);


        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || (e.getDamager() instanceof Player)) {

            return;
        }
        Player p = (Player) e.getEntity(); //被杀的人
        Player d = (Player) e.getDamager(); //杀人的人

        if (e.getFinalDamage() > p.getHealth()) {
            System.out.println(1);
            d.playSound(d.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            d.sendMessage("§6你杀死了" + p.getDisplayName() + "！");
        }


    }
}
