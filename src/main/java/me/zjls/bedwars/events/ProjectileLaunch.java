package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ProjectileLaunch implements Listener {

    private GameManager gameManager;

    public ProjectileLaunch(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void eggBridge(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Egg) {
            Egg egg = (Egg) e.getEntity();
            if (egg.getShooter() instanceof Player) {
                egg.setBounce(false);
                Player player = (Player) egg.getShooter();
                if (gameManager.getPlayerInGame().contains(player.getUniqueId())) {
                    new BukkitRunnable() {
                        int i = 0;

                        public void run() {
                            if (!egg.isDead()) {
                                new BukkitRunnable() {
                                    Location location = egg.getLocation().add(0.0, -1.0, 0.0);

                                    public void run() {
                                        if (gameManager.getState() != GameState.ACTIVE) {
                                            this.cancel();
                                            return;
                                        }
                                        this.location.setX((int) this.location.getX());
                                        this.location.setY((int) this.location.getY());
                                        this.location.setZ((int) this.location.getZ());
                                        List<Location> blockLocation = new ArrayList<>();
                                        blockLocation.add(this.location);
                                        Vector vector = egg.getVelocity();
                                        double x = (vector.getX() > 0.0) ? vector.getX() : (-vector.getX());
                                        double y = (vector.getY() > 0.0) ? vector.getY() : (-vector.getY());
                                        double z = (vector.getZ() > 0.0) ? vector.getZ() : (-vector.getZ());
                                        if (y < x || y < z) {
                                            blockLocation.add(Bedwars.getLocation(this.location, -1, 0, -1));
                                            blockLocation.add(Bedwars.getLocation(this.location, -1, 0, 0));
                                            blockLocation.add(Bedwars.getLocation(this.location, 0, 0, -1));
                                        } else {
                                            blockLocation.add(Bedwars.getLocation(this.location, 0, 1, 0));
                                            blockLocation.add(Bedwars.getLocation(this.location, -1, 1, -1));
                                            blockLocation.add(Bedwars.getLocation(this.location, -1, 1, 0));
                                            blockLocation.add(Bedwars.getLocation(this.location, 0, 1, -1));
                                            blockLocation.add(Bedwars.getLocation(this.location, -1, 0, -1));
                                            blockLocation.add(Bedwars.getLocation(this.location, -1, 0, 0));
                                            blockLocation.add(Bedwars.getLocation(this.location, 0, 0, -1));
                                        }
                                        for (Island island : gameManager.getGameWorld().getIslands()) {
                                            blockLocation.removeIf(location1 -> island.isProtected(location1.getBlock()));
                                        }
                                        blockLocation.removeIf(location1 -> gameManager.getProtectedBlock().contains(location1.getBlock()));
                                        for (Location loc : blockLocation) {
                                            Block block = loc.getBlock();
                                            boolean isProtected = false;
                                            for (Island island : gameManager.getGameWorld().getIslands()) {
                                                isProtected = island.isProtected(block);
                                            }
                                            if (block.getType() == new ItemStack(Material.AIR).getType() && !block.equals(player.getLocation().getBlock())
                                                    && !block.equals(player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock())
                                                    && !gameManager.getProtectedBlock().contains(block) && !isProtected && i < 80) {
                                                loc.getBlock().setType(gameManager.getGameWorld().getIsland(player).getColor().getTeamWool());
                                                player.playSound(loc, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                                i++;
                                                gameManager.getPlayerManager().getPlayerBlocks().add(loc.getBlock());
                                            }
                                        }
                                    }
                                }.runTaskLater(gameManager.getPlugin(), 5L);
                                return;
                            }
                            this.cancel();
                        }
                    }.runTaskTimer(gameManager.getPlugin(), 0L, 0L);
                }
            }
        }
    }


    @EventHandler
    public void onSnowball(ProjectileHitEvent e) {
        Projectile entity = e.getEntity();
        if (entity instanceof Snowball) {
            Snowball snowball = (Snowball) entity;
            Location location = snowball.getLocation();
            if (snowball.getShooter() instanceof Player) {
                Player player = (Player) snowball.getShooter();
                if (gameManager.getPlayerInGame().contains(player.getUniqueId())) {
                    Island island = gameManager.getGameWorld().getIsland(player);
                    if (island != null) {
                        location.add(0.0, 1.0, 0.0);
                        Silverfish silverfish = location.getWorld().spawn(location, Silverfish.class);
                        silverfish.setMetadata("Time", new FixedMetadataValue(gameManager.getPlugin(), 20));
                        silverfish.setCustomName(Color.str(island.getColor().getChatColor() + island.getColor().getName() + "队的 蠹虫&r"));
                        silverfish.setCustomNameVisible(true);
                        gameManager.getSilverfishTeamMap().put(silverfish, island);
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void noTarget(EntityTargetEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() == EntityType.IRON_GOLEM || entity.getType() == EntityType.SILVERFISH) {
            Entity target = e.getTarget();
            if (entity instanceof Silverfish && target instanceof Player) {
                Silverfish silverfish = (Silverfish) entity;
                Player p = (Player) target;
                if (gameManager.getPlayerInGame().contains(target.getUniqueId())) {
                    if (gameManager.getGameWorld().getIsland(p).equals(gameManager.getSilverfishTeamMap().get(silverfish))) {
                        e.setCancelled(true);
                    }
                }
            }
        }

//            if (entity.hasMetadata("Time") && e.getReason() != EntityTargetEvent.TargetReason.CUSTOM) {
//                e.setCancelled(true);
//            }
    }
}

