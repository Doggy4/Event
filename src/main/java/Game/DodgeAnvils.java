package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DodgeAnvils implements Listener {
    private static boolean isActivated = false;

    public static void DodgeAnvils() {
        isActivated = aGameCycle.isAnyBattleEnabled;
        BaseClass.EntityDamageOff();

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.setGameMode(GameMode.ADVENTURE);

            player.sendTitle(ChatColor.GREEN + "Спаситесь от", "падающих наковален", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Спаситесь от падающих наковален!");
        }

        new BukkitRunnable() {
            int counter;

            @Override
            public void run() {
                if (counter >= 20) {
                    this.cancel();
                    endDodgeAnvils();
                    aGameCycle.isAnyBattleEnabled = false;
                    isActivated = false;

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
        }.runTaskTimer(Main.main, 20, 20);


    }

    private static void endDodgeAnvils() {
        for (String playerName : Queue.redQueueList) {
            Player winner = Bukkit.getPlayer(playerName);
            if (winner.getGameMode() == GameMode.ADVENTURE) {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + winner.getName() + " победил!");

                winner.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы победили!", 20, 20, 20);
                winner.playSound(winner.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);

                winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Вы получили +" + 2 + " очков(-а)!");
                aGameCycle.gameStats.put(winner.getName(), 2 + aGameCycle.gameStats.get(winner.getName()));

                aGameCycle.reset(winner);
            }
        }
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        Block block = event.getBlock();

        int x = Math.round((float) block.getLocation().getX());
        int y = Math.round((float) block.getLocation().getY()) - 1;
        int z = Math.round((float) block.getLocation().getZ());

        if (event.getEntity() instanceof FallingBlock) {
            if (event.getEntity().getLocation().getWorld().getBlockAt(x, y, z).getType() != Material.AIR) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent event) {
        if (!isActivated)
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!Queue.redQueueList.contains(player.getName()))
            return;


        player.setGameMode(GameMode.SPECTATOR);
        aGameCycle.playerLose(player);

    }


}
