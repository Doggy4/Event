package PluginUtilities;

import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

public class Chat {

    public static String div8 = "--------\n";
    public static String div16 = "----------------\n";
    public static String div32 = "--------------------------------\n";

    public static String divThick8 = "========\n";
    public static String divThick16 = "================\n";
    public static String divThick32 = "================================\n";

    public static String ScoreBoardDivider2 = ".▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
    public static String ScoreBoardDivider1 = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬.";
    public static String ScoreBoardDivider3 = ".▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬.";

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

    public static HashMap<Integer, ChatColor> colorsFromID = new HashMap<Integer, ChatColor>();
    public static HashMap<Integer, ChatColor> roundNames = new HashMap<Integer, ChatColor>();

    static {
        colorsFromID.put(0, ChatColor.getByChar('0'));
        colorsFromID.put(1, ChatColor.getByChar('1'));
        colorsFromID.put(2, ChatColor.getByChar('2'));
        colorsFromID.put(3, ChatColor.getByChar('4'));
        colorsFromID.put(4, ChatColor.getByChar('4'));
        colorsFromID.put(5, ChatColor.getByChar('5'));
        colorsFromID.put(6, ChatColor.getByChar('6'));
        colorsFromID.put(7, ChatColor.getByChar('7'));
        colorsFromID.put(8, ChatColor.getByChar('8'));
        colorsFromID.put(9, ChatColor.getByChar('9'));
        colorsFromID.put(10, ChatColor.getByChar('a'));
        colorsFromID.put(11, ChatColor.getByChar('b'));
        colorsFromID.put(12, ChatColor.getByChar('c'));
        colorsFromID.put(13, ChatColor.getByChar('e'));
        colorsFromID.put(14, ChatColor.getByChar('e'));
        colorsFromID.put(15, ChatColor.getByChar('f'));
    }

    static {
        roundNames.put(1, ChatColor.GOLD);
        roundNames.put(2, ChatColor.LIGHT_PURPLE);
        roundNames.put(3, ChatColor.AQUA);
        roundNames.put(4, ChatColor.YELLOW);
        roundNames.put(5, ChatColor.GREEN);
        roundNames.put(6, ChatColor.LIGHT_PURPLE);
        roundNames.put(7, ChatColor.GRAY);
        roundNames.put(8, ChatColor.GRAY);
        roundNames.put(9, ChatColor.DARK_AQUA);
        roundNames.put(10, ChatColor.BLUE);
        roundNames.put(11, ChatColor.DARK_PURPLE);
        roundNames.put(12, ChatColor.BLACK);
        roundNames.put(13, ChatColor.DARK_GREEN);
        roundNames.put(14, ChatColor.RED);
        roundNames.put(15, ChatColor.BLACK);
        roundNames.put(0, ChatColor.WHITE);
    }

    public static void broadcastToEveryone(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + message);
        }
    }

    public static String translate(String string) {
        File file = new File(Main.main.getDataFolder().getAbsolutePath() + "/ru_ru.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        string = string.toLowerCase();

        String translatedString = null;

        translatedString = config.getString("item.minecraft." + string);
        if (translatedString == null)
            translatedString = config.getString("color.minecraft." + string);
        if (translatedString == null)
            translatedString = config.getString("block.minecraft." + string);
        if (translatedString == null)
            return string;
        else
            return translatedString;

    }
}