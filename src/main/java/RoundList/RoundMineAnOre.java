package RoundList;

import PluginUtils.Chat;
import PluginUtils.Items;
import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RoundMineAnOre implements Listener {
    public static boolean isActivated = false;

    private static List<Material> oreList = Arrays.asList(Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.GOLD_ORE, Material.IRON_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE);
    private static HashMap<Player, Material> playerMaterialHashMap = new HashMap<Player, Material>();

    private static BukkitRunnable runnable;

    public static void mineAnOre() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 40;
        RoundRules.BreakBlockOff();
        MapRebuild.loadSchematic("mine-arena");

        playerMaterialHashMap.clear();

        for (Player player : Queue.redQueueList) {
            player.getInventory().addItem(Items.pickAxe);
            player.setGameMode(GameMode.SURVIVAL);
            nextOre(player);
        }

        for (Block block : LocationUtils.getBlocksFromTwoPoints(LocationUtils.arenaLeft.add(0, -5, 0), LocationUtils.arenaRight))
            if (block.getType().equals(Material.STONE))
                if (Utils.getRandom(0, 1) == 1)
                    block.setType(oreList.get(Utils.getRandom(0, oreList.size() - 1)));
    }

    private static void nextOre(Player player) {
        playerMaterialHashMap.put(player, oreList.get(Utils.getRandom(0, oreList.size() - 1)));
        gameRulesAnnouncement(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Выкопайте ", Chat.translate(playerMaterialHashMap.get(player).name()), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкопайте [" + Chat.translate(playerMaterialHashMap.get(player).name()) + "]");
    }

    @EventHandler
    public void onFallingBlockLand(BlockBreakEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!Queue.redQueueList.contains(player)) return;
        if (!event.getBlock().getType().equals(playerMaterialHashMap.get(player))) return;
        RoundSystem.addScore(player, 5);
        nextOre(player);
    }
}