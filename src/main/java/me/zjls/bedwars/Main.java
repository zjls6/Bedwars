package me.zjls.bedwars;

import me.zjls.bedwars.commands.SetupCommand;
import me.zjls.bedwars.commands.StartCommand;
import me.zjls.bedwars.events.*;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.CitizensTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.gameManager = new GameManager(this);

//        for (Player player : Bukkit.getOnlinePlayers()) {
//            gameManager.getScoreboard().addPlayer(player);
//        }

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
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CitizensTrait.class).withName("shopTrait"));

        getServer().getPluginManager().registerEvents(new PlayerJoinOrQuit(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(gameManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(gameManager), this);
        getServer().getPluginManager().registerEvents(new BlockUpdate(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(gameManager), this);
        getServer().getPluginManager().registerEvents(new DurabilityChange(), this);
        getServer().getPluginManager().registerEvents(new PlayerHunger(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(gameManager), this);

        getCommand("setup").setExecutor(new SetupCommand(gameManager));
        getCommand("start").setExecutor(new StartCommand(gameManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.gameManager.getScoreboard().destroy();
    }
}
