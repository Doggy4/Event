package PluginUtilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class Chat {

    public static String div8 = "--------\n";
    public static String div16 = "----------------\n";
    public static String div32 = "--------------------------------\n";

    public static String divThick8 = "========\n";
    public static String divThick16 = "================\n";
    public static String divThick32 = "================================\n";

    public static String divThicker8 = "########\n";
    public static String divThicker16 = "################\n";
    public static String divThicker32 = "################################\n";

    public static String div = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n";


    public static HashMap<String, ChatColor> colors = new HashMap<String, ChatColor>();

    static {
        colors.put("BLACK", ChatColor.BLACK);
        colors.put("BLUE", ChatColor.BLUE);
        colors.put("ORANGE", ChatColor.GOLD);
        colors.put("BROWN", ChatColor.BLACK);
        colors.put("AQUA", ChatColor.AQUA);
        colors.put("GRAY", ChatColor.DARK_GRAY);
        colors.put("LIGHT_GRAY", ChatColor.GRAY);
        colors.put("GREEN", ChatColor.DARK_GREEN);
        colors.put("LIME", ChatColor.GREEN);
        colors.put("RED", ChatColor.RED);
        colors.put("WHITE", ChatColor.WHITE);
        colors.put("YELLOW", ChatColor.YELLOW);
        colors.put("PURPLE", ChatColor.DARK_PURPLE);
        colors.put("PINK", ChatColor.LIGHT_PURPLE);
        colors.put("CYAN", ChatColor.DARK_AQUA);
        colors.put("MAGENTA", ChatColor.LIGHT_PURPLE);
        colors.put("LIGHT_BLUE", ChatColor.AQUA);
    }

    public static void broadcastToEveryone(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + message);
        }
    }
}