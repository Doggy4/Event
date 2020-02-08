package Game;

import PluginUtilities.LocationUtulities;
import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RoundLavaFloor {
    protected static boolean isActivated = false;

    private static BukkitTask runnable;

    protected static void lavaFloor() {
        isActivated = true;
        aRoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("lavafloor-arena");

        for (Player player : Queue.redQueueList) {
            player.sendTitle(ChatColor.GREEN + "Не упадите", "в лаву", 20, 20, 20);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Не упадите в лаву!");
        }

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    int randX = Math.round((float) Main.main.getConfig().getDouble("spawn.x")) + Utilities.getRandom(0, 34) - 17;
                    int randZ = Math.round((float) Main.main.getConfig().getDouble("spawn.z")) + Utilities.getRandom(0, 34) - 17;
                    int y = Math.round((float) Main.main.getConfig().getDouble("spawn.y") - 1);

                    Block block = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(randX, y, randZ);

                    if (block.getType() == Material.WHITE_WOOL) block.setType(Material.YELLOW_WOOL);
                    else if (block.getType() == Material.YELLOW_WOOL) block.setType(Material.ORANGE_WOOL);
                    else if (block.getType() == Material.ORANGE_WOOL) block.setType(Material.RED_WOOL);
                    else if (block.getType() == Material.RED_WOOL) block.setType(Material.AIR);
                }

                for (Player player : Queue.redQueueList)
                    if (player.getLocation().getY() < Math.round((float) Main.main.getConfig().getDouble("spawn.y") - 3)) {
                        if (!(player.getGameMode() == GameMode.SPECTATOR))
                            aRoundSystem.playerLose(player);
                        LocationUtulities.teleportToSpawn(player);
                    }
            }

        }.

                runTaskTimer(Main.main, 5, 5);
    }

    protected static void endLavaFloor() {
        isActivated = false;
        runnable.cancel();
        for (Player player : Queue.redQueueList)
            if (player.getGameMode() != GameMode.SPECTATOR)
                aRoundSystem.playerWin(player);
    }

}
