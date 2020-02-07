package Game;

import PluginUtilities.Items;
import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class RoundFuckingHarryPotter {

    public static boolean isActivated = false;

    public static void harryPotter() {
        isActivated = true;
        aRoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.sendMessage(ChatColor.RED + "[Дамблдор] " + ChatColor.WHITE + "Ебите друг-друга палками");
            player.getInventory().setItem(0, Items.stickOfFuckingHarryPotter);

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR && item == Items.stickOfFuckingHarryPotter) {
            new BukkitRunnable() {
                double t = 0;
                int hitCooldown = 40;
                Location location = player.getLocation();
                World world = location.getWorld();
                Vector direction = location.getDirection().normalize();
                public void run() {
                    double x = direction.getX() * t;
                    double y = direction.getX() * t + 0.5;
                    double z = direction.getX() * t;

                    location.add(x, y, z);
                    location.getWorld().spawnParticle(Particle.END_ROD, location, 0, 0, 0, 0,1 );
                    location.subtract(x ,y ,z);

                    List<Entity> hitedEnemies = (List<Entity>) world.getNearbyEntities(location, 0.3, 0.3, 0.3);

                    for (Entity entity : world.getEntities()) {
                        if (hitedEnemies.contains(entity) && entity instanceof Player && hitCooldown <= 0) {
                            hitCooldown = 40;
                            Player hited = ((Player) entity).getPlayer();
                            aRoundSystem.addScore(player, 1);
                            aRoundSystem.addScore(hited, 0);

                            hited.getLocation().getWorld().spawnParticle(Particle.FLAME,0,0,0,0,1);
                            hited.getLocation().getWorld().playSound(hited.getLocation(), Sound.ENTITY_VILLAGER_HURT,2,1);
                        }
                    }
                    t--;
                    if (hitCooldown !=0) hitCooldown--;
                    if (t > 8) this.cancel();
                }
            }.runTaskTimer(Main.main, 0, 1);
        }
    }
}
