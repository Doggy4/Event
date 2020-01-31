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
import java.util.HashMap;
import java.util.List;
// Зафиксить появление воды и другой жидкости
public class PlaceBlock implements Listener {

    private static Material randomMaterialBlock;
    private static boolean isActivated = false;
    private static HashMap<Player, Location> playerRoom = new HashMap<Player, Location>();

    public static void PlaceBlock() {
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        GameRules.PlaceBlockOff();

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

        int l = 0;

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();
            player.teleport(locations.get(l).add(0,2,0));
            l++;

            ArrayList<Material> materials = new ArrayList<Material>();

            for (Material block : Material.values())
                if (block.isBlock() && block.isSolid() && !block.isAir() && !block.isInteractable()) {
                    materials.add(block);
                }

            int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
            List<Material> materialsNew =  materials.subList(randomMaterial, randomMaterial + 36);

            int randomBlock = Utilities.getRandom(0, 35);

            randomMaterialBlock = materialsNew.get(randomBlock);

            player.getInventory().clear();
            for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            player.sendTitle(ChatColor.GREEN + "Поставьте блок", randomMaterialBlock.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GREEN + "Поставьте блок " + randomMaterialBlock.name());
            player.setGameMode(GameMode.SURVIVAL);

            location.getWorld().getBlockAt(locations.get(l)).setType(randomMaterialBlock);
            playerRoom.put(player, locations.get(l));
        }
    }

    private static void PlaceNext(Player player) {
        Location location = playerRoom.get(player);
        ArrayList<Material> materials = new ArrayList<Material>();

        for (Material block : Material.values())
            if (block.isBlock() && block.isSolid() && !block.isAir() && !block.isInteractable()) {
                materials.add(block);
            }

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew =  materials.subList(randomMaterial, randomMaterial + 36);

        int randomBlock = Utilities.getRandom(0, 35);

        randomMaterialBlock = materialsNew.get(randomBlock);

        player.getInventory().clear();
        for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
        player.sendTitle(ChatColor.GREEN + "Поставьте блок", randomMaterialBlock.name(), 40, 40, 40);
        player.sendMessage(ChatColor.GREEN + "Поставьте блок " + randomMaterialBlock.name());

        FileConfiguration config = Main.main.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        world.getBlockAt(location).setType(randomMaterialBlock);
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!isActivated) return;

        if (event.getBlockPlaced().getType().equals(randomMaterialBlock)) {
            RoundSystem.addScore(player, 1);
            PlaceNext(player);
        } else {
            RoundSystem.addScore(player, -1);
            PlaceNext(player);
        }

        if (!(RoundSystem.isRoundTimerEnabled)) {
            isActivated = RoundSystem.isRoundTimerEnabled;
        }
        event.setCancelled(true);
    }
}