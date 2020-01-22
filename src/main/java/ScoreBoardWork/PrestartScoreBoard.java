package ScoreBoardWork;

import Commands.StartEvent;
import Game.GameCycle;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class PrestartScoreBoard {

    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void PrestartScoreboard() {

        Team red = scoreboard.registerNewTeam("RED");
        Team yellow = scoreboard.registerNewTeam("YELLOW");
        Team green = scoreboard.registerNewTeam("GREEN");

        Objective objective = scoreboard.registerNewObjective("divider1", "dummy", ChatColor.AQUA + "[" + ChatColor.YELLOW + "EVENT" + ChatColor.AQUA + "]");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        new BukkitRunnable() {
            @Override
            public void run() {

                red.setPrefix(ChatColor.RED + "[RED] ");
                yellow.setPrefix(ChatColor.YELLOW + "[YELLOW] ");
                green.setPrefix(ChatColor.GREEN + "[GREEN] ");

                objective.unregister();
                Objective objective = scoreboard.registerNewObjective("divider1", "dummy", ChatColor.AQUA + "[" + ChatColor.YELLOW + "EVENT" + ChatColor.AQUA + "]");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score divider1 = objective.getScore(ChatColor.AQUA + "▪▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                divider1.setScore(-3);

                if (StartEvent.isGameTimerStarted && !Commands.StartEvent.isGameStarted) {
                    StartEvent.secPreStart--;
                    Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.BLUE + "Начало " + ChatColor.WHITE + "[" + StartEvent.secPreStart + "сек]");
                    gameState.setScore(-2);
                } else if (Commands.StartEvent.isGameStarted) {
                    Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.GREEN + "Запущена");
                    gameState.setScore(-2);
                    Score divider2 = objective.getScore(ChatColor.AQUA + "▪▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▪");
                    divider2.setScore(31);
                    for (String name : Queue.redQueueList) {
                        Score player = objective.getScore(ChatColor.GOLD + name);
                        player.setScore(GameCycle.gameStats.get(name));
                    }
                } else {
                    Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.GREEN + "Ожидание...");
                    gameState.setScore(-2);
                }

                Score divider2 = objective.getScore(ChatColor.AQUA + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▪");
                divider2.setScore(-1);


                Game.GameCycle.mainCycle();

                for (Player player : Bukkit.getOnlinePlayers())
                    player.setScoreboard(scoreboard);
            }
        }.

                runTaskTimer(Main.main, 10, 10);
    }

}
