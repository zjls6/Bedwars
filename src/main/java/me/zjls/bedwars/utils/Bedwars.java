package me.zjls.bedwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Bedwars {
    public static void bc(String msg){
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
        }
    }

}
