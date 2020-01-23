package Game;

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
        isActivated = GameCycle.isAnyBattleEnabled;

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

                Location oldLocation = block.getLocation();

                double rand_x = Main.main.getConfig().getDouble("spawn.x") + Utilities.getRandom(1, 20) - 10;
                double rand_z = Main.main.getConfig().getDouble("spawn.z") + Utilities.getRandom(1, 20) - 10;

                World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

                block.setType(Material.AIR);
                block = world.getBlockAt(Math.round((float) rand_x), Math.round((float) Main.main.getConfig().getDouble("spawn.y")) + 5, Math.round((float) rand_z));
                block.setType(targets[n]);

                Location newLocation = block.getLocation();

                oldLocation.subtract(newLocation);
                double distance = oldLocation.distance(newLocation);
                Vector direction = oldLocation.subtract(newLocation).toVector();

                for (double i = 0; i < distance; i += 0.1) {
                    Location particle = oldLocation.add(direction.normalize().multiply(i));
                    world.spawnParticle(Particle.END_ROD, particle, 1);
                }


                world.playEffect(block.getLocation(), Effect.SMOKE, 20, 20);
                world.playSound(block.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
                world.playEffect(block.getLocation(), Effect.SMOKE, 20, 20);




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
        BlockIterator iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 200);
        Block hitBlock = null;

        while (iterator.hasNext()) {
            hitBlock = iterator.next();
            if (!hitBlock.getType().equals(Material.AIR))
                break;
        }

        if (hitBlock.getType().equals(targets[n])) {
            GameCycle.addScore(player, place);
            place++;
            player.getInventory().clear();
        }
        if (place > 3) {
            isActivated = false;
            GameCycle.isAnyBattleEnabled = isActivated;
            place = 1;
        }
    }
}



