package RoundList;

import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import RoundSystem.GameRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundAnvilEscape implements Listener {
    public static boolean isActivated = false;

    private static BukkitRunnable runnable;

    public static void anvilEscape() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 15;
        GameRules.PlayerDamageOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable = this;
                for (int i = 0; i < 50; i++)
                    LocationUtils.world.getBlockAt(LocationUtils.getRandomLocation()).setType(Material.ANVIL);
            }
        }.runTaskTimer(Main.main, 10, 10);
    }

    public static void endDodgeAnvils() {
        isActivated = false;
        runnable.cancel();
        for (Player roundPlayer : Queue.redQueueList)
            if (roundPlayer.getGameMode() != GameMode.ADVENTURE)
                RoundSystem.playerWin(roundPlayer);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Спаситесь от", "падающих наковален", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Спаситесь от падающих наковален!");
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        Block block = event.getBlock();

        int x = Math.round((float) block.getLocation().getX());
        int y = Math.round((float) block.getLocation().getY()) - 1;
        int z = Math.round((float) block.getLocation().getZ());

        if (event.getEntity() instanceof FallingBlock)
            if (event.getEntity().getLocation().getWorld().getBlockAt(x, y, z).getType() != Material.AIR)
                event.setCancelled(true);
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!Queue.redQueueList.contains(player)) return;

        RoundSystem.playerLose(player);
    }
}
