package Game;

import PluginUtilities.BlackList;
import PluginUtilities.Chat;
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

public class DropItem implements Listener {

    private static Material randomMaterialBlock;
    public static boolean isActivated = false;

    public static void DropItem() {
        isActivated = true;
        GameRules.DropItemOff();
        RoundSystem.roundSeconds = 30;

        ArrayList<Material> materials = new ArrayList<Material>();

        for (Material material : Material.values())
            if (!BlackList.isItemBlocked(material.name())) materials.add(material);

        int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utilities.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = allowedMaterials.get(randomBlockIndex);

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", Chat.translate(randomMaterialBlock.name()), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + "[" + Chat.translate(randomMaterialBlock.name()) + "]");

            for (Material block : allowedMaterials)
                player.getInventory().addItem(new ItemStack(block, 1));
        }
    }

    private static void DropNext(Player player) {
        ArrayList<Material> materials = new ArrayList<Material>();

        for (Material material : Material.values())
            if (!BlackList.isItemBlocked(material.name())) materials.add(material);

        int randomMaterialIndex = Utilities.getRandom(0, materials.size() - 37);
        int randomBlockIndex = Utilities.getRandom(0, 35);

        List<Material> allowedMaterials = materials.subList(randomMaterialIndex, randomMaterialIndex + 36);
        randomMaterialBlock = allowedMaterials.get(randomBlockIndex);

        player.getInventory().clear();
        for (Material block : allowedMaterials) player.getInventory().addItem(new ItemStack(block, 1));

        player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", Chat.translate(randomMaterialBlock.name()), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + "[" + Chat.translate(randomMaterialBlock.name()) + "]");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isActivated) return;

        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType().equals(randomMaterialBlock)) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Задание выполнено!");
            RoundSystem.addScore(player, 1);
            DropNext(player);
        } else {
            player.sendMessage(ChatColor.RED + "[EVENT] " + ChatColor.GREEN + "Неверный предмет!");
            RoundSystem.addScore(player, -1);
            DropNext(player);
        }
    }
}