package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

// Потом проапаю
public class DodgeAnvils implements Listener {
    public static boolean isActivated = false;

    public static void DodgeAnvils() {
        isActivated = true;
        RoundSystem.roundSeconds = 15;
        GameRules.EntityDamageOff();

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.setGameMode(GameMode.ADVENTURE);

            player.sendTitle(ChatColor.GREEN + "Спаситесь от", "падающих наковален", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Спаситесь от падающих наковален!");
        }

        new BukkitRunnable() {
            int counter;

            @Override
            public void run() {
                if (!isActivated) {
                    endDodgeAnvils();
                    this.cancel();
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
                counter++;
            }
        }.runTaskTimer(Main.main, 10, 10);
    }

    private static void endDodgeAnvils() {
        for (Player roundPlayer : Queue.redQueueList) {
            if (roundPlayer.getGameMode() == GameMode.ADVENTURE) {
                for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + player.getName() + " победил!");

                roundPlayer.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы победили!", 20, 20, 20);
                roundPlayer.playSound(roundPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);

                roundPlayer.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Вы получили +" + 2 + " очков(-а)!");
                GameCycle.gameStats.put(roundPlayer, 2 + GameCycle.gameStats.get(roundPlayer));

                RoundSystem.playerReset(roundPlayer);
            }
        }
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        if (!isActivated || !(event.getEntity() instanceof FallingBlock)) return;
        Block block = event.getBlock();

        int x = Math.round((float) block.getLocation().getX());
        int y = Math.round((float) block.getLocation().getY()) - 1;
        int z = Math.round((float) block.getLocation().getZ());

        if (event.getEntity().getLocation().getWorld().getBlockAt(x, y, z).getType() != Material.AIR) event.setCancelled(true);
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent event) {
        if (!isActivated || !(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!Queue.redQueueList.contains(player)) return;

        player.setGameMode(GameMode.SPECTATOR);
        RoundSystem.playerLose(player);
    }
}
