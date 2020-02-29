package RoundList;

import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

public class RoundDropParkour implements Listener {
    public static boolean isActivated = false;
    private static List<Material> clays = Arrays.asList(Material.TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.LIME_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.PINK_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.WHITE_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA);

    private static BukkitTask runnable;

    public static void startRound() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 40;
        MapRebuild.loadSchematic("arena");
        RoundRules.PlayerDamageOff();


        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            player.teleport(LocationUtils.addLocation(LocationUtils.arenaCentre, 0, 35, 0));
            LocationUtils.world.getBlockAt(LocationUtils.addLocation(LocationUtils.arenaCentre, 0, 33, 0)).setType(Material.DIAMOND_BLOCK);
        }

        for (Block block : LocationUtils.getBlocksFromTwoPoints(LocationUtils.addLocation(LocationUtils.arenaLeft, 0, 1, 0), LocationUtils.addLocation(LocationUtils.arenaRight, 0, 35, 0)))
            if (Utils.getRandom(0, 500) == 1)
                block.setType(clays.get(Utils.getRandom(0, clays.size() - 1)));

        runnable = new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Queue.redQueueList)
                    if (player.getGameMode().equals(GameMode.ADVENTURE))
                        if (player.getLocation().getY() <= LocationUtils.arenaCentre.getY() + 1) {
                            player.setGameMode(GameMode.SPECTATOR);
                            RoundSystem.playerWin(player);
                        }

            }
        }.runTaskTimer(Main.main, 20, 20);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Спуститесь вниз ", "но не умрите", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Аккуратно спуститесь вниз!");
    }

    public static void endDropParkour() {
        isActivated = false;
        runnable.cancel();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!isActivated) return;
        Player player = event.getEntity();
        if (!Queue.redQueueList.contains(player)) return;
        RoundSystem.playerLose(player);
    }
}
