package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class DropItem implements Listener {

    private static Material randomMaterialBlock;
    private static boolean isDropItemActivated = false;

    public static void DropItem() {
        isDropItemActivated = true;
        GameCycle.isAnyBattleEnabled = true;

        int randomMaterial = Utilities.getRandom(0, 150);

        Material[] materials = new Material[5000];

        int i = 0;

        materials = Arrays.copyOfRange(Material.values(), randomMaterial, randomMaterial + 36);

        int randomBlock = Utilities.getRandom(0, 36);
        randomMaterialBlock = materials[randomBlock];

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выкиньте блок", randomMaterialBlock.toString(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE +  "[" + randomMaterialBlock.toString() + "]");

            for (Material block : materials)
                player.getInventory().addItem(new ItemStack(block, 64));
        }
    }

    private static int place = 1;

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
        Player winner = event.getPlayer();

        if (!isDropItemActivated)
            return;

        if (event.getItemDrop().getItemStack().getType().equals(randomMaterialBlock)){
            GameCycle.addScore(winner, place);
            place++;
            winner.getInventory().clear();
        }
        if (place > 3){
            isDropItemActivated = false;
            GameCycle.isAnyBattleEnabled = false;
            place = 1;
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!isDropItemActivated)
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPick(EntityPickupItemEvent event) {
        if (!isDropItemActivated)
            return;
        event.setCancelled(true);
    }
}