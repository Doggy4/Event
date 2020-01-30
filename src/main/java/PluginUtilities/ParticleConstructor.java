package PluginUtilities;

import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class ParticleConstructor {
    private HashMap<String, Integer> locations = new HashMap<>();
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("");
    private int range = 25;

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

    public static void spiral(Location loc) {
        for (double t = 0; t < 60; t += 0.5) {
            double x = t * Math.cos(6*t);
            double y = t * Math.sin(6*t);

            Location newLoc = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + t);
            loc.getWorld().spawnParticle(Particle.FLAME, newLoc, 1);
        }
    }


}
