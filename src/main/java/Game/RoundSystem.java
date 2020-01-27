package Game;

import Commands.CommandEvent;
import PluginUtilities.Utilities;
import QueueSystem.PrestartScoreBoard;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static PluginUtilities.Chat.*;

public class RoundSystem {

    public static HashMap<String, Integer> roundStats = new HashMap<String, Integer>();

    public static int round = 1;
    public static int roundCount = 10;
    public static int roundSeconds = 30;
    public static boolean isRoundStarted = false;
    public static boolean isRoundTimerStarted = false;

    private static List<Integer> list = new ArrayList<>(roundStats.values());

    public static void RoundTimer() {
        isRoundTimerStarted = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                roundSeconds--;
            }
        }.runTaskTimer(Main.main, 10, 10);
    }

    public static void StartRound() {
        isRoundStarted = true;
        BaseClass.TurnOnAllRules();
        RoundTimer();

        for (String playerName : Queue.redQueueList){
            Player player = Bukkit.getPlayer(playerName);
            player.setGameMode(GameMode.ADVENTURE);
            roundStats.put(playerName, 0);
            CommandEvent.teleportToSpawn(player);
        }

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Обработка... Выбор мини-режима...");
                    player.sendTitle(ChatColor.YELLOW + "Приготовьтесь!", ChatColor.WHITE + "Выбор мини-режима...", 20, 20, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                }
            }
        }.runTaskLater(Main.main, 2 * 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.YELLOW + divThick32 + ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Раунд: " + ChatColor.AQUA + round + ChatColor.YELLOW + divThick32);
                    player.sendTitle(ChatColor.YELLOW + "РАУНД " + ChatColor.AQUA + round, ChatColor.BLUE + "Начинаем!", 20, 20, 20);
                    player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                }
                round++;
            }
        }.runTaskLater(Main.main, 5 * 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 10, 1);

                int randomBattle = Utilities.getRandom(5, 8);
                switch (randomBattle) {
                    case 0:
                        PlaceBlock.placeBlock();
                        break;
                    case 1:
                        DropItem.DropItem();
                        break;
                    case 2:
                        BowShoot.BowShoot();
                        break;
                    case 3:
                        ShearSheep.ShearSheep();
                        break;
                    case 4:
                        EggThrow.EggThrow();
                        break;
                    case 5:
                        CowMilk.CowMilk();
                        break;
                    case 6:
                        BuildTower.BuildTower();
                        break;
                    case 7:
                        ReachSky.ReachSky();
                        break;
                    case 8:
                        DodgeAnvils.DodgeAnvils();
                        break;
                }
            }
        }.runTaskLater(Main.main, 8 * 20);
    }

    public static void EndRound() {
        isRoundTimerStarted = false;
        isRoundStarted = false;

        LinkedList<Map.Entry<String, Integer>> list = new LinkedList<>(roundStats.entrySet());
        Comparator<Map.Entry<String, Integer>> comparator = Comparator.comparing(Map.Entry::getValue);
        Collections.sort(list, comparator.reversed());

        System.out.println(list);
        // Сортировка, потом применение функции playerPlace()
        StartRound();
    }

    public static void addScore(Player winner, int score) {
        String scoreString;

        if (score == 1) {
            scoreString = " Очко ";
        } else if (score > 1 && score < 5) {
            scoreString = " Очка ";
        } else {
            scoreString = " Очков ";
        }

        roundStats.put(winner.getName(), score);
        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + score + scoreString);
    }

    public static void playerPlace(Player roundPlayer, Integer place, Integer scoreCount) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (place <= 3) {
                player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + roundPlayer.getName() + " занял " + ChatColor.BLUE + place + ChatColor.WHITE + " место! С " + scoreCount + "очками");
                roundPlayer.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы заняли " + place + " призовое место!", 20, 20, 20);
                roundPlayer.playSound(roundPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);
            } else {
                roundPlayer.sendTitle(ChatColor.RED + "К сожалению...", "Вы не заняли  призовое место :C", 20, 20, 20);
                roundPlayer.playSound(roundPlayer.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);
            }

            int scoreForWin = 4 - place;
            String scoreString;

            if (scoreForWin == 1) {
                scoreString = " Очко ";
            } else if (scoreForWin > 1 && scoreForWin < 5) {
                scoreString = " Очка ";
            } else {
                scoreString = " Очков ";
            }

            if (scoreForWin <= 0) {
                roundPlayer.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы не получаете очков за этот раунд");
                PlayerReset(roundPlayer);
            } else {
                GameCycle.gameStats.put(roundPlayer.getName(), scoreForWin + GameCycle.gameStats.get(roundPlayer.getName()));
                roundPlayer.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + scoreForWin + scoreString + "за этот раунд");
                PlayerReset(roundPlayer);
            }
        }
    }

    public static void playerLose(Player loser) {
        loser.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы проиграли!");
        loser.playSound(loser.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);

        PlayerReset(loser);
    }

    public static void PlayerReset(Player player) {
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
    }
}
