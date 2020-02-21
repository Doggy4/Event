package RoundList;

import PluginUtilities.LocationUtulities;
import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import RoundSystem.GameRules;
import RoundSystem.RoundSystem;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class RoundHideUnderBlocks implements Listener {
    public static boolean isActivated = false;

    private static World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

    private static Location loc1 = new Location(world, LocationUtulities.getSpawnLocation().getX() + 17, LocationUtulities.getSpawnLocation().getY() + 16, LocationUtulities.getSpawnLocation().getZ() + 16);
    private static Location loc2 = new Location(world, LocationUtulities.getSpawnLocation().getX() - 15, LocationUtulities.getSpawnLocation().getY() + 16, LocationUtulities.getSpawnLocation().getZ() - 16);

    private static List<Block> blocks = LocationUtulities.getBlocksFromTwoPoints(loc1, loc2);
    private static BukkitTask runnable;

    public static void hideUnderBlocks() {
        isActivated = true;
        RoundSystem.roundSeconds = 60;
        GameRules.PlayerDamageOff();


        runnable = new BukkitRunnable() {

            @Override
            public void run() {
                MapRebuild.loadSchematic("hide-arena");

                for (int i = 0; i < 8; i++) {
                    int randX = Math.round((float) Main.main.getConfig().getDouble("spawn.x")) + Utilities.getRandom(0, 32) - 16;
                    int randZ = Math.round((float) Main.main.getConfig().getDouble("spawn.z")) + Utilities.getRandom(0, 32) - 16;
                    int y = Math.round((float) Main.main.getConfig().getDouble("spawn.y")) + 2;

                    world.getBlockAt(randX, y, randZ).setType(Material.DIORITE_SLAB);
                }

                for (Player player : Queue.redQueueList) {
                    gameRulesAnnouncement(player);
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Block block : blocks)
                            world.spawn(LocationUtulities.getCenter(block.getLocation()), Snowball.class);
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
        runnable.cancel();
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
