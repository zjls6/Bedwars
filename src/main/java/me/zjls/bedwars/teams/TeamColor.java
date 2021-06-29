package me.zjls.bedwars.teams;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public enum TeamColor {

    RED,
    BLUE,
    GREEN,
    YELLOW,
    AQUA,
    WHITE,
    PINK,
    GRAY;

    public String getName() {
        switch (this) {
            case RED:
                return "§c红";
            case BLUE:
                return "§1蓝";
            case GREEN:
                return "§a绿";
            case YELLOW:
                return "§e黄";
            case AQUA:
                return "§b青";
            case WHITE:
                return "§f白";
            case PINK:
                return "§d粉";
            case GRAY:
                return "§8灰";
        }
        return "";
    }

    public Material getTeamWool() {
        Material teamWool = Material.BLACK_WOOL;
        switch (this){
            case RED:
                teamWool=Material.RED_WOOL;
                break;
            case BLUE:
                teamWool=Material.BLUE_WOOL;
                break;
            case GREEN:
                teamWool=Material.GREEN_WOOL;
                break;
            case YELLOW:
                teamWool=Material.YELLOW_WOOL;
                break;
            case AQUA:
                teamWool=Material.CYAN_WOOL;
                break;
            case WHITE:
                teamWool=Material.WHITE_WOOL;
                break;
            case PINK:
                teamWool=Material.PINK_WOOL;
                break;
            case GRAY:
                teamWool=Material.GRAY_WOOL;
                break;
        }

        return teamWool;
    }

}
