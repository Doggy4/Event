package RoundList;

import Particles.Particles;
import PluginUtils.Chat;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.GameRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class RoundTheRightCombination implements Listener {
    public static boolean isActivated = false;

    private static Material[] blockToBePut = {Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL};
    private static Material[] blockToPlaceOn = {Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL};

    private static HashMap<Player, Material> block1 = new HashMap<Player, Material>();
    private static HashMap<Player, Material> block2 = new HashMap<Player, Material>();

    private static int y = Math.round((float) Main.main.getConfig().getDouble("spawn.y"));
    private static World world = Bukkit.getWorld(Main.main.getConfig().getString("spawn.world"));

    private static int n;
    private static int n2;

    public static void rightCombination() {
        RoundSystem.roundSeconds = 30;
        isActivated = true;
        GameRules.PlaceBlockOff();
        MapRebuild.loadSchematic("arena");

        for (Player player : Queue.redQueueList) {
            n = (int) Math.floor(Math.random() * blockToBePut.length);
            n2 = (int) Math.floor(Math.random() * blockToPlaceOn.length);

            Location blockLoc = player.getLocation().add(0, 0, 0);
            blockLoc.getBlock().setType(blockToPlaceOn[n2]);

            block1.put(player, blockToBePut[n]);
            block2.put(player, blockToPlaceOn[n2]);

            for (Material material : blockToBePut) player.getInventory().addItem(new ItemStack(material));

            player.setGameMode(GameMode.SURVIVAL);
            gameRulesAnnouncement(player);
        }
    }

    private static void rightCombinationNext(Player player, Location blockLoc) {
        player.getInventory().clear();

        n = (int) Math.floor(Math.random() * blockToBePut.length);
        n2 = (int) Math.floor(Math.random() * blockToPlaceOn.length);

        int locProp = Utils.getRandom(-1, 1);

        Location newBlockLoc = blockLoc.add(locProp, 0, locProp);
        newBlockLoc.getBlock().setType(blockToPlaceOn[n2]);

        block1.put(player, blockToBePut[n]);
        block2.put(player, blockToPlaceOn[n2]);

        for (Material material : blockToBePut) player.getInventory().addItem(new ItemStack(material));

        gameRulesAnnouncement(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Ставьте шерсть в", "правильном порядке", 20, 10, 20);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Ставьте шерсть в нужном порядке!");
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Поставьте " + Chat.colorsFromID.get(blockToBePut[n].name()) + Chat.translate(blockToBePut[n].name()) + ChatColor.WHITE + " на " + Chat.colorsFromID.get(blockToPlaceOn[n2].name()) + Chat.translate(blockToPlaceOn[n2].name()));
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getBlockPlaced().getType().equals(block1.get(player)) && event.getBlockAgainst().getType().equals(block2.get(player))) {
            RoundSystem.addScore(player, 1);
            Location blockLoc = event.getBlockPlaced().getLocation();
            Particles.createBlockSplash(blockLoc, Particle.FIREWORKS_SPARK);
            player.playSound(blockLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            rightCombinationNext(player, blockLoc);
        } else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный порядок!");
            RoundSystem.addScore(player, -1);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Поставьте " + Chat.colorsFromID.get(blockToBePut[n].name()) + Chat.translate(blockToBePut[n].name()) + ChatColor.WHITE + " на " + Chat.colorsFromID.get(blockToPlaceOn[n2].name()) + Chat.translate(blockToPlaceOn[n2].name()));
            Location blockLoc = event.getBlockPlaced().getLocation();

            Particles.createBlockSplash(blockLoc, Particle.FALLING_LAVA);
            player.playSound(blockLoc, Sound.BLOCK_WOOL_BREAK, 1, 1);

            Particles.createBlockSplash(blockLoc, Particle.CRIT);
            player.playSound(blockLoc, Sound.BLOCK_WOOL_BREAK, 1, 1);
        }
    }
}
