package Game;

import Commands.CommandEvent;
import Commands.StartEvent;
import PluginUtilities.Chat;
import PluginUtilities.ParticleConstructor;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class aGameCycle {

    public static HashMap<String, Integer> gameStats = new HashMap<String, Integer>();
    public static boolean isAnyBattleEnabled = false;
    public static int battle = 1;

    public static void seconds() {
        if (StartEvent.secPreStart == 58) {
            broadcastToEveryone(ChatColor.GREEN + "Внимание! Эвент запущен!");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.RED + "Внимание!", ChatColor.BLUE + "Эвент запущен!", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (StartEvent.secPreStart == 30 || StartEvent.secPreStart == 15 || StartEvent.secPreStart == 10 || StartEvent.secPreStart == 5) {
            broadcastToEveryone(ChatColor.GREEN + "Начало игры через " + ChatColor.BLUE + "" + StartEvent.secPreStart + " секунд");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Начало игры через", ChatColor.BLUE + "" + StartEvent.secPreStart + " секунд", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (StartEvent.secPreStart < 5 && StartEvent.secPreStart > 1) {
            broadcastToEveryone(ChatColor.GREEN + "Начало игры через " + ChatColor.BLUE + "" + StartEvent.secPreStart + " секунды");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Начало игры через", ChatColor.BLUE + "" + StartEvent.secPreStart + " секунды", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (StartEvent.secPreStart == 1) {
            broadcastToEveryone(ChatColor.GREEN + "Начало игры через " + ChatColor.BLUE + "1 секунду");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Начало игры через", ChatColor.BLUE + "1 секунду", 20, 20, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
            }
        } else if (StartEvent.secPreStart == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Игра началась!", ChatColor.YELLOW + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Игра началась!\n" + ChatColor.YELLOW + "Играют: " + ChatColor.RED + "[RED]\n" + ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
            }
        }
    }

    public static void mainCycle() {
        // Старт игры
        if (StartEvent.secPreStart < 0 && !Commands.StartEvent.isGameStarted) {
            Start.StartGame();
        }

        // Конец игры
        if (battle > 10 && Commands.StartEvent.isGameStarted && !isAnyBattleEnabled) {
            Ending.EndGame();
        } else if (!isAnyBattleEnabled && Commands.StartEvent.isGameStarted) {
            randomBattle();
        }

        // Партиклы над головой игроков очереди RED
        for (String playerName : Queue.redQueueList) {
            Player player = Bukkit.getPlayer(playerName);
            if (player.getGameMode() != GameMode.SPECTATOR)
                ParticleConstructor.circle(player.getLocation(), 1);
        }

        // Кастомизация игроков...
        for (Player player : Bukkit.getOnlinePlayers())
            if (!Queue.redQueueList.contains(player.getName())) {
                player.getLocation().getWorld().spawnParticle(Particle.NOTE, player.getLocation().getX(), player.getLocation().getY() + 2.5, player.getLocation().getZ(), 2);
                player.setHealth(20);
                player.setFoodLevel(20);
            }

        // Отсчет секунд
        seconds();
    }

    private static void randomBattle() {
        isAnyBattleEnabled = true;
        BaseClass.TurnOnAllRules();

        for (String playerName : Queue.redQueueList){
            Player player = Bukkit.getPlayer(playerName);
            player.setGameMode(GameMode.ADVENTURE);
            CommandEvent.teleportToSpawn(player);
        }


        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity.getType() == EntityType.ARROW || entity.getType() == EntityType.DROPPED_ITEM || entity instanceof Mob) {
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
                    player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Раунд: " + ChatColor.AQUA + battle + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
                    player.sendTitle(ChatColor.YELLOW + "РАУНД " + ChatColor.AQUA + battle, ChatColor.GREEN + "Начинаем!", 20, 20, 20);
                    player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                }
                battle++;
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

    public static void broadcastToEveryone(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + message);
        }
    }

    public static void playerWin(Player winner, int place) {
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + winner.getName() + " занял " + ChatColor.GREEN + place + ChatColor.WHITE + " место!");

        winner.sendTitle(ChatColor.GREEN + "Поздравляем!", "Вы заняли " + place + " место!", 20, 20, 20);
        winner.playSound(winner.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 1);

        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Вы получили +" + (4 - place) + " очков(-а)!");
        gameStats.put(winner.getName(), 4 - place + gameStats.get(winner.getName()));

        reset(winner);
    }

    public static void playerLose(Player loser) {
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