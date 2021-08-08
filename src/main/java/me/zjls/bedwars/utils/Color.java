package me.zjls.bedwars.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class Color {
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    public static final char ALT_COLOR_CHAR = '&';
    public static final char RGB_CHAR = '#';

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    public static String str(String text) {

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == RGB_CHAR) {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes(Color.ALT_COLOR_CHAR, "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    public static TextComponent translateColorCodesToTextComponent(String text) {

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        ComponentBuilder builder = new ComponentBuilder();

        for (int i = 0; i < texts.length; i++) {
            TextComponent subComponent = new TextComponent();
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == RGB_CHAR) {
                    subComponent.setText(texts[i].substring(7));
                    subComponent.setColor(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)));
                } else {
                    if (texts[i].length() > 1) {
                        subComponent.setText(texts[i].substring(1));
                    } else {
                        subComponent.setText(" ");
                    }

                    switch (texts[i].charAt(0)) {
                        case '0':
                            subComponent.setColor(ChatColor.BLACK);
                            break;
                        case '1':
                            subComponent.setColor(ChatColor.DARK_BLUE);
                            break;
                        case '2':
                            subComponent.setColor(ChatColor.DARK_GREEN);
                            break;
                        case '3':
                            subComponent.setColor(ChatColor.DARK_AQUA);
                            break;
                        case '4':
                            subComponent.setColor(ChatColor.DARK_RED);
                            break;
                        case '5':
                            subComponent.setColor(ChatColor.DARK_PURPLE);
                            break;
                        case '6':
                            subComponent.setColor(ChatColor.GOLD);
                            break;
                        case '7':
                            subComponent.setColor(ChatColor.GRAY);
                            break;
                        case '8':
                            subComponent.setColor(ChatColor.DARK_GRAY);
                            break;
                        case '9':
                            subComponent.setColor(ChatColor.BLUE);
                            break;
                        case 'a':
                            subComponent.setColor(ChatColor.GREEN);
                            break;
                        case 'b':
                            subComponent.setColor(ChatColor.AQUA);
                            break;
                        case 'c':
                            subComponent.setColor(ChatColor.RED);
                            break;
                        case 'd':
                            subComponent.setColor(ChatColor.LIGHT_PURPLE);
                            break;
                        case 'e':
                            subComponent.setColor(ChatColor.YELLOW);
                            break;
                        case 'f':
                            subComponent.setColor(ChatColor.WHITE);
                            break;
                        case 'k':
                            subComponent.setObfuscated(true);
                            break;
                        case 'l':
                            subComponent.setBold(true);
                            break;
                        case 'm':
                            subComponent.setStrikethrough(true);
                            break;
                        case 'n':
                            subComponent.setUnderlined(true);
                            break;
                        case 'o':
                            subComponent.setItalic(true);
                            break;
                        case 'r':
                            subComponent.setColor(ChatColor.RESET);
                            break;
                    }

                }
                builder.append(subComponent);
            } else {
                builder.append(texts[i]);
            }
        }
        return new TextComponent(builder.create());
    }

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

//    public static String str(String msg) {
//        return ChatColor.translateAlternateColorCodes(ALT_COLOR_CHAR, msg);
//    }

    public static List<String> str(List<String> msgs) {
        return msgs.stream().map(s -> org.bukkit.ChatColor.translateAlternateColorCodes(ALT_COLOR_CHAR, s)).collect(Collectors.toList());
    }

    public static ChatColor of(java.awt.Color color) {
        return ChatColor.of(color);
    }
}
