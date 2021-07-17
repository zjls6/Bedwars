package me.zjls.bedwars.commands;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.worlds.GameWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    private GameManager gameManager;

    public SetupCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4该命令不能在控制台执行！");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("bedwars.admin")) {
            p.sendMessage("§c您没有使用该命令的权限");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("/setup <地图名>");
            return true;
        }

        if (args[0].equalsIgnoreCase("exit")) {
            gameManager.getSetupManager().exitSetup(p);
            return true;
        }

        String mapName = args[0];

        GameWorld world = new GameWorld(mapName);

        p.sendMessage("加载世界中......");

        world.loadWorld(gameManager, true, () -> gameManager.getSetupManager().activateSetup(p, world));

        return true;
    }
}
