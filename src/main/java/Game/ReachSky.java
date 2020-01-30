package Game;

import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ReachSky {
    private static boolean isActivated = false;

    public static void ReachSky() {
        isActivated = true;
        RoundSystem.roundSeconds = 15;

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.setGameMode(GameMode.SURVIVAL);

            player.sendTitle(ChatColor.GREEN + "Летите вверх!", "Исследуйте неизвестное!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Летите вверх!");

            player.setAllowFlight(true);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(RoundSystem.isRoundTimerEnabled)) {
                    isActivated = RoundSystem.isRoundTimerEnabled;

                    for (Player player : Queue.redQueueList) player.setAllowFlight(false);

                    this.cancel();
                }

                for (Player player : Queue.redQueueList) {
                    if (player.getLocation().getY() >= 150) {
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 10);
                        player.setAllowFlight(false);
                        RoundSystem.addScore(player, 1);
                    }
                }
            }
        }.runTaskTimer(Main.main, 40, 40);
    }
}
