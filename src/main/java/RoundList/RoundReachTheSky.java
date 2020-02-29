package RoundList;

import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RoundReachTheSky {
    public static boolean isActivated = false;
    private static BukkitTask runnable;

    public static void startRound() {
        isActivated = true;
        RoundSystem.roundSeconds = 15;
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.sendTitle(ChatColor.GREEN + "Летите вверх!", "Исследуйте неизвестное!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Летите вверх!");
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActivated) {
                    for (Player player : Queue.redQueueList) player.setAllowFlight(false);
                }

                for (Player player : Queue.redQueueList) {
                    if (player.getLocation().getY() >= LocationUtils.getSpawnLocation().getY() + 50 && player.getGameMode().equals(GameMode.ADVENTURE)) {
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 10);
                        RoundSystem.addScore(player, 1);
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }.runTaskTimer(Main.main, 1, 1);
    }

    public static void endReachTheSky() {
        isActivated = false;
        runnable.cancel();
    }

}
