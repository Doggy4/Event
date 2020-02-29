package RoundList;

import PluginUtils.Chat;
import PluginUtils.ItemUtils;
import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private static ArrayList<Material> materials = ItemUtils.materials;
    private static HashMap<Player, Material> univPlayerMaterialHashMap = new HashMap<Player, Material>();

    static {
        materials.removeIf(material -> !material.isBlock());
    }

    public static void startRound() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        RoundRules.PlaceBlockOff();
        MapRebuild.loadSchematic("arena");
        univPlayerMaterialHashMap.clear();

        for (Player player : Queue.redQueueList) {
            player.setGameMode(GameMode.SURVIVAL);

            Location location = (LocationUtils.spawnLocations.get(Queue.redQueueList.indexOf(player)));
            player.teleport(LocationUtils.addLocation(location, 0, 2, 0));

            int randomMaterialIndex = Utils.getRandom(0, materials.size() - 37);
            int randomBlockIndex = Utils.getRandom(0, 35);

            List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
            randomMaterialBlock = blocks.get(randomBlockIndex);

            for (Material block : blocks) player.getInventory().addItem(new ItemStack(block, 1));

            LocationUtils.world.getBlockAt(location).setType(randomMaterialBlock);
            playerRoom.put(player, location);

            univPlayerMaterialHashMap.put(player, randomMaterialBlock);
            gameRulesAnnouncement(player);
        }
    }

    private static void placeNext(Player player) {
        Location location = playerRoom.get(player);

        int randomMaterialIndex = Utils.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utils.getRandom(0, 35);

        List<Material> blocks = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = blocks.get(randomBlockIndex);

        univPlayerMaterialHashMap.put(player, randomMaterialBlock);

        player.getInventory().clear();
        for (Material block : blocks) player.getInventory().addItem(new ItemStack(block, 1));

        gameRulesAnnouncement(player);

        LocationUtils.world.getBlockAt(location).setType(randomMaterialBlock);
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
        event.setCancelled(true);
        event.setBuild(false);
        int score;
        if (event.getBlockPlaced().getType().equals(univPlayerMaterialHashMap.get(player))) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            score = 1;
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный блок!");
            score = -1;
        }

        RoundSystem.addScore(player, score);
        placeNext(player);
    }
}