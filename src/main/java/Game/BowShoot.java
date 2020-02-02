package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import SvistoPerdelki.Particles;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static PluginUtilities.Items.BowEventArrows;
import static PluginUtilities.Items.BowEventBow;

public class BowShoot implements Listener {
    private static boolean isActivated = false;

    private static Material[] targets = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK, Material.REDSTONE_BLOCK};
    private static Material bonusTarget = Material.GLOWSTONE;

    private static int blockChance = Utilities.getRandom(0,100);
    private static int n = (int) Math.floor(Math.random() * targets.length);

    private static Block block = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(0, 0, 0);
    private static Block bonusBlock = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(0, 0, 0);

    public static void BowShoot() {
        RoundSystem.roundSeconds = 30;

        isActivated = true;

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Стреляйте в блоки ", targets[n].toString(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Попадите в блок " + ChatColor.LIGHT_PURPLE + "[" + targets[n].toString() + "]");
            player.getInventory().addItem(BowEventBow);
            player.getInventory().addItem(BowEventArrows);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(RoundSystem.isRoundTimerEnabled)) {
                    block.setType(Material.AIR);
                    bonusBlock.setType(Material.AIR);
                    this.cancel();
                }

                block.setType(Material.AIR);
                bonusBlock.setType(Material.AIR);

                int rand_x = Main.main.getConfig().getInt("spawn.x") + Utilities.getRandom(1, 20) - 10;
                int rand_z = Main.main.getConfig().getInt("spawn.z") + Utilities.getRandom(1, 20) - 10;

                int rand_bonus_x = Main.main.getConfig().getInt("spawn.x") + Utilities.getRandom(1, 20) - 10;
                int rand_bonus_z = Main.main.getConfig().getInt("spawn.z") + Utilities.getRandom(1, 20) - 10;

                World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

                block = world.getBlockAt(rand_x, Main.main.getConfig().getInt("spawn.y") + 5, rand_z);
                bonusBlock = world.getBlockAt(rand_bonus_x, Main.main.getConfig().getInt("spawn.y") + 10, rand_bonus_z);

                Location targetLoc = block.getLocation();
                Location bonusLoc = bonusBlock.getLocation();

                if (blockChance >=35) bonusBlock.setType(bonusTarget);
                block.setType(targets[n]);

                double step = 0.5D;

                Vector line = targetLoc.add(rand_x,0,rand_z).toVector().subtract(targetLoc.toVector());
                for (double d = 0; d < line.length(); d += step) {
                    line.multiply(d);
                    targetLoc.add(line);

                    world.spawnParticle(Particle.END_ROD, targetLoc, 1);

                    targetLoc.subtract(line);
                    line.normalize();
                }

                Vector bonusLine = bonusLoc.add(rand_x,0,rand_z).toVector().subtract(bonusLoc.toVector());
                for (double d = 0; d < bonusLine.length(); d += step) {
                    bonusLine.multiply(d);
                    bonusLoc.add(bonusLine);

                    world.spawnParticle(Particle.FLAME, targetLoc, 1);

                    bonusLoc.subtract(bonusLine);
                    bonusLine.normalize();
                }

                world.playSound(block.getLocation(), Sound.BLOCK_BELL_USE, 1, 2);
                Particles.createBlockSplash(block.getLocation(), Particle.END_ROD);

                world.playSound(bonusBlock.getLocation(), Sound.BLOCK_BELL_RESONATE, 1,3);
                Particles.createBlockSplash(bonusBlock.getLocation(), Particle.DRAGON_BREATH);
            }
        }.runTaskTimer(Main.main, 20, 20);
    }

    public static void disableEvents() {
        isActivated = false;

        bonusBlock.setType(Material.AIR);
        bonusBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 1);
        bonusBlock.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);

        block.setType(Material.AIR);
        block.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 1);
        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
    }

    @EventHandler
    public void OnProjHit(ProjectileHitEvent e) {
        if (!isActivated) return;

        Arrow arrow = (Arrow) e.getEntity();
        Player player = (Player) arrow.getShooter();

        Block hitBlock = e.getHitBlock();

        if (hitBlock.getType().equals(targets[n])) {
            RoundSystem.addScore(player, 1);
        } else if (hitBlock.getType().equals(bonusTarget)) {
            RoundSystem.addScore(player, 5);
        }
    }
}



