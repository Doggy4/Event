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

    public static String div = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n";


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

    public static HashMap<String, ChatColor> colorsFromID = new HashMap<String, ChatColor>();
    public static HashMap<Integer, String> roundNames = new HashMap<Integer, String>();

    static {
        colorsFromID.put("WHITE_WOOL", ChatColor.WHITE);
        colorsFromID.put("ORANGE_WOOL", ChatColor.GOLD);
        colorsFromID.put("MAGENTA_WOOL", ChatColor.LIGHT_PURPLE);
        colorsFromID.put("LIGHT_BLUE_WOOL", ChatColor.AQUA);
        colorsFromID.put("YELLOW_WOOL", ChatColor.YELLOW);
        colorsFromID.put("LIME_WOOL", ChatColor.GREEN);
        colorsFromID.put("PINK_WOOL", ChatColor.LIGHT_PURPLE);
        colorsFromID.put("GRAY_WOOL", ChatColor.DARK_GRAY);
        colorsFromID.put("LIGHT_GRAY_WOOL", ChatColor.GRAY);
        colorsFromID.put("CYAN_WOOL", ChatColor.DARK_AQUA);
        colorsFromID.put("PURPLE_WOOL", ChatColor.DARK_PURPLE);
        colorsFromID.put("BLUE_WOOL", ChatColor.BLUE);
        colorsFromID.put("BROWN_WOOL", ChatColor.BLACK);
        colorsFromID.put("GREEN_WOOL", ChatColor.DARK_GREEN);
        colorsFromID.put("RED_WOOL", ChatColor.RED);
        colorsFromID.put("BLACK_WOOL", ChatColor.BLACK);
    }

    static {
        roundNames.put(0, "Строитель");
        roundNames.put(1, "Чисто и красиво");
        roundNames.put(2, "Меткий стрелок");
        roundNames.put(3, "День стрижки");
        roundNames.put(4, "Горячее яичко");
        roundNames.put(5, "Милка");
        roundNames.put(6, "Головоломка");
        roundNames.put(7, "Космонавт");
        roundNames.put(8, "Железное небо");
        roundNames.put(9, "Калорийно, но вкусно");
        roundNames.put(10, "Математическая проверка");
        roundNames.put(11, "Сумо");
        roundNames.put(12, "Гарри Поттер");
        roundNames.put(13, "Слизневые сражения");
        roundNames.put(14, "Пол - это лава");
        roundNames.put(15, "Снегопад");
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