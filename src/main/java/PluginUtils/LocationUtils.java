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

        Double x = config.getDouble("spawn.x") + (Utils.getRandom(0, 20) - 10);
        Double z = config.getDouble("spawn.z") + (Utils.getRandom(0, 20) - 10);

        Location location = new Location(world, x, config.getDouble("spawn.y"), z);

        location.setPitch((float) config.getDouble("spawn.pitch"));
        location.setYaw((float) config.getDouble("spawn.yaw"));

        player.teleport(location);
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
