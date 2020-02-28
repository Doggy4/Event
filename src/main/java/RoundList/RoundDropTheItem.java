package RoundList;

import PluginUtils.Chat;
import PluginUtils.ItemUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundSystem.RoundRules;
import RoundSystem.RoundSystem;
import RoundUtils.MapRebuild;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoundDropTheItem implements Listener {
    public static boolean isActivated = false;

    private static HashMap<Player, Material> univPlayerMaterialHashMap = new HashMap<Player, Material>();

    private static Material randomMaterialBlock;
    private static ArrayList<Material> materials = ItemUtils.materials;

    public static void startRound() {
        // Опциально:
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        RoundRules.DropItemOff();
        MapRebuild.loadSchematic("arena");

        univPlayerMaterialHashMap.clear();

        int randomMaterialIndex = Utils.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utils.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = allowedMaterials.get(randomBlockIndex);


        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            for (Material block : allowedMaterials) {
                univPlayerMaterialHashMap.put(player, randomMaterialBlock);
                player.getInventory().addItem(new ItemStack(block, 1));
            }
        }
    }

    private static void dropNext(Player player) {
        int randomMaterialIndex = Utils.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utils.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = allowedMaterials.get(randomBlockIndex);

        univPlayerMaterialHashMap.put(player, randomMaterialBlock);

        player.getInventory().clear();
        gameRulesAnnouncement(player);
        for (Material block : allowedMaterials) player.getInventory().addItem(new ItemStack(block, 1));
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", Chat.translate(randomMaterialBlock.name()), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + "[" + Chat.translate(randomMaterialBlock.name()) + "]");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;

        if (event.getItemDrop().getItemStack().getType().equals(univPlayerMaterialHashMap.get(player))) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            RoundSystem.addScore(player, 1);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный предмет!");
            RoundSystem.addScore(player, -1);
        }
        dropNext(player);
    }
}