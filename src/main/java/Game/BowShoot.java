package Game;

import PluginUtilities.ParticleConstructor;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;


import static PluginUtilities.Items.BowEventArrows;
import static PluginUtilities.Items.BowEventBow;

public class BowShoot implements Listener {
    private static boolean isActivated = false;

    private static Material[] targets = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK, Material.REDSTONE_BLOCK};
    private static int n = (int) Math.floor(Math.random() * targets.length);

    private static Block block = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(0, 0, 0);

    public static void BowShoot() {
        isActivated = aGameCycle.isAnyBattleEnabled;

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выстрелите в блок ", targets[n].toString(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Попадите в блок " + ChatColor.LIGHT_PURPLE + "[" + targets[n].toString() + "]");
            player.getInventory().addItem(BowEventBow);
            player.getInventory().addItem(BowEventArrows);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActivated) {
                    block.setType(Material.AIR);
                    this.cancel();
                }
                int rand_x = Main.main.getConfig().getInt("spawn.x") + Utilities.getRandom(1, 20) - 10;
                int rand_z = Main.main.getConfig().getInt("spawn.z") + Utilities.getRandom(1, 20) - 10;

                World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));
                block.setType(Material.AIR);
                Location oldLoc = block.getLocation();
                block = world.getBlockAt(rand_x, Main.main.getConfig().getInt("spawn.y") + 5, rand_z);
                Location newLoc = block.getLocation();
                block.setType(targets[n]);

                Vector line = newLoc.toVector().subtract(oldLoc.toVector());
                double step = 0.5D;

                for (double d = 0; d < line.length(); d += step) {
                    line.multiply(d);
                    oldLoc.add(line);

                    world.spawnParticle(Particle.END_ROD, oldLoc,1);

                    oldLoc.subtract(line);
                    line.normalize();
                }

                world.playSound(block.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
                ParticleConstructor.blockAnimation(block.getLocation(), 1);
            }
        }.runTaskTimer(Main.main, 20, 20);
    }



    private static int place = 1;

    @EventHandler
    public void OnShoot(EntityShootBowEvent e) {
        if (!isActivated) return;
        Player player = (Player) e.getEntity();
        Arrow arrow = (Arrow) e.getProjectile();
        World world = arrow.getWorld();
        BlockIterator iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 30);
        Block hitBlock = null;

        while (iterator.hasNext()) {
            Location arrowLoc = arrow.getLocation();
            arrowLoc.getWorld().spawnParticle(Particle.END_ROD,arrowLoc,1);
            hitBlock = iterator.next();
            if (!hitBlock.getType().equals(Material.AIR))
                break;
        }

        if (hitBlock.getType().equals(targets[n])) {
            aGameCycle.addScore(player, place);
            place++;
            player.getInventory().clear();
        }
        if (place > 3) {
            isActivated = false;
            aGameCycle.isAnyBattleEnabled = isActivated;
            place = 1;
        }
    }
}



