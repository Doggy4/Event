package RoundList;

import PluginUtils.Items;
import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.CooldownManager;
import RoundUtils.MapRebuild;
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

import java.util.Collection;

public class RoundHarryPotter implements Listener {

    public static boolean isActivated = false;

    private final CooldownManager cooldownManager = new CooldownManager();

    public static void startRound() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
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
        ItemStack item = e.getItem();
        int cd = 2;
        int timeLeft = cooldownManager.getCooldown(player.getUniqueId(), "PotterWandCD");

        if (!(Queue.redQueueList.contains(player))) return;
        if (e.getItem() == null) return;
        if (timeLeft == 0) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR && item.getItemMeta().getDisplayName().equals(Items.stickEventHarryPotter.getItemMeta().getDisplayName())) {
                cooldownManager.setCooldown(player.getUniqueId(), "PotterWandCD", cd);
                new BukkitRunnable() {
                    double t = 16;
                    Location location = player.getLocation();
                    World world = location.getWorld();
                    Vector direction = location.getDirection().normalize();

                    public void run() {
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

                            RoundSystem.addScore(player, 1);
                            RoundSystem.addScore(hited, -1);

                            hited.getLocation().getWorld().spawnParticle(Particle.FLAME, hited.getLocation(), 0);
                            hited.getLocation().getWorld().playSound(hited.getLocation(), Sound.ENTITY_VILLAGER_HURT, 2, 1);
                        }

                        t--;

                        if (t == 0) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Main.main, 0, 1);
            }
        } else {
            player.sendMessage(ChatColor.DARK_RED + player.getDisplayName() + ChatColor.RED + " Похоже моя палочка еще не готова, еще " + ChatColor.DARK_RED + timeLeft + ChatColor.RED + " секунд.");
        }
    }
}
