package Game;

import PluginUtilities.BlackList;
import PluginUtilities.Chat;
import PluginUtilities.LocationUtulities;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
// Зафиксить появление воды и другой жидкости
public class PlaceBlock implements Listener {

    private static Material randomMaterialBlock;
    public static boolean isActivated = false;
    private static HashMap<Player, Location> playerRoom = new HashMap<Player, Location>();

    public static void PlaceBlock() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        GameRules.PlaceBlockOff();

        FileConfiguration config = Main.main.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();
            Location location = (LocationUtulities.spawnLocations.get(Queue.redQueueList.indexOf(player)));
            player.teleport(location.add(0,2,0));

            ArrayList<Material> materials = new ArrayList<Material>();

            for (Material material : Material.values())
                if (!BlackList.isItemBlocked(material.name()) && material.isBlock()) materials.add(material);

            int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
            int randomBlockIndex = Utilities.getRandom(0, 35);

            List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
            randomMaterialBlock = blocks.get(randomBlockIndex);

            player.getInventory().clear();
            for (Material block : blocks) player.getInventory().addItem(new ItemStack(block, 1));

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.sendTitle(ChatColor.GREEN + "Поставьте блок", Chat.translate(randomMaterialBlock.name()), 40, 40, 40);
            player.sendMessage(ChatColor.GREEN + "Поставьте блок " + Chat.translate(randomMaterialBlock.name()));
            player.setGameMode(GameMode.SURVIVAL);

            LocationUtulities.spawnLocations.get(Queue.redQueueList.indexOf(player)).getWorld().getBlockAt(location).setType(randomMaterialBlock);
            playerRoom.put(player, location);
        }
    }

    private static void PlaceNext(Player player) {
        Location location = playerRoom.get(player);
        ArrayList<Material> materials = new ArrayList<Material>();

        for (Material material : Material.values())
            if (!BlackList.isItemBlocked(material.name()) && material.isBlock()) materials.add(material);

        int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utilities.getRandom(0, 35);

        List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = blocks.get(randomBlockIndex);

        player.getInventory().clear();
        for (Material block : blocks) player.getInventory().addItem(new ItemStack(block, 1));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
        player.sendTitle(ChatColor.GREEN + "Поставьте блок", Chat.translate(randomMaterialBlock.name()), 40, 40, 40);
        player.sendMessage(ChatColor.GREEN + "Поставьте блок " + Chat.translate(randomMaterialBlock.name()));

        FileConfiguration config = Main.main.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        world.getBlockAt(location).setType(randomMaterialBlock);
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!isActivated) return;

        if (event.getBlockPlaced().getType().equals(randomMaterialBlock)) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            RoundSystem.addScore(player, 1);
            PlaceNext(player);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный блок!");
            RoundSystem.addScore(player, -1);
            PlaceNext(player);
        }

        event.setCancelled(true);
    }
}