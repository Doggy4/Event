package QueueSystem;

import Game.GameCycle;
import Game.RoundSystem;
import PluginUtilities.Chat;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

// Сложна вырубай
public class PrestartScoreBoard {

    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    public static Team red = scoreboard.registerNewTeam("RED");
    public static Team yellow = scoreboard.registerNewTeam("YELLOW");
    public static Team green = scoreboard.registerNewTeam("GREEN");

    public static Objective objective = scoreboard.registerNewObjective("divider1", "dummy", ChatColor.AQUA + "[" + ChatColor.YELLOW + "EVENT" + ChatColor.AQUA + "]");

    public static void setMainBoardSettings() {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        red.setPrefix(ChatColor.RED + "[RED] ");
        yellow.setPrefix(ChatColor.YELLOW + "[YELLOW] ");
        green.setPrefix(ChatColor.GREEN + "[GREEN] ");

        objective.unregister();
        Objective objective = scoreboard.registerNewObjective("divider1", "dummy", ChatColor.AQUA + "[" + ChatColor.YELLOW + "EVENT" + ChatColor.AQUA + "]");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score divider1 = objective.getScore(ChatColor.AQUA + Chat.divThick16);
        divider1.setScore(-4);

        if (!GameCycle.isGameStarted && GameCycle.isGameTimerStarted) {
            Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.BLUE + "Начало " + ChatColor.WHITE + "[" + GameCycle.mainSecPreStart + "сек]");
            gameState.setScore(-2);
        } else if (GameCycle.isGameStarted) {
            Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.GREEN + "Активна");
            gameState.setScore(-2);
            Score battle = objective.getScore(ChatColor.GOLD + "Раунд: " + ChatColor.GREEN + RoundSystem.round);
            battle.setScore(-3);

            Score divider2 = objective.getScore(ChatColor.AQUA + Chat.divThick16);
            divider2.setScore(31);
            for (Player player : Queue.redQueueList) {
                Score score = objective.getScore(ChatColor.GOLD + player.getName());
                score.setScore(GameCycle.gameStats.get(player);
                player.setLevel(GameCycle.gameStats.get(player));
            }
        } else {
            Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.GREEN + "Ожидание...");
            gameState.setScore(-2);
        }

        Score divider2 = objective.getScore(ChatColor.AQUA + Chat.divThick16);
        divider2.setScore(-1);
    }

    public static void PrestartScoreboard() {
        // Игровой раннабл
        new BukkitRunnable() {
            @Override
            public void run() {
                // ГЛАВНЫЕ ЭЛЕМЕНТЫ БОРДА
                setMainBoardSettings();
                // УСТАНОВКА БОРДА
                for (Player player : Bukkit.getOnlinePlayers())
                    player.setScoreboard(scoreboard);
                // ГЛАВНЫЙ ИГРОВОЙ ЦИКЛ
                GameCycle.mainCycle();
            }
        }.runTaskTimer(Main.main, 10, 10);
    }

}
