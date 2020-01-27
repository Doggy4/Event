package Game;

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
import java.util.Arrays;
import java.util.List;

public class PlaceBlock implements Listener {

    private static Material randomMaterialBlock;
    private static boolean isActivated = false;

    private static ArrayList<Location> GetSpawnPoints() {
        FileConfiguration config = Main.main.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        Location location = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));
        ArrayList<Location> locations = new ArrayList<Location>();

        locations.add(location.add(10,0,0));
        locations.add(location.add(-10,0,0));
        locations.add(location.add(3,0,-10));
        locations.add(location.add(-3,0,-10));
        locations.add(location.add(3,0,10));
        locations.add(location.add(-3,0,10));
        locations.add(location.add(9,0,-6));
        locations.add(location.add(-9,0,-6));
        locations.add(location.add(9,0,6));
        locations.add(location.add(-9,0,6));
        return locations;
    }
///////// ПЕРЕДЕЛАТЬ НАХУЙ
    public static void start() {
        isActivated = true;
        BaseClass.PlaceBlockOff();
        ArrayList<Location> locations = GetSpawnPoints();

        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);

            player.getInventory().clear();

            for (Location roomLoc : locations) {
                player.teleport(roomLoc);
            }

            ArrayList<Material> materials = new ArrayList<Material>();

            for (Material block : Material.values())
                if (block.isBlock() && block.isSolid() && !block.isAir() && !block.isInteractable()) {
                    materials.add(block);
                }

            int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
            List<Material> materialsNew =  materials.subList(randomMaterial, randomMaterial + 36);

            int randomBlock = Utilities.getRandom(0, 36);

            randomMaterialBlock = materialsNew.get(randomBlock);

            for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));



            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.sendTitle(ChatColor.GREEN + "Поставьте блок", randomMaterialBlock.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Поставьте блок " + ChatColor.LIGHT_PURPLE + "[" + randomMaterialBlock.name() + "]");
            player.setGameMode(GameMode.SURVIVAL);

            FileConfiguration config = Main.main.getConfig();
            World world = Bukkit.getWorld(config.getString("spawn.world"));
            Location location = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y") + 2, config.getDouble("spawn.z"));
            world.getBlockAt(location).setType(randomMaterialBlock);
        }
    }

    private static void PlaceNext(Player player) {
        ArrayList<Material> materials = new ArrayList<Material>();

        for (Material block : Material.values())
            if (block.isBlock() && block.isSolid() && !block.isAir() && !block.isInteractable()) {
                materials.add(block);
            }

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew =  materials.subList(randomMaterial, randomMaterial + 36);

        int randomBlock = Utilities.getRandom(0, 36);

        randomMaterialBlock = materialsNew.get(randomBlock);

        for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

        player.getInventory().clear();

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
        player.sendTitle(ChatColor.GREEN + "Поставьте блок", randomMaterialBlock.name(), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Поставьте блок " + ChatColor.LIGHT_PURPLE + "[" + randomMaterialBlock.name() + "]");
        player.setGameMode(GameMode.SURVIVAL);

        FileConfiguration config = Main.main.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        Location location = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y") + 2, config.getDouble("spawn.z"));
        world.getBlockAt(location).setType(randomMaterialBlock);

    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!isActivated) return;

        if (event.getBlockPlaced().getType().toString().equals(randomMaterialBlock.toString())) {
            RoundSystem.addScore(player, 1);
            PlaceNext(player);
        } else {
            RoundSystem.addScore(player, -1);
        }

        if (RoundSystem.roundSeconds >= 0) {
            isActivated = false;
            RoundSystem.EndRound();
        }
        event.setCancelled(true);
    }
}