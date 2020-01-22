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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceBlock implements Listener {

    private static Material randomMaterialBlock;
    private static boolean isPlaceBlockEventActivated = false;

    public static void placeBlock() {
        isPlaceBlockEventActivated = GameCycle.isAnyBattleEnabled;

        int randomMaterial = Utilities.getRandom(0, 150);

        Material[] materials = new Material[5000];

        int i = 0;
        for (Material block : Material.values())
            if (block.isBlock()) {
                materials[i] = block;
                i++;
            }

        materials = Arrays.copyOfRange(materials, randomMaterial, randomMaterial + 36);

        int randomBlock = Utilities.getRandom(0, 36);
        randomMaterialBlock = materials[randomBlock];

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Поставьте блок", randomMaterialBlock.toString(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Поставьте блок " + ChatColor.LIGHT_PURPLE +  "[" + randomMaterialBlock.toString() + "]");
            player.setGameMode(GameMode.SURVIVAL);

            for (Material block : materials)
                player.getInventory().addItem(new ItemStack(block, 64));
        }

        FileConfiguration config = Main.main.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        Location location = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y") + 2, config.getDouble("spawn.z"));
        world.getBlockAt(location).setType(randomMaterialBlock);
    }

    private static int place = 1;

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player winner = event.getPlayer();
        if (!isPlaceBlockEventActivated)
            return;
        if (event.getBlockPlaced().getType().toString().equals(randomMaterialBlock.toString())){
            GameCycle.addScore(winner, place);
            place++;
            winner.setGameMode(GameMode.ADVENTURE);
            winner.getInventory().clear();
        }
        if (place > 3){
            isPlaceBlockEventActivated = false;
            GameCycle.isAnyBattleEnabled = isPlaceBlockEventActivated;
            place = 1;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!isPlaceBlockEventActivated)
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        if (!isPlaceBlockEventActivated)
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isPlaceBlockEventActivated)
            return;
        event.setCancelled(true);
    }
}