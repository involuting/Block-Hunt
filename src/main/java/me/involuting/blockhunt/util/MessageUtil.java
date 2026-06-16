package me.involuting.blockhunt.util;


import org.bukkit.ChatColor;

public final class MessageUtil {

    private MessageUtil() {}

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
