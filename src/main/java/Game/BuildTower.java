package Game;

import PluginUtilities.MapRebuilding;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BuildTower implements Listener {
    // Добавить условия компановки блоков, например поставьте красную шерсть рядом с синей
    private static boolean isActivated = false;
    private static Material[] blockWhatNeedToPlace = {Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL};
    private static Material[] blockOnWhatPlace = {Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL};

    private static HashMap<Player, Material> block1 = new HashMap<Player, Material>();
    private static HashMap<Player, Material> block2 = new HashMap<Player, Material>();

    private static int y = Math.round((float) Main.main.getConfig().getDouble("spawn.y"));
    private static World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

    private static int n;
    private static int n2;

    public static void BuildTower() {
        RoundSystem.roundSeconds = 30;
        isActivated = true;
        GameRules.PlaceBlockOff();

        for (Player player : Queue.redQueueList) {
            int randX = Math.round((float) Main.main.getConfig().getDouble("spawn.x")) + Utilities.getRandom(0, 32) - 16;
            int randZ = Math.round((float) Main.main.getConfig().getDouble("spawn.z")) + Utilities.getRandom(0, 32) - 16;

            Location tpLoc = new Location(world, randX, y, randZ);
            player.teleport(tpLoc);
            player.getInventory().clear();

            n = (int) Math.floor(Math.random() * blockWhatNeedToPlace.length);
            n2 = (int) Math.floor(Math.random() * blockOnWhatPlace.length);

            Location blockLoc = player.getLocation().add(0, 0, 0);
            blockLoc.getBlock().setType(blockOnWhatPlace[n2]);
            player.getInventory().addItem(new ItemStack(blockWhatNeedToPlace[n], 1));

            block1.put(player, blockWhatNeedToPlace[n]);
            block2.put(player, blockOnWhatPlace[n2]);

            for (int i = 0; i < 7; i++) player.getInventory().addItem(new ItemStack(blockWhatNeedToPlace[i]));

            player.setGameMode(GameMode.SURVIVAL);

            player.sendTitle(ChatColor.GREEN + "Постройте башню из", "20 блоков", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Ставьте шерсть в нужном порядке");
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Поставьте " + blockWhatNeedToPlace[n].toString() + " На " + blockOnWhatPlace[n2].toString());
        }
    }


    private static void nextBlockToPlace(Player player, Location blockLoc) {
        player.getInventory().clear();

        n = (int) Math.floor(Math.random() * blockWhatNeedToPlace.length);
        n2 = (int) Math.floor(Math.random() * blockOnWhatPlace.length);

        int locProp = Utilities.getRandom(-1,1);

        Location newBlockLoc = blockLoc.add(locProp, 0, locProp);
        newBlockLoc.getBlock().setType(blockOnWhatPlace[n2]);
        player.getInventory().addItem(new ItemStack(blockWhatNeedToPlace[n], 1));

        block1.put(player, blockWhatNeedToPlace[n]);
        block2.put(player, blockOnWhatPlace[n2]);

        for (int i = 0; i < 7; i++) player.getInventory().addItem(new ItemStack(blockWhatNeedToPlace[i]));

        player.sendTitle(ChatColor.GREEN + "Постройте башню из", "20 блоков", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Ставьте шерсть в нужном порядке");
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Поставьте " + blockWhatNeedToPlace[n].toString() + " На " + blockOnWhatPlace[n2].toString());

    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (!isActivated) return;

        Player player = e.getPlayer();

        if (e.getBlockPlaced().getType().equals(block1.get(player)) && e.getBlockAgainst().getType().equals(block2.get(player))) {
            RoundSystem.addScore(player, 1);
            Location blockLoc = e.getBlockPlaced().getLocation();
            nextBlockToPlace(player, blockLoc);
        } else {
            e.setCancelled(true);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Поставьте " + blockWhatNeedToPlace[n].toString() + " На " + blockOnWhatPlace[n2].toString());
        }

        if (!(RoundSystem.isRoundTimerEnabled)) {
            isActivated = RoundSystem.isRoundTimerEnabled;
            MapRebuilding.loadSchematic("arena");
        }
    }
}
