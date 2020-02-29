package RoundList;

import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RoundHideUnderBlocks implements Listener {
    public static boolean isActivated = false;

    private static BukkitTask runnable1;
    private static BukkitTask runnable2;

    public static void startRound() {
        isActivated = true;
        RoundSystem.roundSeconds = 60;
        RoundRules.PlayerDamageOff();


        runnable1 = new BukkitRunnable() {
            @Override
            public void run() {
                MapRebuild.loadSchematic("hide-arena");
                for (int i = 0; i < 8; i++)
                    LocationUtils.world.getBlockAt(LocationUtils.addLocation(LocationUtils.getRandomLocation(), 0, 3, 0)).setType(Material.DIORITE_SLAB);

                for (Player player : Queue.redQueueList)
                    gameRulesAnnouncement(player);

                runnable2 = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Location location : LocationUtils.availableSpawnLocations) {
                            LocationUtils.world.spawn(LocationUtils.getCenter(LocationUtils.addLocation(location, 0, 15, 0)), Snowball.class);
                        }
                    }
                }.runTaskLater(Main.main, 60);
            }
        }.runTaskTimer(Main.main, 40, 160);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Спрячьтесь от", "снежков", 20, 20, 20);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Спрячьтесь от снежков!");
    }

    public static void endHideUnderBlocks() {
        isActivated = false;
        runnable1.cancel();
        runnable2.cancel();
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                RoundSystem.playerWin(player);
    }

    @EventHandler
    public void onSnowball(EntityDamageEvent event) {
        if (!isActivated) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
            RoundSystem.playerLose(player);
    }

}
