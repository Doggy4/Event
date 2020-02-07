package Game;

import PluginUtilities.Items;
import PluginUtilities.LocationUtulities;
import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KnockOff implements Listener {
    protected static boolean isActivated = false;

    public static void knockOff() {
        isActivated = true;
        RoundSystem.roundSeconds = 60;
        GameRules.EntityDamageOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.getInventory().addItem(Items.stickEventKnockOff);
            gameRulesAnnouncement(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActivated) {
                    endKnockOff();
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.main, 5, 5);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Столкните соперников", "в бездну", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Столкните соперников в бездну!");
    }

    private static void endKnockOff() {
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                RoundSystem.playerWin(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (event.getDamage() > 2) {
            RoundSystem.playerLose(player);
            LocationUtulities.teleportToSpawn(player);
        }
    }
}
