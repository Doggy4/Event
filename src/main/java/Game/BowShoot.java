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
import org.bukkit.event.entity.ProjectileHitEvent;
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
    private static Location oldLoc = block.getLocation();

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

                Location newLoc = block.getLocation().add(rand_x,0 ,rand_z);
                World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

                block.setType(Material.AIR);
                block = world.getBlockAt(rand_x, Main.main.getConfig().getInt("spawn.y") + 5, rand_z);
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
    public void OnProjHit(ProjectileHitEvent e) {
        if (!isActivated) return;

        Arrow arrow = (Arrow) e.getEntity();
        Player player = (Player) arrow.getShooter();

        Block hitBlock = e.getHitBlock();

        if (hitBlock.getType().equals(targets[n]) && !(hitBlock.getType().isAir())) {
            aGameCycle.addScore(player, place);
            place++;
            player.getInventory().clear();
        }
        if (place > 3) {
            block.setType(Material.AIR);
            block.getWorld().spawnParticle(Particle.EXPLOSION_HUGE,  block.getLocation(), 1);
            block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,10,1);
            isActivated = false;
            aGameCycle.isAnyBattleEnabled = isActivated;
            place = 1;
        }
    }
}



