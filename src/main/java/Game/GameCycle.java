package Game;

import Commands.CommandEvent;
import QueueSystem.MainScoreBoard;
import QueueSystem.Queue;
import event.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static Game.RoundSystem.*;
import static PluginUtilities.Chat.divThick16;
import static PluginUtilities.Chat.divThick32;


public class GameCycle {

    public static HashMap<Player, Integer> gameStats = new HashMap<Player, Integer>();

    public static boolean isGameStarted = false;
    public static boolean isGameTimerStarted = false;
    public static boolean isCommandStartEventTipped = false;

    public static void mainCycle() {
        // START
        if (isCommandStartEventTipped && !isGameStarted)
            MainScoreBoard.countdown();

        if (isGameStarted && !isRoundStarted)
            new BukkitRunnable() {
                @Override
                public void run() {
                    RoundSystem.startRound();
                }
            }.runTaskLater(Main.main, 4 * 20);
    }

    public static void StartGame() {
        for (Player player : Queue.redQueueList) {
            player.setExp(0);
            PlayerReset(player);
            CommandEvent.teleportToSpawn(player);
            roundStats.put(player, 0);
            gameStats.put(player, 0);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BLUE + "Игра началась!", ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
            player.sendMessage(ChatColor.AQUA + divThick16 + ChatColor.GOLD + "[EVENT] " + ChatColor.BLUE + "Игра началась!\n" + ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]\n" + ChatColor.YELLOW + divThick32);
        }

        isGameStarted = true;
    }

    public static void endGame() {
        isRoundTimerStarted = false;
        isGameStarted = false;
        MainScoreBoard.mainSecPreStart = 60;
        round = 1;

        int max = 0;
        String winner = "null";

        for (Player redPlayer : Queue.redQueueList)
            if (gameStats.get(redPlayer) > max) {
                max = roundStats.get(redPlayer);
                winner = redPlayer.getName();
            }


        // Переопределение очередей
        Player player = Bukkit.getPlayer(winner);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(ChatColor.GOLD + winner, ChatColor.BLUE + "победитель", 20, 40, 20);

            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            onlinePlayer.spawnParticle(Particle.DRAGON_BREATH, onlinePlayer.getLocation(), 80);
            onlinePlayer.sendMessage(ChatColor.BLUE + "Игрок " + ChatColor.GOLD + winner + ChatColor.BLUE + " победитель!");
        }

        for (Player redPlayer : Queue.redQueueList) {
            CommandEvent.teleportToLobby(redPlayer);
            MainScoreBoard.red.removeEntry(player.getName());
        }

        Queue.redQueueList.clear();

        for (Player yellowPlayer : Queue.yellowQueueList) {
            Queue.redQueueList.add(yellowPlayer);

            MainScoreBoard.red.addEntry(yellowPlayer.getName());
            yellowPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]", ChatColor.LIGHT_PURPLE + "Приготовьтесь к игре!", 20, 20, 20);
            yellowPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.RED + "[RED]");
            yellowPlayer.playSound(yellowPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }

        Queue.yellowQueueList.clear();

        for (int i = 0; i < 10; i++) {
            Player greenPlayer = Queue.greenQueueList.get(i);

            if (greenPlayer == null) break;

            Queue.greenQueueList.remove(i);
            Queue.yellowQueueList.add(greenPlayer);

            MainScoreBoard.yellow.addEntry(greenPlayer.getName());
            greenPlayer.sendTitle(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]", ChatColor.LIGHT_PURPLE + "Ожидайте!", 20, 20, 20);
            greenPlayer.sendMessage(ChatColor.YELLOW + "Вы определены в " + ChatColor.YELLOW + "[YELLOW]");
            greenPlayer.playSound(greenPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 20, 1);
        }
    }


}
