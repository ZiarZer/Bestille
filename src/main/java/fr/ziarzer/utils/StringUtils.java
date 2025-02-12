package fr.ziarzer.utils;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {
    public static String colorText(String text, ChatColor color, boolean isBold) {
        String colors = ChatColor.RESET + "" + color + "";
        if (isBold) {
            colors += ChatColor.BOLD + "";
        }
        return colors + text + ChatColor.RESET;
    }
}
