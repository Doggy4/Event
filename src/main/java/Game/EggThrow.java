package Game;

import PluginUtilities.MapRebuild;
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
    protected static boolean isActivated = false;

    private static ArrayList<String> phrases = new ArrayList<String>();
    private static ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));

    static {
        phrases.add("Кидай яйцо!");
        phrases.add("Кидай яичко!");
        phrases.add("Бросай яйцо!");
        phrases.add("Бросай яичко!");
        phrases.add("Швырните яйцо!");
        phrases.add("Швырните яичко!");
        phrases.add("Яиц мало не бывает, бросай еще!");
        phrases.add("Не останавливайся, кидай яйца!");
        phrases.add("Ещё больше яиц!");
        phrases.add("Швыряй еще яйца!");
        phrases.add("Сделай омлет, брось яйцо!");
        phrases.add("Сделай яишницу, кинь яичко!");
        phrases.add("Кидай яйцо вновь!");
        phrases.add("Кидай еще одно!");
    }

    public static void eggThrow() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 15;
        MapRebuild.loadSchematic("arena");

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew = materials.subList(randomMaterial, randomMaterial + 36);

        int randomSlot = Utilities.getRandom(0, 35);

        for (Player player : Queue.redQueueList) {
            gameRulesAnnouncement(player);
            for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));
            player.getInventory().setItem(randomSlot, new ItemStack(Material.EGG));
        }
    }

    public static void throwNext(Player player) {
        ArrayList<Material> materials = new ArrayList<Material>(Arrays.asList(Material.values()));

        int randomMaterial = Utilities.getRandom(0, materials.size() - 37);
        List<Material> materialsNew = materials.subList(randomMaterial, randomMaterial + 36);


        gameRulesAnnouncement(player);

        player.getInventory().clear();
        for (Material block : materialsNew) player.getInventory().addItem(new ItemStack(block, 1));

        int randomSlot = Utilities.getRandom(0, 35);
        player.getInventory().setItem(randomSlot, new ItemStack(Material.EGG));
    }

    public static void disableEvents() {
        isActivated = false;
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + phrases.get(Utilities.getRandom(0, phrases.size() - 1)), "Поторпитесь!", 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + phrases.get(Utilities.getRandom(0, phrases.size() - 1)));
    }

    @EventHandler
    public void onPlayerThrowEgg(PlayerEggThrowEvent e) {
        if (!isActivated) return;
        Player player = e.getPlayer();
        if (!(Queue.redQueueList.contains(player))) return;

        RoundSystem.addScore(player, 1);
        throwNext(player);
    }
}
