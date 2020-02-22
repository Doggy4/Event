package RoundList;

import PluginUtils.Items;
import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import RoundSystem.GameRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class RoundKnockEveryoneOff implements Listener {
    public static boolean isActivated = false;

    public static void knockEveryoneOff() {
        isActivated = true;
        RoundSystem.roundSeconds = 60;
        GameRules.PlayerDamageOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.getInventory().addItem(Items.stickEventKnockOff);
            gameRulesAnnouncement(player);
        }
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Столкните соперников", "в бездну", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Столкните соперников в бездну!");
    }

    public static void endKnockOff() {
        isActivated = false;
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                RoundSystem.playerWin(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            RoundSystem.playerLose(player);
            LocationUtils.teleportToSpawn(player);
        }
    }
}
