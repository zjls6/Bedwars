package me.zjls.bedwars.commands;

import me.zjls.bedwars.games.GameManager;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetupCommand implements CommandExecutor {

    private GameManager gameManager;

    public SetupCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§2该命令不能在控制台执行！");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("bedwars.admin")) {
            p.sendMessage("§c您没有使用该命令的权限");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("/setup <地图名>");
        }
        String mapName=args[0];
        p.sendMessage("加载世界中......");
        WorldCreator creator=new WorldCreator(mapName);
        World world = creator.createWorld();

        gameManager.getSetupManager().activateSetup(p,world);

        return true;
    }
}