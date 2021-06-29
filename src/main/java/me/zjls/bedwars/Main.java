package me.zjls.bedwars;

import me.zjls.bedwars.commands.SetupCommand;
import me.zjls.bedwars.events.InventoryClick;
import me.zjls.bedwars.events.PlayerInteract;
import me.zjls.bedwars.events.PlayerJoinOrQuit;
import me.zjls.bedwars.games.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.gameManager = new GameManager(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            gameManager.getScoreboard().addPlayer(player);
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinOrQuit(gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(gameManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(gameManager), this);

        getCommand("setup").setExecutor(new SetupCommand(gameManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        this.gameManager.getScoreboard().destroy();
    }
}
