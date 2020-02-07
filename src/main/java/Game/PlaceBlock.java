package Game;

import PluginUtilities.*;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceBlock implements Listener {
    protected static boolean isActivated = false;

    private static Material randomMaterialBlock;
    private static HashMap<Player, Location> playerRoom = new HashMap<Player, Location>();
    private static World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));
    private static ArrayList<Material> materials = Items.materials;

    public static void placeBlock() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        GameRules.PlaceBlockOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            player.setGameMode(GameMode.SURVIVAL);

            Location location = (LocationUtulities.spawnLocations.get(Queue.redQueueList.indexOf(player)));
            player.teleport(location.add(0, 2, 0));

            int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
            int randomBlockIndex = Utilities.getRandom(0, 35);

            List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
            randomMaterialBlock = blocks.get(randomBlockIndex);

            for (Material block : blocks) player.getInventory().addItem(new ItemStack(block, 1));

            gameRulesAnnouncement(player);

            LocationUtulities.spawnLocations.get(Queue.redQueueList.indexOf(player)).getWorld().getBlockAt(location).setType(randomMaterialBlock);
            playerRoom.put(player, location);
        }
    }

    private static void placeNext(Player player) {
        Location location = playerRoom.get(player);

        int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utilities.getRandom(0, 35);

        List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = blocks.get(randomBlockIndex);

        player.getInventory().clear();
        for (Material block : blocks) player.getInventory().addItem(new ItemStack(block, 1));

        world.getBlockAt(location).setType(randomMaterialBlock);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Поставьте блок", Chat.translate(randomMaterialBlock.name()), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Поставьте блок " + Chat.translate(randomMaterialBlock.name()));
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;


        if (event.getBlockPlaced().getType().equals(randomMaterialBlock)) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            RoundSystem.addScore(player, 1);
            placeNext(player);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный блок!");
            RoundSystem.addScore(player, -1);
            placeNext(player);
        }

        event.setCancelled(true);
    }
}