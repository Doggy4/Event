package Game;

import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundReachTheSky {
    protected static boolean isActivated = false;

    protected static void reachTheSky() {
        isActivated = true;
        aRoundSystem.roundSeconds = 15;
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.sendTitle(ChatColor.GREEN + "Летите вверх!", "Исследуйте неизвестное!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Летите вверх!");
            player.setAllowFlight(true);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActivated) {
                    for (Player player : Queue.redQueueList) player.setAllowFlight(false);
                }

                for (Player player : Queue.redQueueList) {
                    if (player.getLocation().getY() >= 150) {
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 10);
                        player.setAllowFlight(false);
                        aRoundSystem.addScore(player, 1);
                    }
                }
            }
        }.runTaskTimer(Main.main, 40, 40);
    }

}
