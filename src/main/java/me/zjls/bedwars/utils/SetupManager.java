package me.zjls.bedwars.utils;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.teams.TeamColor;
import me.zjls.bedwars.worlds.GameWorld;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SetupManager {

    public Map<Player, TeamColor> playerTeamColorMap = new HashMap<>();
    public Map<Player, World> playerGameWorldMap = new HashMap<>();

    private GameManager gameManager;

    public SetupManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void activateSetup(Player p, World world) {
        p.getInventory().clear();
        p.setGameMode(GameMode.CREATIVE);
        p.teleport(new Location(world, 0, 64, 0));
        playerGameWorldMap.put(p, world);
    }

    public void worldSetup(Player p, GameWorld world) {
        p.getInventory().clear();
        p.getInventory().addItem(
                new ItemBuilder(Material.DIAMOND, 1).setName("设置钻石生成点").toItemStack(),
                new ItemBuilder(Material.EMERALD, 1).setName("设置绿宝石生成点").toItemStack(),
                new ItemBuilder(Material.LIME_WOOL, 1).setName("更改队伍").toItemStack()
        );
    }

    public void teamSetup(Player p, TeamColor color) {
        p.getInventory().clear();

        p.getInventory().addItem(
                new ItemBuilder(Material.STICK, 1).setName("定点棒1").toItemStack(),
                new ItemBuilder(Material.BLAZE_ROD, 1).setName("定点棒2").toItemStack(),
                new ItemBuilder(Material.VILLAGER_SPAWN_EGG, 1).setName("设置物品商店位置").toItemStack(),
                new ItemBuilder(Material.ZOMBIE_VILLAGER_SPAWN_EGG, 1).setName("设置升级商店位置").toItemStack(),
                new ItemBuilder(Material.IRON_INGOT, 1).setName("设置资源生成点").toItemStack(),
                new ItemBuilder(Material.RED_BED, 1).setName("设置床").toItemStack(),
                new ItemBuilder(Material.BOWL, 1).setName("设置队伍出生点").toItemStack(),

                new ItemBuilder(color.getTeamWool(), 1).setName("更改队伍").toItemStack()
        );


        playerTeamColorMap.put(p, color);
    }

}
