package Game;

import Commands.StartEvent;
import PluginUtilities.ParticleConstructor;
import PluginUtilities.Utilities;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
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
            broadcastToEveryone(ChatColor.GREEN + "Игра началась! " + ChatColor.YELLOW + "Играют: " + ChatColor.RED + "[RED]");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Игра началась!", ChatColor.YELLOW + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
                player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "Игра началась!\n" + ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
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
            ParticleConstructor.circle(Bukkit.getPlayer(playerName).getLocation(), 1);
        }

        // Кастомизация игроков...
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Queue.redQueueList.contains(player.getName()))
                player.getLocation().getWorld().spawnParticle(Particle.NOTE, player.getLocation().getX(), player.getLocation().getY() + 2.5, player.getLocation().getZ(), 2);

        }

        // Отсчет секунд
        seconds();
    }

    private static void randomBattle() {
        isAnyBattleEnabled = true;
        BaseClass.TurnOffAllRules();


        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Раунд: " + ChatColor.AQUA + battle + ChatColor.YELLOW + "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
            player.sendTitle(ChatColor.YELLOW + "РАУНД " + ChatColor.AQUA + battle, ChatColor.GREEN + "Начинаем!", 20, 20, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                int randomBattle = Utilities.getRandom(0, 4);
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
                }
            }
        }.runTaskLater(Main.main, 5 * 20);
        battle++;
    }

    public static void addScore(Player winner, int place) {
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + "Игрок " + winner.getName() + " занял " + ChatColor.GREEN + place + ChatColor.WHITE + " место!");

        winner.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.GREEN + "+" + (4 - place) + " очков(-а)!");
        gameStats.put(winner.getName(), 4 - place + gameStats.get(winner.getName()));
    }

    public static void broadcastToEveryone(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "[EVENT] " + ChatColor.WHITE + message);
        }
    }
}