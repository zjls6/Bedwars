package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.games.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.inventory.ItemStack;

public class OtherEvents implements Listener {

    private GameManager gameManager;

    public OtherEvents(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (gameManager.getPlayerManager().getSpectatorList().contains(p.getUniqueId()) || p.getGameMode() == GameMode.CREATIVE) {
            e.setCancelled(true);
            e.getItemDrop().remove();
        }
        if (gameManager.getState().equals(GameState.ACTIVE)) {
            Material type = e.getItemDrop().getItemStack().getType();
            if (type.equals(Material.WOODEN_SWORD) || type.equals(Material.COMPASS)) {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        for (HumanEntity humanEntity : e.getViewers()) {
            if (humanEntity instanceof Player) {
                Player p = (Player) humanEntity;
                if (gameManager.getPlayerInGame().contains(p.getUniqueId())) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onDiscover(PlayerRecipeDiscoverEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEgg(PlayerEggThrowEvent e) {
        Player p = e.getPlayer();
        if (gameManager.getPlayerInGame().contains(p.getUniqueId())) {
            e.setHatching(false);
        }
    }
}
