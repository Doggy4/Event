package RoundList;

import PluginUtilities.*;
import QueueSystem.Queue;
import RoundSystem.GameRules;
import RoundSystem.RoundSystem;
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

public class RoundPlaceTheBlock implements Listener {
    public static boolean isActivated = false;

    private static Material randomMaterialBlock;
    private static HashMap<Player, Location> playerRoom = new HashMap<Player, Location>();
    private static World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));
    private static ArrayList<Material> materials = Items.materials;
    private static HashMap<Player, Material> univPlayerMaterialHashMap = new HashMap<Player, Material>();

    static {
        materials.removeIf(material -> !material.isBlock());
    }

    public static void placeBlock() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        GameRules.PlaceBlockOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            univPlayerMaterialHashMap.clear();

            player.setGameMode(GameMode.SURVIVAL);

            Location location = (LocationUtulities.spawnLocations.get(Queue.redQueueList.indexOf(player)));
            player.teleport(location.add(0, 2, 0));

            int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
            int randomBlockIndex = Utilities.getRandom(0, 35);

            List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
            randomMaterialBlock = blocks.get(randomBlockIndex);

            univPlayerMaterialHashMap.put(player, randomMaterialBlock);

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

        univPlayerMaterialHashMap.put(player, randomMaterialBlock);

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

        int score = 0;

        if (event.getBlockPlaced().getType().equals(univPlayerMaterialHashMap.get(player))) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            score = 1;
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный блок!");
            score = -1;
        }

        RoundSystem.addScore(player, score);
        placeNext(player);

        event.setCancelled(true);
    }
}