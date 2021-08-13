package me.zjls.bedwars.tasks;

import lombok.Getter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.Island;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
public class GameTick extends BukkitRunnable {

    private GameManager gameManager;
    private int currentSecond = 0;

    public GameTick(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        gameManager.getScoreboard().updateScoreboard();

        currentSecond += 1;
        gameManager.getGameWorld().tick(currentSecond);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR
                    , Color.translateColorCodesToTextComponent("&#DF7C52死亡数：&6"
                            + gameManager.getPlayerManager().getPlayerDeathCount().getOrDefault(p.getUniqueId(), 0)));
        }

        if (currentSecond == 30 * 60) {
            for (Island island : gameManager.getGameWorld().getIslands()) {
                if (island.isBedPlaced()) {
                    Block block = island.getBedLocation().getBlock();
                    block.setType(Material.AIR);
                    gameManager.getColorHologramMap().get(island.getColor()).delete();
                }
            }
            for (UUID uuid : gameManager.getPlayerInGame()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
            }

            Bedwars.bc("");
            Bedwars.bc(Color.str("&c床自毁 &7> &f所有的床都被破坏了！"));
            Bedwars.bc("");

            gameManager.endGame();
        }

        if (currentSecond == 60 * 60) {
            gameManager.setState(GameState.WON);
        }
/*
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            System.out.println(Bukkit.getOnlinePlayers().size());
            System.out.println(onlinePlayer.getUniqueId());
        }
        for (Island island1 : gameManager.getGameWorld().getIslands()) {
            for (Player onlinePlayer1 : Bukkit.getOnlinePlayers()) {

                if (island1.getPlayers().contains(onlinePlayer1)) {

                    System.out.println(onlinePlayer1.getDisplayName() + "是" + island1.getColor().getChatColor() + island1.getColor().getName() + "  队 §r的成员");
                }


            }
            for (Player island1Player : island1.getPlayers()) {
                System.out.println(island1.getColor().getChatColor() + island1.getColor().getName() + "  " + island1Player.getDisplayName());
            }
        }
*/

    }
}
