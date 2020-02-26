package PluginUtils;

import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static ArrayList<Location> spawnLocations = new ArrayList<Location>();
    private static FileConfiguration config = Main.main.getConfig();
    public static World world = Bukkit.getWorld(config.getString("spawn.world"));
    private static Location arenaCentre = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));

    public static List<Location> availableSpawnLocations = new ArrayList<Location>();

    static {
        for (int x = (int) Math.round(arenaCentre.getX()) - 16; x <= (int) Math.round(arenaCentre.getX()) + 16; x++)
            for (int z = (int) Math.round(arenaCentre.getZ()) - 16; z <= (int) Math.round(arenaCentre.getZ()) + 16; z++)
                availableSpawnLocations.add(new Location(world, x, arenaCentre.getY(), z));
    }

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

    public static void teleportToSpawn(Player player) {
        player.teleport(getRandomLocation());
    }

    public static List<Block> getBlocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++)
            for (int z = bottomBlockZ; z <= topBlockZ; z++)
                for (int y = bottomBlockY; y <= topBlockY; y++)
                    blocks.add(loc1.getWorld().getBlockAt(x, y, z));

        return blocks;
    }

    public static void teleportToLobby(Player player) {
        Location location = new Location(world, config.getDouble("lobby.x"), config.getDouble("lobby.y"), config.getDouble("lobby.z"));

        location.setPitch((float) config.getDouble("lobby.pitch"));
        location.setYaw((float) config.getDouble("lobby.yaw"));

        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы были телепортированы в Лобби!");
        player.teleport(location);
    }

    public static Location getRandomLocation() {
        return availableSpawnLocations.get(Utils.getRandom(0, availableSpawnLocations.size() - 1));
    }

    public static Location getSpawnLocation() {
        FileConfiguration config = Main.main.getConfig();
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        World world = Bukkit.getWorld(config.getString("spawn.world"));

        return new Location(world, x, y, z);
    }

    public static Location getCenter(Location loc) {
        return new Location(loc.getWorld(),
                getRelativeCoordination(loc.getBlockX()),
                getRelativeCoordination(loc.getBlockY()),
                getRelativeCoordination(loc.getBlockZ()));
    }

    private static double getRelativeCoordination(int i) {
        double d = i;
        d = d < 0 ? d - .5 : d + .5;
        return d;
    }

    public static void vectorsBetweenLocations(Location fromLocation, Location toLocation, Particle particle) {
        Vector target = new Vector(fromLocation.getX(), fromLocation.getY(), fromLocation.getZ());

        toLocation.setDirection(target.subtract(toLocation.toVector()));
        Vector increase = toLocation.getDirection();

        int distance = (int) fromLocation.distance(toLocation) + 1;

        for (int counter = 0; counter < distance; counter++) {
            Location loc = toLocation.add(increase);
            fromLocation.getWorld().spawnParticle(particle, loc, 0, 0, 0, 0, 1);
        }
    }
}
