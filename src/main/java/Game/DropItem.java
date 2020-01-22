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
        isDropItemActivated = GameCycle.isAnyBattleEnabled;

        ArrayList<Material> materials = new ArrayList<Material>();
        for (Material block : Material.values())
                materials.add(block);

        int randomMaterial = Utilities.getRandom(0, materials.size() - 1);
        materials.subList(randomMaterial, randomMaterial+36);

        int randomBlock = Utilities.getRandom(0, 36);
        randomMaterialBlock = materials.get(randomBlock);

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", randomMaterialBlock.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + randomMaterialBlock.name());

            for (Material block : materials)
                player.getInventory().addItem(new ItemStack(block, 64));
        }
    }

    private static int place = 1;

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isDropItemActivated)
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType().name().equals(randomMaterialBlock.name())){
            GameCycle.addScore(player, place);
            place++;

        } else
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы проиграли!");
        player.getInventory().clear();

        if (place > 3){
            isDropItemActivated = false;
            GameCycle.isAnyBattleEnabled = isDropItemActivated;
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