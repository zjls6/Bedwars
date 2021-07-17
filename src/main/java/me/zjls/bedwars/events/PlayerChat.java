package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    private GameManager gameManager;

    public PlayerChat(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        StringBuilder prefix = new StringBuilder();

        Player p = e.getPlayer();

        if (p.hasPermission("bedwars.admin")) {
            prefix.append(Color.str("&c&l管理员&r "));
        }

        if (p.getGameMode() != GameMode.SURVIVAL) {
            prefix.append(Color.str("&7&l旁观者&r "));
        }

        Island island = gameManager.getGameWorld().getIsland(p);
        if (island != null) {
            prefix.append(Color.of(island.getColor().getColor())).append(Color.str("&l" + Color.of(island.getColor().getColor()) + island.getColor().getName() + " "));
        } else {
            prefix.append(Color.str("&3"));
        }

        e.setFormat(Color.str(prefix + p.getDisplayName() + "&7> " + e.getMessage()));

    }
}
