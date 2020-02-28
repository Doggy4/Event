package RoundList;

import PluginUtils.Items;
import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class RoundSlimePvP implements Listener {
    public static boolean isActivated = false;

    public static void slimePvP() {
        isActivated = true;
        RoundSystem.roundSeconds = 20;
        MapRebuild.loadSchematic("cactus-arena");
        RoundRules.PlayerDamageOff();

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            player.getInventory().addItem(Items.slimeEventSlimePvP);
        }
    }

    public static void endSlimePvP() {
        isActivated = false;
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                RoundSystem.playerWin(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Столкните соперников", "в кактусы", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Столкните соперников в кактусы!");
    }

    @EventHandler
    public void onCactusHit(EntityDamageEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
            RoundSystem.playerLose(player);
            LocationUtils.teleportToSpawn(player);
        }
    }
}
