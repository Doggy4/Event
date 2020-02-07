package Game;

import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.Bukkit;
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
    protected static boolean isActivated = false;

    protected static void anvilEscape() {
        // Опционально:
        isActivated = true;
        aRoundSystem.roundSeconds = 15;
        GameRules.EntityDamageOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!isActivated) {
                    this.cancel();
                    endDodgeAnvils();
                }

                for (int i = 0; i < 50; i++) {
                    int randX = Math.round((float) Main.main.getConfig().getDouble("spawn.x")) + Utilities.getRandom(0, 32) - 16;
                    int randZ = Math.round((float) Main.main.getConfig().getDouble("spawn.z")) + Utilities.getRandom(0, 32) - 16;
                    int y = Math.round((float) Main.main.getConfig().getDouble("spawn.y")) + 10;

                    while (Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(randX, y, randZ).getType() != Material.AIR) {
                        randX = Math.round((float) Main.main.getConfig().getDouble("spawn.x")) + Utilities.getRandom(0, 32) - 16;
                        randZ = Math.round((float) Main.main.getConfig().getDouble("spawn.z")) + Utilities.getRandom(0, 32) - 16;
                        y = Math.round((float) Main.main.getConfig().getDouble("spawn.y")) + 10;
                    }
                    Bukkit.getWorld(Main.main.getConfig().getString("spawn.world")).getBlockAt(randX, y, randZ).setType(Material.ANVIL);
                }
            }
        }.runTaskTimer(Main.main, 10, 10);
    }

    private static void endDodgeAnvils() {
        for (Player roundPlayer : Queue.redQueueList)
            if (roundPlayer.getGameMode() != GameMode.ADVENTURE)
                aRoundSystem.playerWin(roundPlayer);
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

        aRoundSystem.playerLose(player);
    }
}
