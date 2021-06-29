package me.zjls.bedwars.utils;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.teams.TeamColor;
import me.zjls.bedwars.worlds.GameWorld;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SetupManager {

    public Map<Player, Island> playerIslandMap = new HashMap<>();
    public Map<Player, GameWorld> playerGameWorldMap = new HashMap<>();

    private GameManager gameManager;

    public SetupManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean isSetuping(Player p) {
        return playerGameWorldMap.containsKey(p);
    }

    public void activateSetup(Player p, GameWorld world) {
        p.getInventory().clear();
        p.setGameMode(GameMode.CREATIVE);
        p.teleport(new Location(world.getWorld(), 0, 64, 0));

        worldSetup(p, world);
    }

    public void worldSetup(Player p, GameWorld world) {
        p.getInventory().clear();
        p.getInventory().addItem(
                new ItemBuilder(Material.DIAMOND, 1).setName("&b设置钻石生成点").toItemStack(),
                new ItemBuilder(Material.EMERALD, 1).setName("&a设置绿宝石生成点").toItemStack(),
                new ItemBuilder(Material.STICK, 1).setName("&r更改队伍").toItemStack()
        );

        playerGameWorldMap.put(p, world);
    }

    public void teamSetup(Player p, TeamColor color) {
        p.getInventory().clear();

        p.getInventory().addItem(
                new ItemBuilder(Material.STICK, 1).setName("&r定点棒1").toItemStack(),
                new ItemBuilder(Material.BLAZE_ROD, 1).setName("&r定点棒2").toItemStack(),
                new ItemBuilder(Material.VILLAGER_SPAWN_EGG, 1).setName("设置物品商店位置").toItemStack(),
                new ItemBuilder(Material.ZOMBIE_VILLAGER_SPAWN_EGG, 1).setName("设置升级商店位置").toItemStack(),
                new ItemBuilder(Material.IRON_INGOT, 1).setName("设置资源生成点").toItemStack(),
                new ItemBuilder(Material.RED_BED, 1).setName("设置床").toItemStack(),
                new ItemBuilder(Material.BOWL, 1).setName("设置队伍出生点").toItemStack(),
                new ItemBuilder(color.getTeamWool(), 1).setName("更改队伍").toItemStack()
        );

        Island island = new Island(getWorld(p), color);
        playerIslandMap.put(p,island);

    }

    public Island getIsland(Player p) {
        return playerIslandMap.get(p);
    }

    public GameWorld getWorld(Player p) {
        return playerGameWorldMap.get(p);
    }
}
