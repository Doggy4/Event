package RoundList;

import Particles.Particles;
import PluginUtils.Chat;
import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static PluginUtils.Items.BowEventBow;
import static PluginUtils.Items.bowEventArrows;

public class RoundHitTheBlock implements Listener {
    public static boolean isActivated = false;

    private static Material[] targets = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK, Material.REDSTONE_BLOCK};
    private static Material bonusTarget = Material.GLOWSTONE;

    private static int n = (int) Math.floor(Math.random() * targets.length);

    private static Block block = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(0, 0, 0);
    private static Block bonusBlock = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(1, 1, 1);
    private static BukkitRunnable runnable;

    public static void hitTheBlock() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            player.getInventory().addItem(BowEventBow);
            player.getInventory().addItem(bowEventArrows);

        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable = this;

                block.setType(Material.AIR);
                bonusBlock.setType(Material.AIR);


                int rand_x = Main.main.getConfig().getInt("spawn.x") + Utils.getRandom(1, 20) - 10;
                int rand_z = Main.main.getConfig().getInt("spawn.z") + Utils.getRandom(1, 20) - 10;

                int rand_bonus_x = Main.main.getConfig().getInt("spawn.x") + Utils.getRandom(1, 20) - 10;
                int rand_bonus_z = Main.main.getConfig().getInt("spawn.z") + Utils.getRandom(1, 20) - 10;

                World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

                LocationUtils.vectorsBetweenLocations(LocationUtils.getCenter(block.getLocation()), LocationUtils.getCenter(new Location(block.getWorld(), rand_x, Main.main.getConfig().getInt("spawn.y") + 5, rand_z)), Particle.FLAME);
                LocationUtils.vectorsBetweenLocations(LocationUtils.getCenter(bonusBlock.getLocation()), LocationUtils.getCenter(new Location(bonusBlock.getWorld(), rand_bonus_x, Main.main.getConfig().getInt("spawn.y") + 10, rand_bonus_z)), Particle.FLAME);


                block = world.getBlockAt(rand_x, Main.main.getConfig().getInt("spawn.y") + 5, rand_z);
                bonusBlock = world.getBlockAt(rand_bonus_x, Main.main.getConfig().getInt("spawn.y") + 10, rand_bonus_z);

                Location targetLoc = block.getLocation();
                Location bonusLoc = bonusBlock.getLocation();

                bonusBlock.setType(bonusTarget);
                block.setType(targets[n]);

                double step = 0.5D;

                world.playSound(block.getLocation(), Sound.BLOCK_BELL_USE, 1, 2);
                Particles.createBlockSplash(block.getLocation(), Particle.END_ROD);

                world.playSound(bonusBlock.getLocation(), Sound.BLOCK_BELL_RESONATE, 1, 3);
                Particles.createBlockSplash(bonusBlock.getLocation(), Particle.DRAGON_BREATH);
            }
        }.runTaskTimer(Main.main, 20, 20);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Попадите в блок ", Chat.translate(targets[n].name()), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Попадите в блок " + ChatColor.LIGHT_PURPLE + "[" + Chat.translate(targets[n].name()) + "]");
    }

    public static void endHitTheBlock() {
        bonusBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 1);
        bonusBlock.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);

        block.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 1);
        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);

        runnable.cancel();
    }

    @EventHandler
    public void OnProjectileHit(ProjectileHitEvent event) {
        if (!isActivated || !(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();
        if (!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();
        if (!Queue.redQueueList.contains(player)) return;
        Block hitBlock = event.getHitBlock();

        if (hitBlock.getType().equals(targets[n])) {
            RoundSystem.addScore(player, 1);
            hitBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hitBlock.getLocation(), 10);
        } else if (hitBlock.getType().equals(bonusTarget)) {
            RoundSystem.addScore(player, 5);
            hitBlock.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hitBlock.getLocation(), 10);
        }
    }
}



