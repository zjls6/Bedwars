package me.zjls.bedwars.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityChange implements Listener {
    @EventHandler
    public void onChange(PlayerItemDamageEvent e) {
        e.setCancelled(true);
    }
}
