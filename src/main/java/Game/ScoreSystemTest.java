package Game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ScoreSystemTest {
    private static HashMap<String, Integer> playerStats = new HashMap<String, Integer>();
    private static int topCount = 3;

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

    public static void selectWinner() {
        Collections.sort(list);

        System.out.println(playerStats);
        System.out.println(list);
    }
}
