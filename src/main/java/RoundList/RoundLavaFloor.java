package RoundList;

import PluginUtils.LocationUtils;
import QueueSystem.Queue;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RoundLavaFloor {
    public static boolean isActivated = false;

    private static BukkitTask runnable;

    public static void startRound() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("lavafloor-arena");

        for (Player player : Queue.redQueueList) {
            player.sendTitle(ChatColor.GREEN + "Не упадите", "в лаву", 20, 20, 20);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Не упадите в лаву!");
        }

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    Block block = LocationUtils.world.getBlockAt(LocationUtils.addLocation(LocationUtils.getRandomLocation(), 0, -1, 0));

                    if (block.getType().equals(Material.WHITE_WOOL)) block.setType(Material.YELLOW_WOOL);
                    else if (block.getType().equals(Material.YELLOW_WOOL)) block.setType(Material.ORANGE_WOOL);
                    else if (block.getType().equals(Material.ORANGE_WOOL)) block.setType(Material.RED_WOOL);
                    else if (block.getType().equals(Material.RED_WOOL)) block.setType(Material.AIR);
                }

                for (Player player : Queue.redQueueList)
                    if (player.getLocation().getY() < Math.round((float) Main.main.getConfig().getDouble("spawn.y") - 3)) {
                        if (!(player.getGameMode() == GameMode.SPECTATOR))
                            RoundSystem.playerLose(player);
                        LocationUtils.teleportToSpawn(player);
                    }
            }

        }.runTaskTimer(Main.main, 3, 3);
    }

    public static void endLavaFloor() {
        isActivated = false;
        runnable.cancel();
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                RoundSystem.playerWin(player);
    }

}
