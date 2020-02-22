package RoundList;

import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RoundTNTRun {
    public static boolean isActivated = false;

    private static BukkitRunnable runnable1;
    private static BukkitRunnable runnable2;

    public static void TNTRun() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("tntrun-arena");

        for (Player player : Queue.redQueueList)
            gameRulesAnnouncement(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable1 = this;
                for (Player player : Queue.redQueueList)
                    if (player.isOnGround()) {
                        Location location = player.getLocation();

                        int x1 = (int) Math.ceil(location.getX());
                        int x2 = (int) Math.floor(location.getX());
                        int y = (int) location.getY();
                        int z1 = (int) Math.ceil(location.getZ());
                        int z2 = (int) Math.floor(location.getZ());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                runnable2 = this;
                                location.getWorld().getBlockAt(x1, y - 1, z1).setType(Material.AIR);
                                location.getWorld().getBlockAt(x1, y - 2, z1).setType(Material.AIR);
                                location.getWorld().getBlockAt(x2, y - 1, z1).setType(Material.AIR);
                                location.getWorld().getBlockAt(x2, y - 2, z1).setType(Material.AIR);

                                location.getWorld().getBlockAt(x2, y - 1, z2).setType(Material.AIR);
                                location.getWorld().getBlockAt(x2, y - 2, z2).setType(Material.AIR);
                                location.getWorld().getBlockAt(x1, y - 1, z2).setType(Material.AIR);
                                location.getWorld().getBlockAt(x1, y - 2, z2).setType(Material.AIR);
                            }
                        }.runTaskLater(Main.main, 10);

                    }
            }
        }.runTaskTimer(Main.main, 1, 1);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.RED + "TNT" + ChatColor.WHITE + "Run", ChatColor.GREEN + "Не упадите вниз", 20, 20, 20);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "TNT" + ChatColor.WHITE + "Run " + ChatColor.GREEN + "- не упадите вниз!");
    }

    public static void endTNTRun() {
        isActivated = false;

        runnable1.cancel();
        runnable2.cancel();
    }

}
