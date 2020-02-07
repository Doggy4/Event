package Game;

import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class RoundCakeParkour implements Listener {
    protected static boolean isActivated = false;

    protected static void cakeParkour() {
        // Опционально:
        isActivated = true;
        aRoundSystem.roundSeconds = 20;
        MapRebuild.loadSchematic("parkour-cake1");

        for (Player player : Queue.redQueueList)
            gameRulesAnnouncement(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Пройдите паркур", ChatColor.YELLOW + "Съешьте тортик", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Пройдите паркур и съешьте тортик!");
    }

    @EventHandler
    public void onCakeEat(PlayerInteractEvent event) {
        if (!isActivated || event.getClickedBlock() == null) return;
        Player player = event.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getClickedBlock().getType().equals(Material.CAKE)) {
            aRoundSystem.addScore(player, 1);
            player.setGameMode(GameMode.SPECTATOR);
        }

    }
}
