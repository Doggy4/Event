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
    // MainCycle
    public static HashMap<String, Integer> gameStats = new HashMap<String, Integer>();
    public static int round;
    private static int roundCount = 10;

    private static int mainSecPreStart = 60;
    public static boolean isGameStarted = false;
    public static boolean isGameTimerStarted = false;

    // RoundCycle
    private static HashMap<String, Integer> roundStats = new HashMap<String, Integer>();
    public static int eventSeconds = 30;
    public static boolean isRoundStarted = false;
    public static boolean isRoundTimerStarted = false;

    // Main // // Main // // Main // // Main // // Main // // Main // // Main // // Main // // Main // // Main // // Main // // Main // // Main //
    public static void mainCycle() {
        // START
        if (mainSecPreStart <= 0 && !isGameStarted) {
            PlayerReset();
        }
        // END
        if (round > roundCount && isGameStarted && !isRoundStarted) {
            EndGame();
        } else if (!isRoundTimerStarted && isGameStarted) {
            nextRound();
        }
        seconds();
    }

    public static void StartGame() {
        mainSecPreStart = 60;
        isGameStarted = false;
        isGameTimerStarted = true;
    }

    private static void PlayerReset() {
        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);

            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setExp(0);
            player.getActivePotionEffects().clear();
            player.setGameMode(GameMode.ADVENTURE);

            CommandEvent.teleportToSpawn(player);
            roundStats.put(playerName, 0);
        }
        isGameStarted = true;
    }

    public static void EndGame() {
        isRoundTimerStarted = false;
        isGameStarted = false;
        mainSecPreStart = 60;
        round = 1;

        int max = 0;
        String winner = "null";

        for (String name : Queue.redQueueList)
            if (aGameCycle.gameStats.get(name) > max) {
                max = roundStats.get(name);
                winner = name;
            }

        Player player = Bukkit.getPlayer(winner);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(ChatColor.GOLD + winner, ChatColor.BLUE + "победитель", 20, 40, 20);

            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            onlinePlayer.spawnParticle(Particle.DRAGON_BREATH, onlinePlayer.getLocation(), 80);
            onlinePlayer.sendMessage(ChatColor.BLUE + "Игрок " + ChatColor.GOLD + winner + ChatColor.BLUE + " победитель!");
        }

        for (String playerName : Queue.redQueueList) {
            Player redPlayer = Bukkit.getPlayer(playerName);
            CommandEvent.teleportToLobby(redPlayer);

            PrestartScoreBoard.red.removeEntry(playerName);
        }

        Queue.redQueueList.clear();

        for (String playerName : Queue.yellowQueueList) {
            Queue.redQueueList.add(playerName);

            Player yellowPlayer = Bukkit.getPlayer(playerName);
            PrestartScoreBoard.red.addEntry(playerName);
            yellowPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]", ChatColor.LIGHT_PURPLE + "Приготовьтесь к игре!", 20, 20, 20);
            yellowPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]");
            yellowPlayer.playSound(yellowPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }

        Queue.yellowQueueList.clear();

        for (int i = 0; i < 10; i++) {
            String playerName = Queue.greenQueueList.get(i);

            if (playerName == null) break;

            Queue.greenQueueList.remove(i);
            Queue.yellowQueueList.add(playerName);

            Player greenPlayer = Bukkit.getPlayer(playerName);
            PrestartScoreBoard.yellow.addEntry(playerName);
            greenPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]", ChatColor.LIGHT_PURPLE + "Ожидайте!", 20, 20, 20);
            greenPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]");
            greenPlayer.playSound(greenPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }
    }

    public static void seconds() {
        if (mainSecPreStart == 59) {
            broadcastToEveryone(ChatColor.GREEN + "Внимание! Начало игры!");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.RED + "Внимание!", ChatColor.BLUE + "Начало игры!", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (mainSecPreStart == 30 || mainSecPreStart == 15 || mainSecPreStart == 10 ||mainSecPreStart == 5) {
            broadcastToEveryone(ChatColor.BLUE + "Начало игры через " + ChatColor.GOLD + mainSecPreStart + ChatColor.BLUE + " секунд");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.BLUE + "Начало игры через", ChatColor.GOLD + "" + mainSecPreStart + ChatColor.BLUE + " секунд", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (mainSecPreStart < 5 && mainSecPreStart > 1) {
            broadcastToEveryone(ChatColor.BLUE + "Начало игры через " + ChatColor.GOLD + mainSecPreStart + ChatColor.BLUE + " секунд");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.BLUE + "Начало игры через", ChatColor.GOLD + "" + mainSecPreStart + ChatColor.BLUE + " секунд", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (mainSecPreStart == 1) {
            broadcastToEveryone(ChatColor.BLUE + "Начало игры через " + ChatColor.GOLD + mainSecPreStart + ChatColor.BLUE + " секунду");
            for (Player player : Bukkit.getOnlinePlayers()) {
                broadcastToEveryone(ChatColor.BLUE + "Начало игры через " + ChatColor.GOLD + mainSecPreStart + ChatColor.BLUE + " секунду");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (mainSecPreStart == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.BLUE + "Игра началась!", ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                player.sendMessage(ChatColor.AQUA + divThick16 + ChatColor.GOLD + "[EVENT] " + ChatColor.BLUE + "Игра началась!\n" + ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]\n" + ChatColor.YELLOW + divThick32);
            }
        }
    }
// ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND // ROUND
    public static void addScore(Player winner, int score) {
        String scoreString;

        if (score == 1)
            scoreString = " Очко";
        else if (score > 1 && score < 5)
            scoreString = " Очка";
        else
            scoreString = " Очков";

        roundStats.put(winner.getName(), score);
        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Вы получили +" + score + scoreString);
    }

    private static List<Integer> list = new ArrayList<>(roundStats.values());


    public static void StartRound() {
        isRoundStarted = true;
        RoundTimer();
    }

    public static void EndRound() {
        // Сортировка мапы по очкам и выводим в зависимости от места playerWin или playerLose
        isRoundTimerStarted = false;
        isRoundStarted = false;

        System.out.println(roundStats);
        System.out.println(list);
        nextRound();
    }



    public static void RoundTimer() {
        isRoundTimerStarted = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                eventSeconds--;
            }
        }.runTaskTimer(Main.main, 10, 10);
    }

    public static void nextRound() {
        RoundSystem.isRoundStarted = true;
        BaseClass.TurnOnAllRules();

        for (String playerName : Queue.redQueueList){
            Player player = Bukkit.getPlayer(playerName);
            player.setGameMode(GameMode.ADVENTURE);
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

    public static void playerWin(Player winner, Integer place, Integer scoreCount) {
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + winner.getName() + " занял " + ChatColor.BLUE + place + ChatColor.WHITE + " место!");

        winner.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы заняли " + place + " место!", 20, 20, 20);
        winner.playSound(winner.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);

        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Вы получили +" + (4 - place) + " очков(-а)!");
        gameStats.put(winner.getName(), 4 - place + gameStats.get(winner.getName()));

        reset(winner);
    }

    public static void playerLose(Player loser, Integer place, Integer scoreCount) {
        broadcastToEveryone(ChatColor.RED + "Игрок " + loser.getName() + " проиграл!");
        loser.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.RED + "Вы проиграли!");
        loser.playSound(loser.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);

        reset(loser);
    }

    public static void reset(Player player) {
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
    }
}
