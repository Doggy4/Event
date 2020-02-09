package QueueSystem;

import Game.GameCycle;
import Game.aRoundSystem;
import PluginUtilities.Chat;
import event.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import static PluginUtilities.Chat.broadcastToEveryone;

// КЛАСС НЕ ПЕРЕДЕЛЫВАТЬ: ОПТИМИЗИРОВАН ПО МАКСИМУМУ
public class MainScoreBoard {
    // Секунды до начала
    public static int mainSecPreStart = 60;

    // Создаем скорборд
    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    // Регистрируем очереди
    public static Team red = scoreboard.registerNewTeam("RED");
    public static Team yellow = scoreboard.registerNewTeam("YELLOW");
    public static Team green = scoreboard.registerNewTeam("GREEN");
    // Создаем объект
    public static Objective objective = scoreboard.registerNewObjective("divider1", "dummy", ChatColor.AQUA + "[" + ChatColor.YELLOW + "EVENT" + ChatColor.AQUA + "]");

    // Установка опций главного скорборда
    public static void setMainScoreBoardSettings() {
        // Устанавливаем сайдбар
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Устанвливаем префикс
        red.setPrefix(ChatColor.RED + "[RED] ");
        yellow.setPrefix(ChatColor.YELLOW + "[YELLOW] ");
        green.setPrefix(ChatColor.GREEN + "[GREEN] ");

        objective.unregister();
        Objective objective = scoreboard.registerNewObjective("divider1", "dummy", ChatColor.AQUA + "[" + ChatColor.YELLOW + "EVENT" + ChatColor.AQUA + "]");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.GREEN + "Активна");
        Score alternative = null;

        // Если игра запущена:
        if (GameCycle.isGameStarted) {
            alternative = objective.getScore(ChatColor.GOLD + "Раунд: " + ChatColor.GREEN + aRoundSystem.round);
            Score divider2 = objective.getScore(ChatColor.AQUA + Chat.ScoreBoardDivider3);
            divider2.setScore(aRoundSystem.roundStats.get(GameCycle.getWinner()) + 1);

            for (Player player : Queue.redQueueList) {
                Score score = objective.getScore(ChatColor.GOLD + player.getName());

                // Если раунд запущен
                if (aRoundSystem.isRoundStarted) {
                    score.setScore(aRoundSystem.roundStats.get(player));
                    player.setLevel(aRoundSystem.roundStats.get(player));
                } else {
                    score.setScore(GameCycle.gameStats.get(player));
                    player.setLevel(GameCycle.gameStats.get(player));
                }
            }
        } else if (GameCycle.isCommandStartEventTipped)
            alternative = objective.getScore(ChatColor.BLUE + "До начала игры: " + ChatColor.YELLOW + mainSecPreStart);
        else
            gameState = objective.getScore(ChatColor.GOLD + "Статус игры: " + ChatColor.YELLOW + "Ожидание...");

        // Состояние игры
        gameState.setScore(-2);
        if (alternative != null) alternative.setScore(-3);

        // Разделители
        Score divider1 = objective.getScore(ChatColor.AQUA + Chat.ScoreBoardDivider1);
        divider1.setScore(-4);

        Score divider3 = objective.getScore(ChatColor.AQUA + Chat.ScoreBoardDivider2);
        divider3.setScore(-1);
    }

    public static BossBar bossbar = Bukkit.getServer().createBossBar(ChatColor.GOLD + "Ожидание...", BarColor.YELLOW, BarStyle.SEGMENTED_20);

    // Отсчет секунд до старта
    public static void countdown() {
        String secondsString = null;

        if (mainSecPreStart == 59) {
            broadcastToEveryone(ChatColor.GREEN + "Внимание! Начало игры!");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.RED + "Внимание!", ChatColor.BLUE + "Начало игры!", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        }

        if (mainSecPreStart >= 5)
            secondsString = " секунд!";
        else if (mainSecPreStart > 1)
            secondsString = " секунды!";
        else if (mainSecPreStart == 1) {
            secondsString = " секунду!";
        } else if (mainSecPreStart == 0) {
            GameCycle.StartGame();
            mainSecPreStart--;
            return;
        }


        if (mainSecPreStart == 30 || mainSecPreStart == 15 || mainSecPreStart == 10 || mainSecPreStart <= 5) {
            broadcastToEveryone(ChatColor.BLUE + "Начало игры через " + ChatColor.GOLD + mainSecPreStart + ChatColor.BLUE + secondsString);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                player.sendTitle(ChatColor.BLUE + "Начало игры через", ChatColor.GOLD + "" + mainSecPreStart + ChatColor.BLUE + secondsString, 20, 20, 20);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers())
            countdownBar(player);

        mainSecPreStart--;
    }

    private static void timerBar(Player player) {
        bossbar.addPlayer(player);
        if (!GameCycle.isGameStarted) return;
        MainScoreBoard.bossbar.setColor(BarColor.BLUE);
        bossbar.setTitle(ChatColor.AQUA + "До следующего раунда: " + (aRoundSystem.roundSeconds - aRoundSystem.curTicker));
        if ((aRoundSystem.roundSeconds - aRoundSystem.curTicker) * 1.0 / aRoundSystem.roundSeconds < 0)
            bossbar.setProgress(1);
        else
            bossbar.setProgress((aRoundSystem.roundSeconds - aRoundSystem.curTicker) * 1.0 / aRoundSystem.roundSeconds);
        bossbar.setVisible(true);
    }

    private static void countdownBar(Player player) {
        bossbar.setTitle(ChatColor.GREEN + " До начала игры: " + mainSecPreStart);
        MainScoreBoard.bossbar.setColor(BarColor.GREEN);
        bossbar.setProgress(mainSecPreStart / 60.0);
        bossbar.setVisible(true);
        bossbar.addPlayer(player);
    }

    // Запуск главного раннабла
    public static void startPluginRunnable() {
        // Игровой раннабл
        new BukkitRunnable() {
            @Override
            public void run() {
                // ГЛАВНЫЕ ЭЛЕМЕНТЫ БОРДА
                setMainScoreBoardSettings();
                // УСТАНОВКА БОРДА
                for (Player player : Bukkit.getOnlinePlayers()) {
                    timerBar(player);
                    player.setScoreboard(scoreboard);
                }


                // ГЛАВНЫЙ ИГРОВОЙ ЦИКЛ
                GameCycle.mainCycle();
            }
        }.runTaskTimer(Main.main, 60, 20);
    }
}
