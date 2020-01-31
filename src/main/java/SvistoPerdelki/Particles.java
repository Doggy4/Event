package SvistoPerdelki;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;


public class Particles {

    public static void fireBlock(Location location) {
        World world = location.getWorld();
        double startX = 0.0D;
        double startY = 0.0D;
        double startZ = 0.0D;

        double step = 0.1D;

        for (double x = 0; x < 3; x += step) {
            for (double z = 0; z < 3; z += step) {
                for (double y = 0; y <= 3; y += step) {
                    Location loc = new Location(world, startX + x, startY + y, startZ + z);
                    if (y != 3 && y != 0) {
                        if ((x >= 0 && z == 0) || (x >= 0 && z == 2) || (x == 0 && z >= 0) || (x == 2 && z >= 0))
                            world.spawnParticle(Particle.FLAME, loc, 1);
                    } else {
                        world.spawnParticle(Particle.CLOUD, loc, 1);
                    }
                }
            }
        }
    }
}
