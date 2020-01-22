package PluginUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class LocationUtulities {
    public static String getPlayerLocation(Location location) {

        String result = ChatColor.WHITE + "Мир: " + ChatColor.GREEN + location.getWorld().getName() + ChatColor.WHITE + "\nКоординаты: " + ChatColor.GREEN + "[" + Math.ceil(location.getX()) + "], [" + Math.ceil(location.getY()) + "], [" + Math.ceil(location.getZ()) + "]";
        return result;

    }
}
