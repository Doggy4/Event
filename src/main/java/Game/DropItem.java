package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropItem implements Listener {

    private static Material randomMaterialBlock;
    private static boolean isActivated = false;

    public static void DropItem() {
        isActivated = true;
        GameRules.DropItemOff();
        RoundSystem.roundSeconds = 30;

        ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew = materials.subList(randomMaterial, randomMaterial + 36);

        int randomBlock = Utilities.getRandom(0, 35);
        randomMaterialBlock = materialsNew.get(randomBlock);

        while (randomMaterialBlock.name().contains("STEM") || randomMaterialBlock.name().contains("AIR") || randomMaterialBlock.name().contains("BAMBOO") || randomMaterialBlock.name().contains("STAND") || randomMaterialBlock.name().contains("COMMAND") || randomMaterialBlock.name().contains("BARRIER") || randomMaterialBlock.name().contains("LECTERN") || randomMaterialBlock.name().contains("BEETROOTS") || randomMaterialBlock.name().contains("CARROTS") || randomMaterialBlock.name().contains("SEEDS") || randomMaterialBlock.name().contains("POTATO") || randomMaterialBlock.name().contains("BLUET")) {
            randomBlock = Utilities.getRandom(0, 35);
            randomMaterialBlock = materialsNew.get(randomBlock);
        }

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", randomMaterialBlock.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + "[" + randomMaterialBlock.name() + "]");

            for (Material block : materialsNew)
                player.getInventory().addItem(new ItemStack(block, 1));
        }
    }

    private static void DropNext(Player player) {
        ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew = materials.subList(randomMaterial, randomMaterial + 36);

        int randomBlock = Utilities.getRandom(0, 35);
        randomMaterialBlock = materialsNew.get(randomBlock);

        while (randomMaterialBlock.name().contains("STEM") || randomMaterialBlock.name().contains("AIR") || randomMaterialBlock.name().contains("BAMBOO") || randomMaterialBlock.name().contains("STAND") || randomMaterialBlock.name().contains("COMMAND") || randomMaterialBlock.name().contains("BARRIER") || randomMaterialBlock.name().contains("LECTERN") || randomMaterialBlock.name().contains("BEETROOTS") || randomMaterialBlock.name().contains("CARROTS") || randomMaterialBlock.name().contains("SEEDS") || randomMaterialBlock.name().contains("POTATO") || randomMaterialBlock.name().contains("BLUET")) {
            randomBlock = Utilities.getRandom(0, 35);
            randomMaterialBlock = materialsNew.get(randomBlock);
        }
        player.getInventory().clear();
        player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", randomMaterialBlock.name(), 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + "[" + randomMaterialBlock.name() + "]");

        for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

    }

    public static void disableEvents() {
        isActivated = false;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isActivated) return;

        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType().equals(randomMaterialBlock)) {
            player.sendMessage(ChatColor.RED + "Правильно");
            RoundSystem.addScore(player, 1);
            DropNext(player);
        } else
            player.sendMessage(ChatColor.RED + "Неправильно");
            RoundSystem.addScore(player, -1);
            DropNext(player);
    }
}