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
    private static int place = 1;

    public static void ReachSky() {
        isActivated = aGameCycle.isAnyBattleEnabled;

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.setGameMode(GameMode.SURVIVAL);

            player.sendTitle(ChatColor.GREEN + "Летите вверх!", "Исследуйте неизвестное!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Летите вверх!");

            player.setAllowFlight(true);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (place > 3) {
                    isActivated = false;
                    aGameCycle.isAnyBattleEnabled = false;
                    place = 1;

                    for (String playerName : Queue.redQueueList)
                        Bukkit.getPlayer(playerName).setAllowFlight(false);

                    this.cancel();
                }

                for (String playerName : Queue.redQueueList) {
                    Player player = Bukkit.getPlayer(playerName);

                    if (player.getLocation().getY() >= 150){
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 10);
                        player.setAllowFlight(false);
                        aGameCycle.playerWin(player, place);
                        place++;
                    }
                }
            }
        }.runTaskTimer(Main.main, 40, 40);
    }
}