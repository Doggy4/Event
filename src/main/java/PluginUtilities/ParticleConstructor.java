package PluginUtilities;

import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticleConstructor {

    public static void circle(Location loc, float radius) {
        for (double t = 0; t < 25; t += 1) {
            float x = radius * (float) Math.sin(t);
            float z = radius * (float) Math.cos(t);

            Location newLoc = new Location(loc.getWorld(), (float) loc.getX() + x, (float) loc.getY() + 2.5, (float) loc.getZ() + z);
            loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, newLoc, 1);
        }
    }

    public static void blockAnimation(Location loc, float radius) {
        for (double t = 0; t < 50; t += 0.5) {
            float x = radius * (float) Math.sin(t);
            float z = radius * (float) Math.cos(t);

            Location newLoc = new Location(loc.getWorld(), (float) loc.getX() + x, (float) loc.getY(), (float) loc.getZ() + z);
            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, newLoc, 1);
        }
    }
}
