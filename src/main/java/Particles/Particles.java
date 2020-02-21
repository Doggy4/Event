package Particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class Particles {
    public static void createBlockSplash(Location location, Particle particle) {
        World world = location.getWorld();
        double k = 2;

        for (double t = 0; t < 5; t += 0.5) {
            double x = Math.cos(k*t) * Math.cos(t);
            double z = Math.cos(k*t) * Math.sin(t);

            Location newLoc = new Location(location.getWorld(), (float) location.getX() + x, (float) location.getY() + 0.5, (float) location.getZ() + z);
            location.getWorld().spawnParticle(particle, newLoc, 1);
        }
    }
}