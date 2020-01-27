package Game;

import PluginUtilities.MapRebuild;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BuildTower implements Listener {
    // Добавить условия компановки блоков, например поставьте красную шерсть рядом с синей

    private static boolean isActivated = false;

    public static void BuildTower() {
        BaseClass.PlaceBlockOff();

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.setGameMode(GameMode.SURVIVAL);

            player.sendTitle(ChatColor.GREEN + "Постройте башню из", "20 блоков", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Постройте башню из 20 блоков!");

            for (int i = 0; i < 36; i++) player.getInventory().setItem(i, new ItemStack(Material.CYAN_WOOL, 1));
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        if (!isActivated) return;

        int blockPlace = (int) Math.round(event.getBlockPlaced().getY() - Main.main.getConfig().getDouble("spawn.y")) + 1;

        Player player = event.getPlayer();

        if (blockPlace >= 20) {
            int score = 10;
            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, event.getBlockPlaced().getLocation(), 10);
            RoundSystem.addScore(player, score);
            score -= 2;
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Вы поставили блоков: [" + blockPlace + "/20]");
        }

        if (RoundSystem.roundSeconds <= 0) {
            isActivated = false;
            MapRebuild.loadSchematic("arena");
            RoundSystem.EndRound();
        }
    }
}
