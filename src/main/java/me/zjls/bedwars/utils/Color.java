package me.zjls.bedwars.utils;

import net.md_5.bungee.api.ChatColor;

public class Color {
    public static ChatColor of(org.bukkit.Color color) {
        return ChatColor.of(new java.awt.Color(color.asRGB()));
    }

    public static ChatColor of(org.bukkit.Color color, float alpha) {
        return ChatColor.of(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    public static ChatColor of(java.awt.Color color, float alpha) {
        return ChatColor.of(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    public static ChatColor of(float r, float g, float b) {
        return ChatColor.of(new java.awt.Color(r, g, b));
    }

    public static ChatColor of(float r, float g, float b, float a) {
        return ChatColor.of(new java.awt.Color(r, g, b, a));
    }

    public static ChatColor of(int rgb) {
        return ChatColor.of(new java.awt.Color(rgb));
    }

    public static ChatColor of(int rgba, boolean alpha) {
        return ChatColor.of(new java.awt.Color(rgba, alpha));
    }

    public static ChatColor of(String hex) {
        return ChatColor.of(hex);
    }

    public static String str(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static ChatColor of(java.awt.Color color) {
        return ChatColor.of(color);
    }
}
