package Game;

import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ScoreSystemTest {
    private static HashMap<String, Integer> playerStats = new HashMap<String, Integer>();
    public static int EventSeconds = 30;
    public static boolean isEventStarted = false;
    public static boolean isEventTimerStarted = false;

    public static void addScore(Player winner, int score) {
        String scoreString;

        if (score == 1)
            scoreString = " Очко";
        else if (score > 1 && score < 5)
            scoreString = " Очка";
        else
            scoreString = " Очков";

        playerStats.put(winner.getName(), score);
        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + score + scoreString);
    }

    private static List<Integer> list = new ArrayList<>(playerStats.values());

    public static void Winner() {
        Collections.sort(list);

        System.out.println(playerStats);
        System.out.println(list);
    }

    public static void StartEvent() {
        isEventStarted = true;
        EventTimer();
    }

    public static void EventTimer() {
        isEventTimerStarted = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                EventSeconds--;
            }
        }.runTaskTimer(Main.main, 10, 10);
    }
}
