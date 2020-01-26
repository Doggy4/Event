package Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.*;

public class ScoreSystemTest {
    private static int scoreBonus;

    private static HashMap<String, Integer> playerStats = new HashMap<String, Integer>();
    private static int topCount = 3;

    public static void addScore(Player winner, int score) {
        HashMap<String, Integer> scoreNames = new HashMap<String, Integer>();

        int scoreBonus1 = 1;
        int scoreBonus2 = 2;
        int scoreBonus5 = 5;
        int scoreBonus10 = 10;

        String score1 = " Очко.";
        String score2 = " Очка.";
        String score3 = " Очков.";

        switch (scoreBonus) {
            case 1:
                scoreNames.put(score1, scoreBonus1);
                break;
            case 2:
                scoreNames.put(score2, scoreBonus2);
                break;
            case 5:
                scoreNames.put(score3, scoreBonus5);
                break;
            case 10:
                scoreNames.put(score3, scoreBonus10);
                break;
        }

        Map.Entry<String, Integer> entry = scoreNames.entrySet().iterator().next();
        String scoreName = entry.getKey();
        int scoreCount = entry.getValue();
        playerStats.put(winner.getName(), scoreCount);
        winner.sendMessage(ChatColor.GOLD + "[Event]" + ChatColor.WHITE + "Вы получини +" + scoreBonus + scoreName);
    }

    public static void selectWinner() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            System.out.println("KEY SET: " + playerStats.keySet());
            System.out.println("VALUE: " + playerStats.values());
        }
    }
}
