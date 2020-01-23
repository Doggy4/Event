package SvistoPerdelki;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class TestParticle implements Listener {
    // Боп вспоминает как юзать геометрию
    @EventHandler
    public static void ParticleHandler(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location loc = player.getLocation();
        Location corner1 = loc.add(3,3,3);
        Location corner2 = loc.add(-3,-3,-3);
        for (Location location : getHollowCube(corner1,corner2)) {
            location.getWorld().spawnParticle(Particle.HEART,location,1);
        }
    }

    public static List<Location> getHollowCube(Location corner1, Location corner2) {
        List<Location> result = new ArrayList<Location>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (double x = minX; x <= maxX; x += 0.2D) {
            for (double z = minZ; z <= maxZ; z += 0.2D) {
                result.add(new Location(world, x, minY, z));
                result.add(new Location(world, x, maxY, z));
            }
        }

        for (double x = minX; x <= maxX; x += 0.2D) {
            for (double y = minY; y <= maxY; y += 0.2D) {
                result.add(new Location(world, x, y, minZ));
                result.add(new Location(world, x, y, maxZ));
            }
        }

        for (double z = minZ; z <= maxZ; z += 0.2D) {
            for (double y = minY; y <= maxY; y += 0.2D) {
                result.add(new Location(world, minX, y, z));
                result.add(new Location(world, maxX, y, z));
            }
        }
        return result;
    }
}

