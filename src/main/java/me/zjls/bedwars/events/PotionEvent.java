package me.zjls.bedwars.events;

import me.zjls.bedwars.games.GameManager;
import me.zjls.bedwars.utils.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEvent implements Listener {

    private GameManager gameManager;

    public PotionEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onClick(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (!gameManager.getPlayerInGame().contains(player.getUniqueId())) {
            return;
        }
        ItemStack item = e.getItem();

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Color.str("&fSpeed Potion"))) {
                player.setItemInHand(new ItemStack(Material.AIR));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 900, 1));
                e.setCancelled(true);
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Color.str("&fJump Potion"))) {
                player.setItemInHand(new ItemStack(Material.AIR));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 900, 4));
                e.setCancelled(true);
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Color.str("&fInvisibility Potion"))) {
                player.setItemInHand(new ItemStack(Material.AIR));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1));
                e.setCancelled(true);
            }
        }
    }
}
