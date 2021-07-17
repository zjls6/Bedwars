package me.zjls.bedwars.commands;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {

    private GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("bedwars.admin")) {
            if (!gameManager.getState().equals(GameState.LOBBY)) {
                return true;
            }
            gameManager.setState(GameState.STARTING);
        } else {
            sender.sendMessage("§c您没有使用该命令的权限");
        }
        return true;
    }
}
