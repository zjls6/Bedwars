package me.zjls.bedwars.worlds;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum IslandColor {

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
                return "红";
            case BLUE:
                return "蓝";
            case GREEN:
                return "绿";
            case YELLOW:
                return "黄";
            case AQUA:
                return "青";
            case WHITE:
                return "白";
            case PINK:
                return "粉";
            case GRAY:
                return "灰";
        }
        return "";
    }

    public Color getColor() {
        switch (this) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case YELLOW:
                return Color.YELLOW;
            case AQUA:
                return Color.AQUA;
            case WHITE:
                return Color.WHITE;
            case PINK:
                return Color.FUCHSIA;
            case GRAY:
                return Color.GRAY;
        }

        return Color.BLACK;
    }

    public ChatColor getChatColor(){
        switch (this){
            case RED:
                return ChatColor.RED;
            case BLUE:
                return ChatColor.BLUE;
            case GREEN:
                return ChatColor.GREEN;
            case YELLOW:
                return ChatColor.YELLOW;
            case AQUA:
                return ChatColor.AQUA;
            case WHITE:
                return ChatColor.WHITE;
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            case GRAY:
                return ChatColor.GRAY;
        }
        return ChatColor.BLACK;
    }

    public Material getTeamWool() {
        Material teamWool = Material.BLACK_WOOL;
        switch (this) {
            case RED:
                teamWool = Material.RED_WOOL;
                break;
            case BLUE:
                teamWool = Material.BLUE_WOOL;
                break;
            case GREEN:
                teamWool = Material.GREEN_WOOL;
                break;
            case YELLOW:
                teamWool = Material.YELLOW_WOOL;
                break;
            case AQUA:
                teamWool = Material.CYAN_WOOL;
                break;
            case WHITE:
                teamWool = Material.WHITE_WOOL;
                break;
            case PINK:
                teamWool = Material.PINK_WOOL;
                break;
            case GRAY:
                teamWool = Material.GRAY_WOOL;
                break;
        }

        return teamWool;
    }

    public Material getTeamTerracotta() {
        Material teamTerracotta = Material.BLACK_TERRACOTTA;
        switch (this) {
            case RED:
                teamTerracotta = Material.RED_TERRACOTTA;
                break;
            case BLUE:
                teamTerracotta = Material.BLUE_TERRACOTTA;
                break;
            case GREEN:
                teamTerracotta = Material.GREEN_TERRACOTTA;
                break;
            case YELLOW:
                teamTerracotta = Material.YELLOW_TERRACOTTA;
                break;
            case AQUA:
                teamTerracotta = Material.CYAN_TERRACOTTA;
                break;
            case WHITE:
                teamTerracotta = Material.WHITE_TERRACOTTA;
                break;
            case PINK:
                teamTerracotta = Material.PINK_TERRACOTTA;
                break;
            case GRAY:
                teamTerracotta = Material.GRAY_TERRACOTTA;
                break;
        }
        return teamTerracotta;
    }

    public Material getTeamConcrete() {
        Material teamConcrete = Material.BLACK_CONCRETE;
        switch (this) {
            case RED:
                teamConcrete = Material.RED_CONCRETE;
                break;
            case BLUE:
                teamConcrete = Material.BLUE_CONCRETE;
                break;
            case GREEN:
                teamConcrete = Material.GREEN_CONCRETE;
                break;
            case YELLOW:
                teamConcrete = Material.YELLOW_CONCRETE;
                break;
            case AQUA:
                teamConcrete = Material.CYAN_CONCRETE;
                break;
            case WHITE:
                teamConcrete = Material.WHITE_CONCRETE;
                break;
            case PINK:
                teamConcrete = Material.PINK_CONCRETE;
                break;
            case GRAY:
                teamConcrete = Material.GRAY_CONCRETE;
                break;
        }
        return teamConcrete;
    }

    public Material getTeamGlass() {
        Material teamGlass = Material.BLACK_STAINED_GLASS;
        switch (this) {
            case RED:
                teamGlass = Material.RED_STAINED_GLASS;
                break;
            case BLUE:
                teamGlass = Material.BLUE_STAINED_GLASS;
                break;
            case GREEN:
                teamGlass = Material.GREEN_STAINED_GLASS;
                break;
            case YELLOW:
                teamGlass = Material.YELLOW_STAINED_GLASS;
                break;
            case AQUA:
                teamGlass = Material.CYAN_STAINED_GLASS;
                break;
            case WHITE:
                teamGlass = Material.WHITE_STAINED_GLASS;
                break;
            case PINK:
                teamGlass = Material.PINK_STAINED_GLASS;
                break;
            case GRAY:
                teamGlass = Material.GRAY_STAINED_GLASS;
                break;
        }
        return teamGlass;
    }


}
