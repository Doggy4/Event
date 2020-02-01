package Game;

import Commands.CommandEvent;
import PluginUtilities.Chat;
import QueueSystem.MainScoreBoard;
import QueueSystem.Queue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class GameCycle {

    public static HashMap<Player, Integer> gameStats = new HashMap<Player, Integer>();

    public static boolean isGameStarted = false;
    public static boolean isGameTimerStarted = false;
    public static boolean isCommandStartEventTipped = false;

    public static void mainCycle() {

        if (RoundSystem.round >= RoundSystem.roundCount && isGameStarted && !RoundSystem.isRoundStarted) {
            endGame();
            return;
        }
        // START
        if (isCommandStartEventTipped && !isGameStarted)
            MainScoreBoard.countdown();

        else if (isGameStarted && !RoundSystem.isRoundStarted && !RoundSystem.isRoundTimerEnabled)
            RoundSystem.startRound();

        else if (isGameStarted)
            RoundSystem.roundTimer();
    }

    public static void StartGame() {
        for (Player player : Queue.redQueueList) {
            player.setExp(0);
            RoundSystem.PlayerReset(player);
            CommandEvent.teleportToSpawn(player);
            RoundSystem.roundStats.put(player, 0);
            gameStats.put(player, 0);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BLUE + "Игра началась!", ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]", 30, 30, 30);
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 1);
            player.sendMessage(ChatColor.AQUA + Chat.divThick16 + ChatColor.GOLD + "[EVENT] " + ChatColor.BLUE + "Игра началась!\n" + ChatColor.GOLD + "Играют: " + ChatColor.RED + "[RED]\n" + ChatColor.YELLOW + Chat.divThick32);
        }
        isGameStarted = true;
    }

    public static void endGame() {
        isGameStarted = false;
        MainScoreBoard.mainSecPreStart = 60;
        RoundSystem.round = 1;

        int max = 0;
        String winner = "null";

        for (Player redPlayer : Queue.redQueueList)
            if (gameStats.get(redPlayer) > max) {
                max = RoundSystem.roundStats.get(redPlayer);
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

    public static Player getWinner() {
        int max = -1;
        Player winner = null;

        for (Player player : Queue.redQueueList)
            if (RoundSystem.roundStats.get(player) > max) {
                max = RoundSystem.roundStats.get(player);
                winner = player;
            }

        return winner;
    }
}
