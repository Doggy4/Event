package SvistoPerdelki;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class Particles {
    public static void createBlockSplash(Location location, Particle particle, Particle particle2) {
        World world = location.getWorld();

        double startX = location.add(-0.5, 0, 0).getX();
        double startY = location.getY();
        double startZ = location.add(0, 0, -0.5).getZ();

        double step = 0.5D;

        for (double x = 0; x < 3; x += step) {
            for (double y = 0; y < 3; y += step) {
                for (double z = 0; z < 3; z += step) {
                    Location loc = location.add(startX + x, startY + y, startZ + z);
                    if (y != 3 && y != 0) {
                        if ((x >= 0 && z == 0) || (x >= 0 && z == 2) || (x == 0 && z >= 0) || (x == 2 && z >= 0))
                            world.spawnParticle(particle, loc, 0);
                        else
                            world.spawnParticle(particle, loc, 0);
                    }
                }
            }
        }
    }
}