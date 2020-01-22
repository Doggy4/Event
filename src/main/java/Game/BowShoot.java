package Game;

import QueueSystem.Queue;
import Utils.ArrowHitBlockEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.BlockIterator;


import java.util.Vector;

import static PluginUtilities.Items.BowEventArrows;
import static PluginUtilities.Items.BowEventBow;

public class BowShoot implements Listener {
    private static boolean isShootTargetActivated = false;

    private static Material[] targets = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK, Material.REDSTONE_BLOCK};
    private static int n = (int) Math.floor(Math.random() * targets.length);

    public static void BowShoot() {
        isShootTargetActivated = GameCycle.isAnyBattleEnabled;
        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выстрелите из лука в блок ", targets[n].toString(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Попадите в блок " + ChatColor.LIGHT_PURPLE + "[" + targets[n].toString() + "]");
            player.getInventory().addItem(BowEventBow);
            player.getInventory().addItem(BowEventArrows);
        }
    }

    private static int place = 1;

    @EventHandler
    public void OnShoot(EntityShootBowEvent e) {
        if (!isShootTargetActivated) return;
        Player player = (Player) e.getEntity();
        Arrow arrow = (Arrow) e.getProjectile();
        World world = arrow.getWorld();
        BlockIterator iterator;
        iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
        Block hitBlock = null;
        while (iterator.hasNext()) {
            hitBlock = iterator.next();
            if(hitBlock.getBlockData().getMaterial().isAir()) break;

            if (hitBlock.getBlockData().getMaterial().equals(targets[n])) {
                GameCycle.addScore(player, place);
                place++;
                player.getInventory().clear();
                if (arrow != null) arrow.remove();
            }
            if (place > 3) {
                isShootTargetActivated = false;
                GameCycle.isAnyBattleEnabled = isShootTargetActivated;
                place = 1;
            }
        }
    }
}


