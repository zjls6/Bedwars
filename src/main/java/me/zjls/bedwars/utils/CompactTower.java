package me.zjls.bedwars.utils;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CompactTower {

    private GameManager gameManager;
    private List<List<String>> blocks;

    public CompactTower(GameManager gameManager) {
        this.gameManager = gameManager;
        this.loadBlocks();
    }

    public static BlockFace getCardinalDirection(Location location) {
        double rotation = (location.getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return BlockFace.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH_EAST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return BlockFace.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return BlockFace.SOUTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return BlockFace.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH_WEST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return BlockFace.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return BlockFace.NORTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return BlockFace.NORTH;
        } else {
            return null;
        }
    }

//    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
//    public void onBlockPlace(BlockPlaceEvent e) {
//        Player player = e.getPlayer();
//        if (!Config.items_compact_tower_enabled) {
//            return;
//        }
//        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(player);
//        e.getItemInHand();
//        if (game == null) {
//            return;
//        }
//        if (game.isOverSet() || game.getState() != GameState.RUNNING || game.isSpectator(player)) {
//            return;
//        }
//        Team team = game.getPlayerTeam(player);
//        if (team == null) {
//            return;
//        }
//        if (e.getItemInHand().getType() != new ItemStack(Material.valueOf(Config.items_compact_tower_item)).getType()) {
//            return;
//        }
//        e.setCancelled(true);
//        if (System.currentTimeMillis() - this.cooldown.getOrDefault(player, 0L) <= Config.items_compact_tower_cooldown * 1000.0) {
//            player.sendMessage(Config.message_cooling.replace("{time}", new StringBuilder(String.valueOf(String.format("%.1f", (Config.items_compact_tower_cooldown * 1000.0 - System.currentTimeMillis() + this.cooldown.getOrDefault(player, 0L)) / 1000.0))).toString()));
//            return;
//        }
//        ItemStack stack = e.getItemInHand();
//        BedwarsUseItemEvent bedwarsUseItemEvent = new BedwarsUseItemEvent(game, player, EnumItem.BRIDGE_EGG, stack);
//        Bukkit.getPluginManager().callEvent(bedwarsUseItemEvent);
//        if (!bedwarsUseItemEvent.isCancelled()) {
//            this.cooldown.put(player, System.currentTimeMillis());
//            this.setblock(game, team, e.getBlock().getLoc ation(), player);
//            TakeItemUtil.TakeItem(player, stack);
//        }
//    }

    private void loadBlocks() {
        this.blocks = new ArrayList<>();
        try {
            URL url = gameManager.getPlugin().getClass().getResource("/Tower.block");
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                List<String> list = new ArrayList<>();
                String[] split;
                for (int length = (split = line.split(";")).length, i = 0; i < length; ++i) {
                    String l = split[i];
                    try {
                        list.add(l);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.blocks.add(list);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void setBlocks(Location location, Player player) {
        int face = this.getFace(player.getLocation());
        BlockFace cardinalDirection = getCardinalDirection(player.getLocation());
        Island island = gameManager.getGameWorld().getIsland(player);
        if (island == null) {
            return;
        }
        new BukkitRunnable() {
            int i = 0;

            public void run() {
                if (!gameManager.getState().equals(GameState.ACTIVE) || this.i >= CompactTower.this.blocks.size()) {
                    this.cancel();
                    return;
                }
                Location loc = null;
                for (String line : CompactTower.this.blocks.get(this.i)) {
                    String[] ary = line.split(",");
                    try {
                        int x = Integer.parseInt(ary[0]);
                        int y = Integer.parseInt(ary[1]);
                        int z = Integer.parseInt(ary[2]);
                        if (face == 0) {
                            loc = location.clone().add(x, y, z);
                        } else if (face == 1) {
                            loc = location.clone().add(-z, y, x);
                        } else if (face == 2) {
                            loc = location.clone().add(-x, y, -z);
                        } else if (face == 3) {
                            loc = location.clone().add(z, y, -x);
                        }
                        Block block = loc.getBlock();
                        if (!isCanPlace(island, loc)) {
                            continue;
                        }
                        try {
                            Material type = Material.valueOf(ary[3]);
                            if (type.name().contains("WOOL")) {
                                block.setType(island.getColor().getTeamWool());
                            } else if (type == Material.LADDER) {
                                block.setType(Material.LADDER);
                                Ladder ladder = (Ladder) block.getBlockData();

                                ladder.setFacing(cardinalDirection);

//                                if (face == 0) {
//                                    ladder.setFacing(BlockFace.SOUTH);
//                                    gameManager.getPlayerManager().getPlayerBlocks().add(block);
//                                } else if (face == 1) {
//                                    ladder.setFacing(BlockFace.WEST);
//                                    gameManager.getPlayerManager().getPlayerBlocks().add(block);
//                                } else if (face == 2) {
//                                    ladder.setFacing(BlockFace.NORTH);
//                                    gameManager.getPlayerManager().getPlayerBlocks().add(block);
//                                } else if (face == 3) {
//                                    ladder.setFacing(BlockFace.EAST);
//                                    gameManager.getPlayerManager().getPlayerBlocks().add(block);
//                                }
                                gameManager.getPlayerManager().getPlayerBlocks().add(block);
                            } else {
                                block.setType(type);
                                try {
                                    block.setBlockData(Bukkit.createBlockData(ary[4]));
                                } catch (Exception ignored) {
                                }
                            }
                            gameManager.getPlayerManager().getPlayerBlocks().add(block);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (loc != null) {
                    loc.getWorld().playSound(loc, Sound.ENTITY_CHICKEN_EGG, 5.0f, 1.0f);
                }
                ++this.i;
            }
        }.runTaskTimer(gameManager.getPlugin(), 0L, 3L);
    }

    private boolean isCanPlace(Island island, Location location) {
        Block block = location.getBlock();
        if (!block.getType().equals(Material.AIR)) {
            return false;
        }
        if (island.isProtected(block)) {
            return false;
        }
        if (gameManager.getProtectedBlock().contains(block)) {
            return false;
        }
        for (Entity entity : location.getWorld().getNearbyEntities(location.clone().add(0.5, 1.0, 0.5), 0.5, 1.0, 0.5)) {
            if (!(entity instanceof Player)) {
                continue;
            }
            Player player = (Player) entity;
            if (gameManager.getPlayerManager().getSpectatorList().contains(player.getUniqueId())) {
                continue;
            }
            if (!gameManager.getPlayerInGame().contains(player.getUniqueId())) {
                continue;
            }
            return false;
        }
        return true;
    }

    private int getFace(Location location) {
        List<Integer> list = new ArrayList<>();
        for (int i = -360; i <= 360; i += 90) {
            list.add(i);
        }
        int yaw = (int) location.getYaw();
        int a = Math.abs(list.get(0) - yaw);
        int nyaw = list.get(0);
        for (int j : list) {
            int k = Math.abs(j - yaw);
            if (k < a) {
                a = k;
                nyaw = j;
            }
        }
        int face = 0;
        if (nyaw == -360 || nyaw == 0 || nyaw == 360) {
            face = 0;
        } else if (nyaw == 90 || nyaw == -270) {
            face = 1;
        } else if (nyaw == 180 || nyaw == -180) {
            face = 2;
        } else if (nyaw == 270 || nyaw == -90) {
            face = 3;
        }
        return face;
    }

}


