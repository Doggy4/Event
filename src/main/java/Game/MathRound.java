package Game;

import PluginUtilities.MapRebuild;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MathRound implements Listener {
    protected static boolean isActivated = false;

    private static HashMap<String, Integer> examples = new HashMap<String, Integer>();
    private static int result = 0;
    private static String example = null;

    static {
        examples.put("2 + 2 * 2 = ?", 6);
        examples.put("(2 + 2) * 2 = ?", 8);
        examples.put("12 * 11 = ?", 134);
        examples.put("23 + 18 = ?", 51);
        examples.put("75 - 37 = ?", 38);
        examples.put("125 ÷ 5 = ?", 25);
        examples.put("2 - 4 * 6 + 8 = ?", -16);
        examples.put("3 + 3 - 3 * 3 = ?", -3);
        examples.put("5 + 3 * 5 = ?", 20);
        examples.put("х - 12 = 78", 64);
        examples.put("9 + х = 54", 45);
        examples.put("3х = 18", 6);
        examples.put("х + 19 = 31", 12);
        examples.put("2 - х = -21", -23);
        examples.put("2х = -16", -8);
        examples.put("-3х = 12", -4);
        examples.put("4х + 2 = 10", 8);
        examples.put("2х = 4", 2);
        examples.put("16 = 2х", 8);
    }

    private static List<String> keysAsArray = new ArrayList<String>(examples.keySet());

    public static void mathRound() {
        // Опционально:
        isActivated = true;
        RoundSystem.roundSeconds = 30;
        MapRebuild.loadSchematic("arena");

        example = keysAsArray.get(Utilities.getRandom(0, keysAsArray.size()));
        result = examples.get(example);

        for (Player player : Queue.redQueueList)
            gameRulesAnnouncement(player);
    }

    private static void gameRulesAnnouncement(Player player) {
        player.sendTitle(ChatColor.GREEN + "Решите пример", ChatColor.YELLOW + example, 40, 40, 40);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Решите пример: " + ChatColor.AQUA + example);
    }

    public void mathRoundNext(Player player) {
        example = keysAsArray.get(new Random().nextInt(keysAsArray.size()));
        result = examples.get(example);
        gameRulesAnnouncement(player);
    }

    @EventHandler
    public void onResult(AsyncPlayerChatEvent event) {
        if (!isActivated) return;
        Player player = event.getPlayer();
        if (!Queue.redQueueList.contains(player)) return;

        if (event.getMessage().equals(result + "")) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Ответ верный!");
            RoundSystem.addScore(player, 10);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Ответ неверный!");
            RoundSystem.addScore(player, -5);
        }

        event.setCancelled(true);

        mathRoundNext(player);
    }
}
