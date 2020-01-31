package SvistoPerdelki;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;


public class Particles {

    public static void fireBlock(Location location) {
        World world = location.getWorld();
        double startX = location.getX();
        double startY = location.getY();
        double startZ = location.getZ();

        double step = 0.3D;

        for (double x = 0; x < 3; x += step) {
            for (double z = 0; z < 3; z += step) {
                for (double y = 0; y <= 3; y += step) {
                    Location loc =  location.add( startX + x, startY + y, startZ + z);
                    if (y != 3 && y != 0) {
                        if ((x >= 0 && z == 0) || (x >= 0 && z == 2) || (x == 0 && z >= 0) || (x == 2 && z >= 0))
                            world.spawnParticle(Particle.FLAME, loc, 0);
                    } else {
                        world.spawnParticle(Particle.CLOUD, loc, 0);
                    }
                }
            }
        }
    }
}
