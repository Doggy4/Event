package Game;

import Commands.CommandEvent;
import PluginUtilities.Chat;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

import static PluginUtilities.Chat.divThick16;

public class RoundSystem {

    public static HashMap<Player, Integer> roundStats = new HashMap<Player, Integer>();

    public static int round = 1;
    public static int roundCount = 10;
    public static int roundSeconds = 30;
    public static boolean isRoundStarted = false;
    public static boolean isRoundTimerEnabled = false;

    private static int randomGame;

    public static int curTicker = 0;


    public static void roundTimer() {
        if (!isRoundTimerEnabled) {
            curTicker = 0;
            isRoundTimerEnabled = true;
        }

        curTicker++;

        if (roundSeconds <= curTicker) {
            RoundSystem.endRound();
            isRoundTimerEnabled = false;
        }
    }

    public static void startRound() {
        isRoundStarted = true;
        GameRules.TurnOnAllRules();

        for (Player player : Queue.redQueueList) {
            player.setGameMode(GameMode.ADVENTURE);
            CommandEvent.teleportToSpawn(player);
        }

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (!(entity instanceof Player && !(entity instanceof ArmorStand))) {
                entity.remove();
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Обработка... Выбор мини-режима...");
            player.sendTitle(ChatColor.YELLOW + "Приготовьтесь!", ChatColor.WHITE + "Выбор мини-режима...", 20, 20, 20);
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.YELLOW + divThick16 + ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Раунд: " + ChatColor.AQUA + round + "\n" + ChatColor.YELLOW + divThick16);
            player.sendTitle(ChatColor.YELLOW + "РАУНД " + ChatColor.AQUA + round, ChatColor.BLUE + "Начинаем!", 20, 20, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
        }
        round++;

        for (Player player : Bukkit.getOnlinePlayers())
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 10, 1);

        randomGame = Utilities.getRandom(6, 6);
        switch (randomGame) {
            case 0:
                PlaceBlock.PlaceBlock();
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
                CowMilk.MilkCow();
                break;
            case 6:
                PlaceWool.placeWool();
                break;
            case 7:
                ReachSky.ReachSky();
                break;
            case 8:
                DodgeAnvils.DodgeAnvils();
                break;
            case 9:
                ParkourEatCake.parkour();
                break;
        }
    }

    public static void endRound() {
        disableRoundEvents();
        isRoundStarted = false;

        if (RoundSystem.round == RoundSystem.roundCount) {
            GameCycle.endGame();
            return;
        }

        LinkedList<Map.Entry<Player, Integer>> list = new LinkedList<>(roundStats.entrySet());
        Comparator<Map.Entry<Player, Integer>> comparator = Comparator.comparing(Map.Entry::getValue);
        Collections.sort(list, comparator.reversed());

        System.out.println(list);
        // Сортировка, потом применение функции playerPlace()
        startRound();
    }

    private static void disableRoundEvents() {
        switch (randomGame) {
            case 0:
                PlaceBlock.isActivated = false;
                break;
            case 1:
                DropItem.isActivated = false;
                break;
            case 2:
                BowShoot.isActivated = false;
                break;
            case 3:
                ShearSheep.isActivated = false;
                break;
            case 4:
                EggThrow.isActivated = false;
                break;
            case 5:
                CowMilk.isActivated = false;
                break;
            case 6:
                PlaceWool.isActivated = false;
                break;
            case 7:
                ReachSky.isActivated = false;
                break;
            case 8:
                DodgeAnvils.isActivated = false;
                break;
        }
    }

    public static void addScore(Player player, int score) {
        String scoreString;

        if (score == 1 || score == -1) {
            scoreString = " очко ";
        } else if (score > 1 && score < 5 || score < -1 && score > -5) {
            scoreString = " очка ";
        } else {
            scoreString = " очков ";
        }

        if (score > 0) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили " + ChatColor.GREEN + score + ChatColor.WHITE + scoreString);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
        } else {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы потеряли " + ChatColor.RED + (score * -1) + ChatColor.WHITE + scoreString);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 10, 1);
            if (roundStats.get(player) <= 0) {
                player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы достигли минимального количества очков!");
                score = 0;
            }
        }

        roundStats.put(player, score + roundStats.get(player));
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
                GameCycle.gameStats.put(roundPlayer, scoreForWin + GameCycle.gameStats.get(roundPlayer));
                roundPlayer.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + scoreForWin + scoreString + "за этот раунд");
                PlayerReset(roundPlayer);
            }
        }
    }

    public static void playerLose(Player loser) {
        Chat.broadcastToEveryone(ChatColor.RED + "Игрок " + loser.getName() + " проиграл!");

        loser.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы проиграли!");
        loser.playSound(loser.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);

        PlayerReset(loser);
    }

    public static void PlayerReset(Player player) {
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getActivePotionEffects().clear();
    }
}
