package Game;

import PluginUtilities.Chat;
import PluginUtilities.LocationUtulities;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import WebHooks.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static PluginUtilities.Chat.divThick16;

public class aRoundSystem {

    public static HashMap<Player, Integer> roundStats = new HashMap<Player, Integer>();

    public static int round = 1;
    public static int roundCount = 10;
    public static int roundSeconds = 30;
    public static boolean isRoundStarted = false;
    public static boolean isRoundTimerEnabled = false;

    public static int randomGame;
    public static int curTicker = 0;

    public static void roundTimer() {
        if (!isRoundTimerEnabled) {
            curTicker = 0;
            isRoundTimerEnabled = true;
        }

        curTicker++;

        if (roundSeconds <= curTicker) {
            aRoundSystem.endRound();
            isRoundTimerEnabled = false;
        }
    }

    public static void startRound() {
        isRoundStarted = true;
        GameRules.TurnOnAllRules();

        for (Player player : Queue.redQueueList) {
            playerReset(player);
            player.setGameMode(GameMode.ADVENTURE);
            LocationUtulities.teleportToSpawn(player);
        }

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (!(entity instanceof Player && !(entity instanceof ArmorStand)) && !(entity instanceof ItemFrame)) {
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

        randomGame = Utilities.getRandom(0, 15);
        switch (randomGame) {
            case 0:
                RoundPlaceTheBlock.placeBlock();
                break;
            case 1:
                RoundDropTheItem.DropTheItem();
                break;
            case 2:
                RoundHitTheBlock.hitTheBlock();
                break;
            case 3:
                RoundTrimTheSheep.trimTheSheep();
                break;
            case 4:
                RoundThrowTheEgg.throwTheEgg();
                break;
            case 5:
                RoundMilkTheCow.milkTheCow();
                break;
            case 6:
                RoundTheRightCombination.rightCombination();
                break;
            case 7:
                RoundReachTheSky.reachTheSky();
                break;
            case 8:
                RoundAnvilEscape.anvilEscape();
                break;
            case 9:
                RoundCakeParkour.cakeParkour();
                break;
            case 10:
                RoundMath.math();
                break;
            case 11:
                RoundKnockEveryoneOff.knockEveryoneOff();
                break;
            case 12:
                RoundHarryPotter.harryPotter();
                break;
            case 13:
                RoundSlimePvP.slimePvP();
                break;
            case 14:
                RoundLavaFloor.lavaFloor();
                break;
            case 15:
                RoundHideUnderBlocks.hideUnderBlocks();
                break;
        }
    }

    public static void endRound() {
        disableRoundEvents();
        isRoundStarted = false;

        for (Player player : Bukkit.getOnlinePlayers())
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 10, 1);

        if (aRoundSystem.round == aRoundSystem.roundCount) {
            GameCycle.endGame();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.YELLOW + divThick16 + ChatColor.WHITE + "\nРаунд " + ChatColor.AQUA + round + ChatColor.WHITE + " завершен! \nСтатистика: \n" + ChatColor.GREEN + String.join("\n", getStats()) + ChatColor.YELLOW + "\n" + divThick16);
        }
        DiscordWebhook.roundEnded();

        startRound();
    }

    private static void disableRoundEvents() {
        RoundPlaceTheBlock.isActivated = false;
        RoundDropTheItem.isActivated = false;
        RoundHitTheBlock.isActivated = false;
        RoundTrimTheSheep.isActivated = false;
        RoundThrowTheEgg.isActivated = false;
        RoundMilkTheCow.isActivated = false;
        RoundTheRightCombination.isActivated = false;
        RoundReachTheSky.isActivated = false;
        RoundAnvilEscape.isActivated = false;
        RoundCakeParkour.isActivated = false;
        RoundMath.isActivated = false;
        RoundHarryPotter.isActivated = false;

        if (RoundKnockEveryoneOff.isActivated) RoundKnockEveryoneOff.endKnockOff();
        if (RoundSlimePvP.isActivated) RoundSlimePvP.endSlimePvP();
        if (RoundLavaFloor.isActivated) RoundLavaFloor.endLavaFloor();
        if (RoundHideUnderBlocks.isActivated) RoundHideUnderBlocks.endHideUnderBlocks();
    }

    public static List<String> getStats() {
        List<String> stats = new ArrayList<String>();
        List<Player> winners = new ArrayList<Player>();

        for (Player player : Queue.redQueueList) {
            int max = -1;
            if (roundStats.get(player) > max && !(winners.contains(player))) {
                max = roundStats.get(player);
                winners.add(player);
            }
        }

        for (int i = 0; i < 3; i++) {
            stats.add((i + 1) + ". " + winners.get(i).getName() + " [" + roundStats.get(winners.get(i)) + "]");
        }

        return stats;
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
                playerReset(roundPlayer);
            } else {
                GameCycle.gameStats.put(roundPlayer, scoreForWin + GameCycle.gameStats.get(roundPlayer));
                roundPlayer.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + scoreForWin + scoreString + "за этот раунд");
                playerReset(roundPlayer);
            }
        }
    }

    public static void playerLose(Player loser) {
        Chat.broadcastToEveryone(ChatColor.RED + "Игрок " + loser.getName() + " проиграл!");

        loser.setGameMode(GameMode.SPECTATOR);
        loser.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы проиграли!");
        loser.playSound(loser.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);

        playerReset(loser);
    }

    public static void playerWin(Player player) {
        Chat.broadcastToEveryone(ChatColor.GREEN + "Игрок " + player.getName() + " победил в раунде!");

        player.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы победили!", 20, 20, 20);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);

        aRoundSystem.addScore(player, 5);
        aRoundSystem.playerReset(player);
    }

    public static void playerReset(Player player) {
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getActivePotionEffects().clear();
    }
}
