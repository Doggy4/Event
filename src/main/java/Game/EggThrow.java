package Game;

import PluginUtilities.Utilities;
import QueueSystem.Queue;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EggThrow implements Listener {

    private static ArrayList<String> phrases = new ArrayList<String>();

    static {
        phrases.add("Больше яиц!");
        phrases.add("Подкати ещё!");
        phrases.add("Яиц мало не бывает!");
        phrases.add("Не останавливайся!");
        phrases.add("Ещё больше!");
        phrases.add("Швыряй еще!");
        phrases.add("Сделай омлет!");
        phrases.add("Сделай яишницу!");
        phrases.add("Кидай яйцо!");
        phrases.add("Кидай вновь!");
        phrases.add("Кидай еще одно!");
    }


    public static boolean isActivated = false;

    public static void EggThrow() {
        isActivated = true;
        RoundSystem.roundSeconds = 15;

        ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew = materials.subList(randomMaterial, randomMaterial + 36);

        int randomSlot = Utilities.getRandom(0, 35);

        for (Player player : Queue.redQueueList) {
            player.getInventory().clear();

            player.sendTitle(ChatColor.GREEN + "Бросьте яйцо!", "Поторпитесь!", 40, 40, 40);
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Бросьте яйцо!");

            for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

            player.getInventory().setItem(randomSlot, new ItemStack(Material.EGG));
        }
    }

    public static void throwNext(Player player) {
        ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew = materials.subList(randomMaterial, randomMaterial + 36);

        int randomSlot = Utilities.getRandom(0, 35);

        player.getInventory().clear();

        player.sendTitle(ChatColor.GREEN + phrases.get(Utilities.getRandom(0, phrases.size() - 1)), "Поторпитесь!", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Бросьте еще одно!");

        for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

        player.getInventory().setItem(randomSlot, new ItemStack(Material.EGG));
    }

    @EventHandler
    public void onPlayerThrowEgg(PlayerEggThrowEvent e) {
        if (!isActivated) return;

        Player player = e.getPlayer();

        RoundSystem.addScore(player, 1);
        throwNext(player);
    }
}
