package PluginUtilities;

import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class TeleportManager {


    public static ArrayList<Location> spawnLocations = new ArrayList<Location>();
    private static FileConfiguration config = Main.main.getConfig();
    private static World world = Bukkit.getWorld(config.getString("spawn.world"));
    private static Location arenaCentre = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));

    static {
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() + 10, arenaCentre.getY(), arenaCentre.getZ()));
        spawnLocations.add(new Location(arenaCentre.getWorld(), arenaCentre.getX() - 10, arenaCentre.getY(), arenaCentre.getZ()));
        spawnLocations.add(arenaCentre.add(3, 0, -10));
        spawnLocations.add(arenaCentre.add(-3, 0, -10));
        spawnLocations.add(arenaCentre.add(3, 0, 10));
        spawnLocations.add(arenaCentre.add(-3, 0, 10));
        spawnLocations.add(arenaCentre.add(9, 0, -6));
        spawnLocations.add(arenaCentre.add(-9, 0, -6));
        spawnLocations.add(arenaCentre.add(9, 0, 6));
        spawnLocations.add(arenaCentre.add(-9, 0, 6));
    }

}
