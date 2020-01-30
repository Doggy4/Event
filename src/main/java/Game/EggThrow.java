package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class EggThrow implements Listener {
    private static boolean isActivated = false;

    public static void EggThrow() {
        isActivated = true;
        RoundSystem.roundSeconds = 15;

        ArrayList<Material> materials = new ArrayList<Material>();
        Collections.addAll(materials, Material.values());

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        materials.subList(randomMaterial, randomMaterial + 36);

        int randomSlot = Utilities.getRandom(0, 35);

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Бросьте яйцо!", "Поторпитесь!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Бросьте яйцо!");

            for (Material block : materials) player.getInventory().addItem(new ItemStack(block, 1));

            player.getInventory().setItem(randomSlot, new ItemStack(Material.EGG));
        }
    }

    public static void throwNext(Player player) {
        ArrayList<Material> materials = new ArrayList<Material>();
        Collections.addAll(materials, Material.values());

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        materials.subList(randomMaterial, randomMaterial + 36);

        int randomSlot = Utilities.getRandom(0, 35);

        player.getInventory().clear();

        player.sendTitle(ChatColor.GREEN + "Еще одно!", "Поторпитесь!", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Бросьте еще одно!");

        for (Material block : materials) player.getInventory().addItem(new ItemStack(block, 1));

        player.getInventory().setItem(randomSlot, new ItemStack(Material.EGG));
    }

    @EventHandler
    public void onPlayerThrowEgg(PlayerEggThrowEvent e) {
        if (!isActivated) return;

        Player player = e.getPlayer();

        RoundSystem.addScore(player, 1);
        throwNext(player);

        if (!(RoundSystem.isRoundTimerEnabled)) {
            isActivated = RoundSystem.isRoundTimerEnabled;
        }
    }
}
