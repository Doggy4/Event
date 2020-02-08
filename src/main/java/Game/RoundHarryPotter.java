package Game;

import PluginUtilities.Items;
import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoundHarryPotter implements Listener {

    protected static boolean isActivated = false;

    private static List<Player> cooldown = new ArrayList<Player>();

    protected static void harryPotter() {
        cooldown.clear();
        isActivated = true;
        aRoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.sendMessage(ChatColor.GOLD + "[Дамблдор] " + ChatColor.WHITE + "Да начнется сражение!");
            player.sendTitle(ChatColor.GREEN + "Стреляйте в ", "своих соперников", 20, 20, 20);
            player.getInventory().setItem(0, Items.stickEventHarryPotter);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!isActivated) return;
        Player player = e.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;
        if (cooldown.contains(player)) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Пожалуйста, подождите!");
            return;
        }
        cooldown.add(player);
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();

        if (e.getAction() == Action.RIGHT_CLICK_AIR && item.getItemMeta().getDisplayName().equals(Items.stickEventHarryPotter.getItemMeta().getDisplayName())) {
            new BukkitRunnable() {
                double t = 0;
                Location location = player.getLocation();
                World world = location.getWorld();
                Vector direction = location.getDirection().normalize();

                public void run() {
                    t += 0.5;

                    double x = direction.getX() * t;
                    double y = direction.getY() * t + 1.5;
                    double z = direction.getZ() * t;

                    location.add(x, y, z);
                    location.getWorld().spawnParticle(Particle.END_ROD, location, 0, 0, 0, 0, 1);
                    Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 0, 0, 0);
                    location.subtract(x, y, z);

                    for (Entity entity : nearbyEntities) {
                        if (!(entity instanceof Player)) return;
                        Player hited = ((Player) entity).getPlayer();
                        if (hited == player) return;

                        aRoundSystem.addScore(player, 1);
                        aRoundSystem.addScore(hited, -1);

                        hited.getLocation().getWorld().spawnParticle(Particle.FLAME, hited.getLocation(), 0);
                        hited.getLocation().getWorld().playSound(hited.getLocation(), Sound.ENTITY_VILLAGER_HURT, 2, 1);
                    }

                    if (t > 8) {
                        cooldown.remove(player);
                        this.cancel();
                    }
                }
            }.runTaskTimer(Main.main, 0, 1);
        }
    }
}
