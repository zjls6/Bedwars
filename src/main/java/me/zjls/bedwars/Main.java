package me.zjls.bedwars;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import me.zjls.bedwars.commands.SetupCommand;
import me.zjls.bedwars.commands.StartCommand;
import me.zjls.bedwars.events.*;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.mysql.MySQL;
import me.zjls.bedwars.mysql.SQLGetter;
import me.zjls.bedwars.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class Main extends JavaPlugin {

    private GameManager gameManager;

    public MySQL sql;
    public SQLGetter data;

    public static WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.gameManager = new GameManager(this);

        this.sql = new MySQL();
        this.data = new SQLGetter(this);
        try {
            sql.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().info(Color.str("&c数据库连接失败"));
        }
        if (sql.isConnected()) {
            getLogger().info(Color.str("&a数据库连接成功"));
            data.createTable();
        }


        if (getServer().getPluginManager().getPlugin("HolographicDisplays") == null || !Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("未找到依赖 HolographicDisplays (全息显示)");
            getLogger().severe("插件将被禁用");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().getPlugin("Citizens") == null || !Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            getLogger().severe("未找到依赖 Citizens 2 (公民2)");
            getLogger().severe("插件将被禁用");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinOrQuit(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(gameManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(gameManager), this);
        getServer().getPluginManager().registerEvents(new BlockUpdate(gameManager), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(gameManager), this);
        getServer().getPluginManager().registerEvents(new DurabilityChange(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(gameManager), this);
        getServer().getPluginManager().registerEvents(new NPCClick(gameManager), this);
        getServer().getPluginManager().registerEvents(new OtherEvents(gameManager), this);
        getServer().getPluginManager().registerEvents(new EntityExplode(gameManager), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunch(gameManager), this);
        getServer().getPluginManager().registerEvents(new PotionEvent(gameManager), this);

        getCommand("setup").setExecutor(new SetupCommand(gameManager));
        getCommand("start").setExecutor(new StartCommand(gameManager));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.gameManager.getScoreboard().destroy();
        if (sql.isConnected()) {
            sql.disConnect();
        }
//
//        for (Island island : gameManager.getGameWorld().getIslands()) {
//            island.getItemShop().destroy();
//            System.out.println("已删除NPC " + island.getItemShop().getId());
//            island.getUpgradeShop().destroy();
//            System.out.println("已删除NPC " + island.getUpgradeShop().getId());
//        }
    }
}
