package Game;

import PluginUtilities.BlackList;
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
import java.util.Arrays;
import java.util.List;

public class DropItem implements Listener {

    private static Material randomMaterialBlock;
    private static boolean isActivated = false;

    // Тошнит от этого кода, сделай его красивым
    public static void DropItem() {
        // Активация раунда
        isActivated = true;

        // Разрешение дропа предметов
        GameRules.DropItemOff();

        // Установка времени на раунд
        RoundSystem.roundSeconds = 30;

        // Список материалов с учетом черного списка в BlackList.java
        ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));
        materials.removeIf(material -> (BlackList.isItemBlocked(material.name())));

        // Рандомный range
        int randomMaterialListIndex = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialList = materials.subList(randomMaterialListIndex, randomMaterialListIndex + 36);

        int randomMaterialIndex = Utilities.getRandom(0, 35);
        randomMaterialBlock = materialList.get(randomMaterialIndex);

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Выкиньте предмет", randomMaterialBlock.name(), 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Выкиньте предмет " + ChatColor.LIGHT_PURPLE + "[" + randomMaterialBlock.name() + "]");

            for (Material block : materialList)
                player.getInventory().addItem(new ItemStack(block, 64));
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

        for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 64));

    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isActivated) return;

        Player player = event.getPlayer();

        if (event.getItemDrop().getItemStack().getType().equals(randomMaterialBlock)) {
            RoundSystem.addScore(player, 1);
            DropNext(player);
        } else
            RoundSystem.playerLose(player);

        if (!(RoundSystem.isRoundTimerEnabled)) {
            isActivated = RoundSystem.isRoundTimerEnabled;
        }
    }
}