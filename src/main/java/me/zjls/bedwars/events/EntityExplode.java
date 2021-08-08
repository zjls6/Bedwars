package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {

    private GameManager gameManager;

    public EntityExplode(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {

        Entity entity = e.getEntity();

        if (entity.getType().equals(EntityType.FIREBALL)) {
            e.setCancelled(true);
            TNTPrimed tntPrimed = entity.getWorld().spawn(entity.getLocation(), TNTPrimed.class);
            tntPrimed.setFuseTicks(0);
            tntPrimed.setYield(2);

            e.blockList().removeIf(block -> !gameManager.getPlayerManager().getPlayerBlocks().contains(block)
                    || block.getLocation().add(-1.0, 0.0, 0.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(-2.0, 0.0, 0.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(0.0, 0.0, -2.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(0.0, 0.0, -1.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(2.0, 0.0, 0.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(1.0, 0.0, 0.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(0.0, 0.0, 2.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(0.0, 0.0, 1.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(0.0, 2.0, 0.0).getBlock().getType().name().contains("END_STONE")
                    || block.getLocation().add(0.0, 1.0, 0.0).getBlock().getType().name().contains("END_STONE")
                    || block.getType().name().contains("END_STONE"));
        }

        e.blockList().removeIf(block -> !gameManager.getPlayerManager().getPlayerBlocks().contains(block)
                || block.getLocation().add(-1.0, 0.0, 0.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(-2.0, 0.0, 0.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(0.0, 0.0, -2.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(0.0, 0.0, -1.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(2.0, 0.0, 0.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(1.0, 0.0, 0.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(0.0, 0.0, 2.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(0.0, 0.0, 1.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(0.0, 2.0, 0.0).getBlock().getType().name().contains("GLASS")
                || block.getLocation().add(0.0, 1.0, 0.0).getBlock().getType().name().contains("GLASS")
                || block.getType().name().contains("GLASS"));
    }

}
