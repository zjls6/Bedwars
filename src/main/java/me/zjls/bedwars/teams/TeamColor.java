package me.zjls.bedwars.teams;

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
