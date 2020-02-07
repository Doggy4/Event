package Game;

import PluginUtilities.Chat;
import PluginUtilities.Items;
import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RoundDropTheItem implements Listener {
    protected static boolean isActivated = false;

    private static Material randomMaterialBlock;
    private static ArrayList<Material> materials = Items.materials;

    protected static void DropTheItem() {
        // Опциально:
        isActivated = true;
        aRoundSystem.roundSeconds = 30;
        GameRules.DropItemOff();
        MapRebuild.loadSchematic("arena");

        int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utilities.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = allowedMaterials.get(randomBlockIndex);

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            for (Material block : allowedMaterials)
                player.getInventory().addItem(new ItemStack(block, 1));
        }
    }

    private static void dropNext(Player player) {
        int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utilities.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = allowedMaterials.get(randomBlockIndex);

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

        if (event.getItemDrop().getItemStack().getType().equals(randomMaterialBlock)) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Задание выполнено!");
            aRoundSystem.addScore(player, 1);
            dropNext(player);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Неверный предмет!");
            aRoundSystem.addScore(player, -1);
            dropNext(player);
        }
    }
}