package me.zjls.bedwars.events;

import lombok.Getter;
import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import me.zjls.bedwars.utils.Bedwars;
import me.zjls.bedwars.utils.Color;
import me.zjls.bedwars.worlds.Island;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

@Getter
public class BlockUpdate implements Listener {

    private GameManager gameManager;

    public BlockUpdate(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (!(gameManager.getState().equals(GameState.ACTIVE) || gameManager.getState().equals(GameState.WON))) {
            e.setCancelled(true);
            return;
        }

        Block b = e.getBlock();
        Material type = b.getType();

        if (b.getLocation().getBlockY() > 110) {
            e.setCancelled(true);
            return;
        }

        if (gameManager.getPlayerManager().getPlayerBlocks().contains(e.getBlock())) {
            return;
        }

        if (type.toString().contains("BED")) {
            Location location = b.getLocation();
            Island island = gameManager.getGameWorld().getIsland(location);
            if (island != null && !island.isMember(p)) {
                e.setDropItems(false);

                for (UUID uuid : island.getPlayers()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        continue;
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                    player.sendTitle(Color.str("&c&l床被破坏"), Color.str("&c你将无法重生"), 20, 40, 20);
                }

                location.getWorld().strikeLightningEffect(location);
                gameManager.getColorHologramMap().get(island.getColor()).delete();

                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);

                Bedwars.bc(Color.str(island.getColor().getChatColor() + island.getColor().getName() + "队 &r的床已被摧毁！"));

            } else if (island != null && island.isMember(p)) {
                p.sendMessage(Color.str("&c你不能破坏自己的床！"));
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
            }
            return;
        }

        for (Island island : gameManager.getGameWorld().getIslands()) {
            if (island.isProtected(b)) {
                e.setCancelled(true);
            }
        }
        p.sendMessage(Color.str("&c你只能破坏由玩家放置的方块！"));
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        for (Island island : gameManager.getGameWorld().getIslands()) {
            if (island.isProtected(b)) {
                e.setCancelled(true);
                p.sendMessage(Color.str("&c你不能在这里放置方块！"));
                return;
            }
        }
        if (gameManager.getProtectedBlock().contains(b)) {
            e.setCancelled(true);
            p.sendMessage(Color.str("&c你不能在这里放置方块！ 2"));
            return;
        }

        if (b.getLocation().getBlockY() > 110) {
            e.setCancelled(true);
            p.sendMessage(Color.str("&c你不能在这里放置方块！"));
            return;
        }

        if (b.getType() == Material.TNT) {
            b.setType(Material.AIR);
            b.getWorld().spawn(b.getLocation().add(0.5, 0.0, 0.5), TNTPrimed.class);
            return;
        }
        //防御塔
        if (b.getType().equals(Material.TRAPPED_CHEST)) {
            b.setType(Material.AIR);
            gameManager.getCompactTower().setBlocks(p.getLocation(), p);
            Bedwars.takeItem(p, e.getItemInHand());
        }

        if (gameManager.getState().equals(GameState.ACTIVE)) {
            gameManager.getPlayerManager().getPlayerBlocks().add(e.getBlock());
        }

        if (p.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (!(gameManager.getState().equals(GameState.ACTIVE) || gameManager.getState().equals(GameState.WON))) {
            e.setCancelled(true);
            return;
        }

    }

}
