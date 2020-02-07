package PluginUtilities;

import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LocationUtulities {

    public static ArrayList<Location> spawnLocations = new ArrayList<Location>();
    private static FileConfiguration config = Main.main.getConfig();
    private static World world = Bukkit.getWorld(config.getString("spawn.world"));
    private static Location arenaCentre = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));

    static {
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() + 10, arenaCentre.getY(), arenaCentre.getZ()));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() - 10, arenaCentre.getY(), arenaCentre.getZ()));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() + 3, arenaCentre.getY(), arenaCentre.getZ() - 10));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() - 3, arenaCentre.getY(), arenaCentre.getZ() - 10));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() + 3, arenaCentre.getY(), arenaCentre.getZ() + 10));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() - 3, arenaCentre.getY(), arenaCentre.getZ() + 10));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() + 9, arenaCentre.getY(), arenaCentre.getZ() - 6));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() - 9, arenaCentre.getY(), arenaCentre.getZ() - 6));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() + 9, arenaCentre.getY(), arenaCentre.getZ() + 6));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() - 9, arenaCentre.getY(), arenaCentre.getZ() + 6));
    }

    public static String getPlayerLocation(Location location) {

        String result = ChatColor.WHITE + "Мир: " + ChatColor.GREEN + location.getWorld().getName() + ChatColor.WHITE + "\nКоординаты: " + ChatColor.GREEN + "[" + Math.ceil(location.getX()) + "], [" + Math.ceil(location.getY()) + "], [" + Math.ceil(location.getZ()) + "]";
        return result;
    }

    public static void teleportToLobby(Player player) {
        FileConfiguration config = Main.main.getConfig();

        World world = Bukkit.getWorld(config.getString("lobby.world"));
        Location location = new Location(world, config.getDouble("lobby.x"), config.getDouble("lobby.y"), config.getDouble("lobby.z"));

        location.setPitch((float) config.getDouble("lobby.pitch"));
        location.setYaw((float) config.getDouble("lobby.yaw"));

        player.teleport(location);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы были телепортированы в Лобби!");
    }

    public static void teleportToSpawn(Player player) {
        FileConfiguration config = Main.main.getConfig();

        World world = Bukkit.getWorld(config.getString("spawn.world"));

        Double x = config.getDouble("spawn.x") + (Utilities.getRandom(0, 20) - 10);
        Double z = config.getDouble("spawn.z") + (Utilities.getRandom(0, 20) - 10);

        Location location = new Location(world, x, config.getDouble("spawn.y"), z);

        location.setPitch((float) config.getDouble("spawn.pitch"));
        location.setYaw((float) config.getDouble("spawn.yaw"));

        player.teleport(location);
    }
}
