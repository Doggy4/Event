package Game;

import Commands.CommandEvent;
import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
// Потом раздрочу этот класс
public class ParkourEatCake implements Listener {
    private static boolean isActivated = false;

    public static void parkour() {

        MapRebuild.loadSchematic("parkour-cake1");

        isActivated = true;
        RoundSystem.roundSeconds = 30;

        for (Player player : Queue.redQueueList) {
            CommandEvent.teleportToSpawn(player);

            player.getInventory().clear();

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.sendTitle(ChatColor.GREEN + "Пройдите паркур", ChatColor.YELLOW + "Съешьте тортик", 40, 40, 40);
            player.sendMessage(ChatColor.GREEN + "Пройдите паркур и съешьте тортик!");
        }
    }

    @EventHandler
    public void onCakeEat(PlayerInteractEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();

        if (event.getClickedBlock().getType().equals(Material.CAKE))
            RoundSystem.addScore(player, 1);
    }
}
