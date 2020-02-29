package RoundSystem;

import PluginUtils.Chat;
import PluginUtils.DiscordWebHooks;
import PluginUtils.LocationUtils;
import PluginUtils.Utils;
import QueueSystem.Queue;
import RoundList.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static PluginUtils.Chat.divThick16;

public class RoundSystem {

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
            RoundSystem.endRound();
            isRoundTimerEnabled = false;
        }
    }

    public static void startRound() {
        isRoundStarted = true;
        RoundRules.TurnOnAllRules();

        for (Player player : Queue.redQueueList) {
            playerReset(player);
            LocationUtils.teleportToSpawn(player);
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

        randomGame = Utils.getRandom(18, 18);
        switch (randomGame) {
            case 0:
                RoundPlaceTheBlock.startRound();
                break;
            case 1:
                RoundDropTheItem.startRound();
                break;
            case 2:
                RoundHitTheBlock.startRound();
                break;
            case 3:
                RoundTrimTheSheep.startRound();
                break;
            case 4:
                RoundThrowTheEgg.startRound();
                break;
            case 5:
                RoundMilkTheCow.startRound();
                break;
            case 6:
                RoundTheRightCombination.startRound();
                break;
            case 7:
                RoundReachTheSky.startRound();
                break;
            case 8:
                RoundAnvilEscape.startRound();
                break;
            case 9:
                RoundCakeParkour.startRound();
                break;
            case 10:
                RoundMath.startRound();
                break;
            case 11:
                RoundKnockEveryoneOff.startRound();
                break;
            case 12:
                RoundHarryPotter.startRound();
                break;
            case 13:
                RoundSlimePvP.startRound();
                break;
            case 14:
                RoundLavaFloor.startRound();
                break;
            case 15:
                RoundHideUnderBlocks.startRound();
                break;
            case 16:
                RoundFeedBob.startRound();
                break;
            case 17:
                RoundCraftItem.startRound();
                break;
            case 18:
                RoundTNTRun.startRound();
                break;
            case 19:
                RoundHotPotato.startRound();
                break;
            case 20:
                RoundMineAnOre.startRound();
                break;
            case 21:
                RoundSnowFight.startRound();
                break;
            case 22:
                RoundDropParkour.startRound();
                break;
        }
    }

    public static void endRound() {
        disableRoundEvents();
        isRoundStarted = false;

        for (Player player : Bukkit.getOnlinePlayers())
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 10, 1);

        if (RoundSystem.round == RoundSystem.roundCount) {
            GameCycle.endGame();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.YELLOW + divThick16 + ChatColor.WHITE + "\nРаунд " + ChatColor.AQUA + round + ChatColor.WHITE + " завершен! \nСтатистика: \n" + ChatColor.GREEN + String.join("\n", getStats()) + ChatColor.YELLOW + "\n" + divThick16);
        }
        DiscordWebHooks.roundEnded();

        startRound();
    }

    private static void disableRoundEvents() {
        RoundPlaceTheBlock.isActivated = false;
        RoundDropTheItem.isActivated = false;
        RoundTrimTheSheep.isActivated = false;
        RoundThrowTheEgg.isActivated = false;
        RoundMilkTheCow.isActivated = false;
        RoundTheRightCombination.isActivated = false;
        RoundCakeParkour.isActivated = false;
        RoundMath.isActivated = false;
        RoundHarryPotter.isActivated = false;
        RoundCraftItem.isActivated = false;
        RoundMineAnOre.isActivated = false;

        if (RoundKnockEveryoneOff.isActivated) RoundKnockEveryoneOff.endKnockOff();
        else if (RoundSlimePvP.isActivated) RoundSlimePvP.endSlimePvP();
        else if (RoundLavaFloor.isActivated) RoundLavaFloor.endLavaFloor();
        else if (RoundHideUnderBlocks.isActivated) RoundHideUnderBlocks.endHideUnderBlocks();
        else if (RoundFeedBob.isActivated) RoundFeedBob.endFeedBob();
        else if (RoundTNTRun.isActivated) RoundTNTRun.endTNTRun();
        else if (RoundAnvilEscape.isActivated) RoundAnvilEscape.endDodgeAnvils();
        else if (RoundHitTheBlock.isActivated) RoundHitTheBlock.endHitTheBlock();
        else if (RoundHotPotato.isActivated) RoundHotPotato.endHotPotato();
        else if (RoundSnowFight.isActivated) RoundSnowFight.endSnowFight();
        else if (RoundDropParkour.isActivated) RoundDropParkour.endDropParkour();
        else if (RoundReachTheSky.isActivated) RoundReachTheSky.endReachTheSky();
    }

    public static List<String> getStats() {
        List<String> stats = new ArrayList<String>();
        List<Player> winners = new ArrayList<Player>();

        int winnersAmount = 3;
        if (Queue.redQueueList.size() < 3)
            winnersAmount = Queue.redQueueList.size();

        for (int i = 0; i < winnersAmount; i++) {
            Player winner = null;
            int max = -1;

            for (Player player : Queue.redQueueList) {
                if (roundStats.get(player) > max && !winners.contains(player)) {
                    max = roundStats.get(player);
                    winner = player;
                }
            }
            winners.add(winner);
        }

        for (Player winner : winners) {
            stats.add((winners.indexOf(winner) + 1) + ". " + winner.getName() + " [" + roundStats.get(winner) + "]");
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
            } else {
                GameCycle.gameStats.put(roundPlayer, scoreForWin + GameCycle.gameStats.get(roundPlayer));
                roundPlayer.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + scoreForWin + scoreString + "за этот раунд");
            }
            playerReset(roundPlayer);
        }
    }

    public static void playerLose(Player player) {
        Chat.broadcastToEveryone(ChatColor.RED + "Игрок " + player.getName() + " проиграл!");
        preReset(player);
        player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы проиграли!");
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);
    }

    public static void playerWin(Player player) {
        Chat.broadcastToEveryone(ChatColor.GREEN + "Игрок " + player.getName() + " победил в раунде!");
        preReset(player);
        player.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы победили!", 20, 20, 20);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);
        RoundSystem.addScore(player, 5);
    }

    public static void preReset(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects())
            if (effect != null)
                player.removePotionEffect(effect.getType());

    }

    public static void playerReset(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects())
            if (effect != null)
                player.removePotionEffect(effect.getType());

    }
}
